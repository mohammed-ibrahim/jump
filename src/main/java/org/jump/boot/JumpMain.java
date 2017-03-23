package org.jump.boot;

import org.jump.entity.ApplicationConfiguration;
import org.jump.entity.ExecutionStatus;
import org.jump.parser.JumpGen;
import org.jump.parser.ParseResult;
import org.jump.service.CacheManager;
import org.jump.service.OperationStatusReporter;
import org.jump.service.Executor;
import org.jump.service.FileLogger;
import org.jump.util.Utility;
import org.jump.validation.ArgumentValidator;

public class JumpMain {

    public static void main(String[] args) {

        ArgumentValidator validator = new ArgumentValidator();
        ApplicationConfiguration applicationConfiguration = validator.validate(args);
        OperationStatusReporter operationStatusReporter = new OperationStatusReporter(applicationConfiguration);

        try {

            //1. Read the file
            String fileContents = Utility.getFileContents(applicationConfiguration.getFileName());

            //2. Parse the file
            JumpGen parser = new JumpGen();
            ParseResult result = parser.parse(fileContents);

            // 3. Execute the commands
            ExecutionStatus executionStatus = new Executor().execute(applicationConfiguration, result.getCommands());
            System.out.println(Utility.getExecutionStatus(executionStatus));

            // 4. Write the cached keys
            if (CacheManager.getInstance().allkeys().size() > 0) {
                for (String cacheKey: CacheManager.getInstance().allkeys()) {
                    FileLogger.getInstance().writeRow(cacheKey + ": " + CacheManager.getInstance().itemsForKey(cacheKey));
                }
            }

            // 5. Close the file logger
            FileLogger.getInstance().close();

        } catch (Exception e) {

            operationStatusReporter.reportError(e);
        }
    }
}
