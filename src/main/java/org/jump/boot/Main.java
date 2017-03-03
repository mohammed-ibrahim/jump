package org.jump.boot;

import java.io.IOException;
import java.io.ObjectInputStream.GetField;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.jump.entity.ApplicationConfiguration;
import org.jump.parser.JumpGen;
import org.jump.parser.ParseResult;
import org.jump.service.Executor;
import org.jump.validation.CloValidator;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Main {

    public static void main(String[] args) {
        System.out.println("Hello World.");

        try {

            CloValidator validator = new CloValidator();
            ApplicationConfiguration conf = validator.validate(args);

            if (!conf.isSuccess()) {
                log.info("Failed because of insufficient cli options");
            }

            String fileContents = getFileContents(conf.getFileName());
            ParseResult result = JumpGen.parse(fileContents);

            if (result.getCommands() == null) {
                log.info("There is an error in script: " + conf.getFileName());
                log.info(result.getErrorMessage());
                System.exit(0);
            }

            new Executor().execute(conf, result.getCommands());
        } catch (Exception e) {

            e.printStackTrace();
        }
    }

    private static String getFileContents(String fileName) throws Exception {
        byte[] content = Files.readAllBytes(Paths.get(fileName));
        return new String(content);
    }
}
