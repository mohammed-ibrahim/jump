package org.jump.service;

import java.nio.file.NoSuchFileException;
import java.sql.SQLException;

import org.jump.entity.ApplicationConfiguration;
import org.jump.entity.ExecutionStatus;
import org.jump.exception.ParseFailureException;
import org.jump.util.Utility;

public class OperationStatusReporter {

    private ApplicationConfiguration applicationConfiguration;

    public OperationStatusReporter(ApplicationConfiguration configuration) {
        this.applicationConfiguration = configuration;
    }

    public void reportError(Exception e) {

        if (e instanceof NoSuchFileException) {

            NoSuchFileException nsfe = (NoSuchFileException)e;
            displayMessage(ExecutionStatus.FAILED, "File not accessible: " + nsfe.getFile(), e);
        } else if (e instanceof SQLException) {

            SQLException see = (SQLException)e;

            String message = String.format(
                "Error with connection database/executing query: message: [%s] error-code: [%d] sql-state: [%s], Check whether sql-server host is reachable/correct, or error with sql.",
                see.getMessage(),
                see.getErrorCode(),
                see.getSQLState());
            displayMessage(ExecutionStatus.FAILED, message, see);
        } else if (e instanceof ParseFailureException) {

            ParseFailureException pfe = (ParseFailureException)e;
            String message = String.format("Error in script, token: %s, line: %d char: %d", pfe.getOffendingToken(), pfe.getLine(), pfe.getCharPositionInLine());
            displayMessage(ExecutionStatus.FAILED, message, pfe);
        } else {



            displayMessage(ExecutionStatus.FAILED, e.getMessage(), e);
        }
    }

    private void displayMessage(ExecutionStatus executionStatus, String message, Exception e) {
        System.out.println(Utility.getExecutionStatus(executionStatus));
        System.out.println(message);
        printStackTrace(e);
    }

    private void printStackTrace(Exception e) {
        if (this.applicationConfiguration.isVerbose()) {
            e.printStackTrace();
        } else {
            System.out.println("Use verbose command line option for more details, use --verbose");
        }
    }
}
