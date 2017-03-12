package org.jump.boot;

import java.nio.file.Files;
import java.nio.file.Paths;

import org.jump.entity.ApplicationConfiguration;
import org.jump.parser.JumpGen;
import org.jump.parser.ParseResult;
import org.jump.service.Executor;
import org.jump.validation.CloValidator;

public class Main {

    public static void main(String[] args) {

        boolean verbose = false;

        try {

            CloValidator validator = new CloValidator();
            ApplicationConfiguration conf = validator.validate(args);
            verbose = conf.isLogSql() || conf.isVerbose();

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
        } catch (Exception e) {

            System.out.println(e.getMessage());

            if (verbose) {
                e.printStackTrace();
            } else {
                System.out.println("User verbose command line option for more details, use --help");
            }
        }
    }

    private static String getFileContents(String fileName) throws Exception {
        byte[] content = Files.readAllBytes(Paths.get(fileName));
        return new String(content);
    }
}
