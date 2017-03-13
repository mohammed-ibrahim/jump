package org.jump.validation;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.GnuParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.UnrecognizedOptionException;
import org.jump.entity.ApplicationConfiguration;
import org.jump.util.Utility;

public class CloValidator {

    private static String FILE_NAME = "file";

    private static String DATA_BASE = "database";

    private static String USER = "username";

    private static String PWD = "password";

    private static String LOG_SQL = "logsql";

    private static String HELP = "help";

    private static String DRY_RUN = "dry-run";

    private static String VERBOSE = "verbose";

    private static String HOST = "host";

    public ApplicationConfiguration validate(String[] args) {
        return unsafeValidate(args);
    }

    private ApplicationConfiguration unsafeValidate(String[] args) {
        Options options = new Options();

        options.addOption(
                new Option("d", DATA_BASE, true, "Name of the database where to which the changes are to be made."));
        options.addOption(new Option("u", USER, true, "Database username."));
        options.addOption(new Option("p", PWD, true, "Password for the usename."));
        options.addOption(new Option("f", FILE_NAME, true, "Input jump script file path."));
        options.addOption(new Option("l", LOG_SQL, false, "Log the sql statements that are executed."));
        options.addOption(new Option("v", VERBOSE, false, "Verbose."));
        options.addOption(new Option("h", HELP, false, "Help page."));
        options.addOption(new Option("r", DRY_RUN, false,
                "Dry run, rollback the change after the completion of import, Note: Rollback only works for import changes and does not rollback schema changes."));
        options.addOption(new Option("t", HOST, true, "Sql server host."));

        GnuParser parser = new GnuParser();
        ApplicationConfiguration conf = new ApplicationConfiguration();
        CommandLine cli = null;

        try {
            cli = parser.parse(options, args);
        } catch (UnrecognizedOptionException uoe) {
            conf.setSuccess(false);
            System.out.println("Invalid option");
            displayHelp(options);

            return conf;
        } catch (ParseException pe) {
            conf.setSuccess(false);
            System.out.println("Invalid option");
            displayHelp(options);

            return conf;
        }

        if (cli.hasOption(HELP)) {
            displayHelp(options);
        }

        conf.setSuccess(false);

        if (!cli.hasOption(FILE_NAME)) {

            conf.setFileName(Utility.readFromConsole("Enter file name"));
        } else {

            conf.setFileName(cli.getOptionValue(FILE_NAME));
        }

        if (!cli.hasOption(DATA_BASE)) {

            conf.setDatabase(Utility.readFromConsole("Enter database name"));
        } else {

            conf.setDatabase(cli.getOptionValue(DATA_BASE));
        }

        if (!cli.hasOption(USER)) {
            conf.setUser(Utility.readFromConsole("Enter username"));
        } else {

            conf.setUser(cli.getOptionValue(USER));
        }

        if (cli.hasOption(PWD)) {
            String password = cli.getOptionValue(PWD);
            conf.setPassword(password);
        }

        if (cli.hasOption(LOG_SQL)) {
            conf.setLogSql(true);
        }

        if (cli.hasOption(VERBOSE)) {
            conf.setVerbose(true);
        }

        if (cli.hasOption(DRY_RUN)) {
            conf.setDryRun(true);
        } else {
            conf.setDryRun(false);
        }

        if (cli.hasOption(HOST)) {
            conf.setHost(cli.getOptionValue(HOST));
        }

        conf.setSuccess(true);

        return conf;
    }

    private void displayHelp(Options options) {
        HelpFormatter formater = new HelpFormatter();

        formater.printHelp(
                "java -jar jump.jar --file <file_path> --database <database_name> --username <user_name> --password <password>",
                options);
    }

}
