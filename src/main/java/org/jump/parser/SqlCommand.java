package org.jump.parser;

import java.util.*;

import lombok.Getter;
import lombok.Setter;

public class SqlCommand extends AbstractCommand {

    public SqlCommand() {
        this.type = CommandType.SQL_COMMAND;
    }

    @Getter
    @Setter
    private ArrayList<String> sqls = new ArrayList<String>();

    public SqlCommand getSqlCommand() {

        return this;
    }
}
