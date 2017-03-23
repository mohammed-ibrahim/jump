package org.jump.parser;

import lombok.Getter;
import lombok.Setter;

public class AbstractCommand {

    @Getter
    @Setter
    protected CommandType type;

    public InsertCommand getInsertCommand() {

        throw new RuntimeException("Method not supported");
    }

    public SqlCommand getSqlCommand() {

        throw new RuntimeException("Method not supported");
    }

    public VariableCommand getVariableCommand() {

        throw new RuntimeException("Method not supported");
    }
}
