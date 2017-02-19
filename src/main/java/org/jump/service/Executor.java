package org.jump.service;

import java.util.List;

import org.jump.entity.ApplicationConfiguration;
import org.jump.parser.InsertCommand;
import org.jump.parser.RollbackCommand;
import org.jump.parser.SqlCommand;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Executor {

    public void execute(ApplicationConfiguration appConfig, List<Object> commands) {

        for (Object command: commands) {
            runCommand(appConfig, command);
        }
    }

    private void runCommand(ApplicationConfiguration appConfig, Object command) {
        if (command instanceof SqlCommand) {

            //run sql commands
        } else if (command instanceof InsertCommand) {

            new InsertCommandExecutor(appConfig, (InsertCommand)command).execute();
        } else if (command instanceof RollbackCommand) {

        } else {
            throw new RuntimeException(command.getClass() + " command is not known");
        }
    }
}
