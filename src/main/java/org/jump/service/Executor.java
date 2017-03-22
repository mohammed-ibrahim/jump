package org.jump.service;

import java.util.List;

import org.jump.entity.ApplicationConfiguration;
import org.jump.entity.ExecutionStatus;
import org.jump.parser.InsertCommand;
import org.jump.parser.RollbackCommand;
import org.jump.parser.SqlCommand;

public class Executor {

    public ExecutionStatus execute(ApplicationConfiguration appConfig, List<Object> commands) throws Exception {

        TransactionalSqlExecutor sqlExecutor = new TransactionalSqlExecutor(appConfig);
        for (Object command: commands) {
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

    private ExecutionStatus runCommand(ApplicationConfiguration appConfig, Object command, TransactionalSqlExecutor sqlExecutor) throws Exception {
        if (command instanceof SqlCommand) {

            SqlCommand sqlCommand = (SqlCommand)command;
            for (String sql: sqlCommand.getSqls()) {

                sqlExecutor.executeUpdate(sql);
            }

        } else if (command instanceof InsertCommand) {

            InsertCommandExecutor insertCommandExecutor = new InsertCommandExecutor(appConfig, (InsertCommand)command, sqlExecutor);
            insertCommandExecutor.execute();
        } else if (command instanceof RollbackCommand) {

            sqlExecutor.rollbackAndClose();
            return ExecutionStatus.MANUAL_ROLLBACK;
        } else {
            throw new RuntimeException(command.getClass() + " command is not known");
        }

        return ExecutionStatus.SUCCESSFUL;
    }
}
