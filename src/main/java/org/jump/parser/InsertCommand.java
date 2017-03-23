package org.jump.parser;

import java.util.*;

import lombok.Getter;
import lombok.Setter;

public class InsertCommand extends AbstractCommand {

    public InsertCommand () {

        this.type = CommandType.INSERT_COMMAND;
    }

    @Getter
    @Setter
    private String tableName;

    @Getter
    @Setter
    private Integer numRows;

    @Getter
    @Setter
    private String storageIdentifier;

    @Getter
    @Setter
    private ArrayList<FieldConfig> fieldConfigs = new ArrayList<FieldConfig>();

    @Override
    public InsertCommand getInsertCommand() {

        return this;
    }
}
