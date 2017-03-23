package org.jump.service;

import java.nio.file.NoSuchFileException;
import java.sql.SQLException;

import org.jump.entity.ApplicationConfiguration;
import org.jump.entity.ExecutionStatus;
import org.jump.util.Utility;

public class OperationStatusReporter {

    private ApplicationConfiguration applicationConfiguration;

    public OperationStatusReporter(ApplicationConfiguration configuration) {
        this.applicationConfiguration = configuration;
    }

    public void reportError(Exception e) {

        if (e instanceof NoSuchFileException) {

            NoSuchFileException nsfe = (NoSuchFileException)e;
            System.out.println(Utility.getExecutionStatus(ExecutionStatus.FAILED));
            System.out.println("File not accessible: " + nsfe.getFile());
            printException(e, true);
        } else if (e instanceof SQLException) {

            SQLException see = (SQLException)e;
            System.out.println(Utility.getExecutionStatus(ExecutionStatus.FAILED));
            String message = String.format(
                "Error with connection database/executing query: message: [%s] error-code: [%d] sql-state: [%s], Check whether sql-server host is reachable/correct, or error with sql.",
                see.getMessage(),
                see.getErrorCode(),
                see.getSQLState());
            System.out.println(message);
            printException(see, true);
        } else {

            System.out.println(Utility.getExecutionStatus(ExecutionStatus.FAILED));
            System.out.println(e.getMessage());
            printException(e, true);
        }
    }

    private void printException(Exception e, boolean isVerbose) {
        if (isVerbose) {
            e.printStackTrace();
        } else {
            System.out.println("Use verbose command line option for more details, use --verbose");
        }
    }
}
