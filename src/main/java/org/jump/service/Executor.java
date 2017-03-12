package org.jump.service;

import java.util.List;

import org.jump.entity.ApplicationConfiguration;
import org.jump.parser.InsertCommand;
import org.jump.parser.RollbackCommand;
import org.jump.parser.SqlCommand;

public class Executor {

    public void execute(ApplicationConfiguration appConfig, List<Object> commands) throws Exception {

        TransactionalSqlExecutor sqlExecutor = new TransactionalSqlExecutor(appConfig);
        boolean keepRunning = true;
        for (Object command: commands) {
            keepRunning = runCommand(appConfig, command, sqlExecutor);

            if (!keepRunning) {

                return;
            }
        }

        sqlExecutor.commitAndClose();
    }

    private boolean runCommand(ApplicationConfiguration appConfig, Object command, TransactionalSqlExecutor sqlExecutor) throws Exception {
        if (command instanceof SqlCommand) {

            SqlCommand sqlCommand = (SqlCommand)command;
            for (String sql: sqlCommand.getSqls()) {
                sqlExecutor.executeUpdate(sql);
            }

        } else if (command instanceof InsertCommand) {

            new InsertCommandExecutor(appConfig, (InsertCommand)command, sqlExecutor).execute();
        } else if (command instanceof RollbackCommand) {

            sqlExecutor.rollbackAndClose();
            return false;
        } else {
            throw new RuntimeException(command.getClass() + " command is not known");
        }

        return true;
    }
}
