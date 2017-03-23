package org.jump.parser;

import lombok.Getter;
import lombok.Setter;

public class VariableCommand extends AbstractCommand {

    @Getter
    @Setter
    private String name;

    @Getter
    @Setter
    private String value;

    public VariableCommand(String name, String value) {
        this.name = name;
        this.value = value;
        this.type = CommandType.BASIC_VARIABLE;
    }

    @Override
    public VariableCommand getVariableCommand() {

        return this;
    }
}
