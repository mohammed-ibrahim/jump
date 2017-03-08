package org.jump.service;

import java.util.List;

import org.jump.datagen.IField;
import org.jump.entity.ApplicationConfiguration;
import org.jump.factory.FieldFactory;
import org.jump.parser.InsertCommand;

public class InsertCommandExecutor {

    private InsertCommand insertCommand;

    private ApplicationConfiguration appConfig;

    public InsertCommandExecutor(ApplicationConfiguration appConfig, InsertCommand insertCommand) {
        this.insertCommand = insertCommand;
        this.appConfig = appConfig;
    }

    public void execute() {
        List<IField> fields = new FieldFactory().build(this.appConfig, insertCommand);

        new ImportHandler().importRows(appConfig, insertCommand, fields);
    }

}
