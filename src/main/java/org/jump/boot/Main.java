package org.jump.boot;

import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Paths;
import java.sql.SQLException;

import org.jump.entity.ApplicationConfiguration;
import org.jump.parser.JumpGen;
import org.jump.parser.ParseResult;
import org.jump.service.Executor;
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

            new Executor().execute(conf, result.getCommands());
        } catch (NoSuchFileException nsfe) {

            System.out.println("File not accessible: " + conf.getFileName());
            printException(nsfe, conf);
        } catch (SQLException see) {

            String message = String.format(
                "Error with connection database/executing query: message: [%s] error-code: [%d] sql-state: [%s], Check whether sql-server host is reachable/correct, or error with sql.",
                see.getMessage(),
                see.getErrorCode(),
                see.getSQLState());
            System.out.println(message);
            printException(see, conf);
        } catch (Exception e) {

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
}
