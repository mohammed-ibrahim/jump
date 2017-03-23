package org.jump.service;

import java.util.List;

import org.jump.entity.ApplicationConfiguration;
import org.jump.entity.ExecutionStatus;
import org.jump.parser.AbstractCommand;

public class Executor {

    public ExecutionStatus execute(ApplicationConfiguration appConfig, List<AbstractCommand> commands) throws Exception {

        TransactionalSqlExecutor sqlExecutor = new TransactionalSqlExecutor(appConfig);
        for (AbstractCommand command: commands) {
            ExecutionStatus executionStatus = runCommand(appConfig, command, sqlExecutor);

            if (executionStatus.equals(ExecutionStatus.MANUAL_ROLLBACK)) {

                return executionStatus;
            }
        }

        if (!appConfig.isDryRun()) {

            sqlExecutor.commitAndClose();
            return ExecutionStatus.SUCCESSFUL;
        } else {

            sqlExecutor.rollbackAndClose();
            return ExecutionStatus.SUCCESSFUL_DRY_RUN;
        }
    }

    private ExecutionStatus runCommand(ApplicationConfiguration appConfig, AbstractCommand command, TransactionalSqlExecutor sqlExecutor) throws Exception {

        switch (command.getType()) {
        case SQL_COMMAND:
            for (String sql: command.getSqlCommand().getSqls()) {

                sqlExecutor.executeUpdate(sql);
            }
            break;

        case INSERT_COMMAND:
            InsertCommandExecutor insertCommandExecutor = new InsertCommandExecutor(appConfig, command.getInsertCommand(), sqlExecutor);
            insertCommandExecutor.execute();
            break;

        case ROLLBACK_COMMAND:
            sqlExecutor.rollbackAndClose();
            return ExecutionStatus.MANUAL_ROLLBACK;

        case BASIC_VARIABLE:
            CacheManager.getInstance().addVariable(command.getVariableCommand().getName(), command.getVariableCommand().getValue());
            break;

        default:
            String message = String.format("Command %s unknown.", command.getType().toString());
            throw new RuntimeException(message);
        }

        return ExecutionStatus.SUCCESSFUL;
    }
}
