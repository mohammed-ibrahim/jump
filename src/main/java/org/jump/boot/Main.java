package org.jump.boot;

import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Paths;
import java.sql.SQLException;

import org.jump.entity.ApplicationConfiguration;
import org.jump.entity.ExecutionStatus;
import org.jump.parser.JumpGen;
import org.jump.parser.ParseResult;
import org.jump.service.CacheManager;
import org.jump.service.Executor;
import org.jump.service.FileLogger;
import org.jump.validation.CloValidator;

public class Main {

    public static void main(String[] args) {

        CloValidator validator = new CloValidator();
        ApplicationConfiguration conf = validator.validate(args);

        try {

            if (!conf.isSuccess()) {

                return;
            }

            String fileContents = getFileContents(conf.getFileName());
            ParseResult result = JumpGen.parse(fileContents);

            if (result.getCommands() == null) {
                System.out.println("There is an error in script: " + conf.getFileName());
                System.out.println(result.getErrorMessage());
                System.exit(0);
            }

            ExecutionStatus executionStatus = new Executor().execute(conf, result.getCommands());
            System.out.println(getExecutionStatus(executionStatus));

            if (CacheManager.getInstance().allkeys().size() > 0) {
                for (String cacheKey: CacheManager.getInstance().allkeys()) {
                    FileLogger.getInstance().writeRow(cacheKey + ": " + CacheManager.getInstance().itemsForKey(cacheKey));
                }
            }

            FileLogger.getInstance().close();
        } catch (NoSuchFileException nsfe) {

            System.out.println(getExecutionStatus(ExecutionStatus.FAILED));
            System.out.println("File not accessible: " + conf.getFileName());
            printException(nsfe, conf);
        } catch (SQLException see) {

            System.out.println(getExecutionStatus(ExecutionStatus.FAILED));
            String message = String.format(
                "Error with connection database/executing query: message: [%s] error-code: [%d] sql-state: [%s], Check whether sql-server host is reachable/correct, or error with sql.",
                see.getMessage(),
                see.getErrorCode(),
                see.getSQLState());
            System.out.println(message);
            printException(see, conf);
        } catch (Exception e) {

            System.out.println(getExecutionStatus(ExecutionStatus.FAILED));
            System.out.println(e.getMessage());
            printException(e, conf);

        }
    }

    public static void printException(Exception e, ApplicationConfiguration conf) {
        if (conf.isVerbose()) {
            e.printStackTrace();
        } else {
            System.out.println("Use verbose command line option for more details, use --verbose");
        }
    }

    private static String getFileContents(String fileName) throws Exception {
        byte[] content = Files.readAllBytes(Paths.get(fileName));
        return new String(content);
    }

    public static String getExecutionStatus(ExecutionStatus executionStatus) {
        switch (executionStatus) {
            case FAILED:
                return "Failed with error";

            case MANUAL_ROLLBACK:
                return "Executed and rolled back successfully.";

            case SUCCESSFUL:
                return "Success!";

            case SUCCESSFUL_DRY_RUN:
                return "Dry run completed successfully";

            default:
                throw new RuntimeException("Execution status not defined: " + executionStatus.toString());
        }
    }
}
