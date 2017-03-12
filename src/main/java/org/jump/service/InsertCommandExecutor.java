package org.jump.service;

import java.util.List;

import org.jump.datagen.IField;
import org.jump.entity.ApplicationConfiguration;
import org.jump.factory.FieldFactory;
import org.jump.parser.InsertCommand;

public class InsertCommandExecutor {

    private InsertCommand insertCommand;

    private ApplicationConfiguration appConfig;

    private TransactionalSqlExecutor sqlExecutor;

    public InsertCommandExecutor(ApplicationConfiguration appConfig, InsertCommand insertCommand, TransactionalSqlExecutor sqlExecutor) {
        this.insertCommand = insertCommand;
        this.appConfig = appConfig;
        this.sqlExecutor = sqlExecutor;
    }

    public void execute() throws Exception {
        List<IField> fields = new FieldFactory().build(this.appConfig, insertCommand);

        new ImportHandler(sqlExecutor, appConfig).importRows(appConfig, insertCommand, fields);
    }

}
