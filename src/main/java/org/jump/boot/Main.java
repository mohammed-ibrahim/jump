package org.jump.boot;

import org.jump.entity.ApplicationConfiguration;
import org.jump.service.Executor;
import org.jump.validation.CloValidator;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Main {

    public static void main(String[] args) {
        System.out.println("Hello World.");

        try {
            //ParseResult result = JumpGen.parse("");

            CloValidator validator = new CloValidator();
            ApplicationConfiguration conf = validator.validate(args);

            if (!conf.isSuccess()) {
                log.info("Failed because of insufficient cli options");
            }

            //new Executor().execute(appConfig, commands);;
        } catch (Exception e) {

            e.printStackTrace();
        }
    }
}
