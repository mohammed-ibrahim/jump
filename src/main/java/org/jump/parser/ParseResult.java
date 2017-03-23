package org.jump.parser;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

public class ParseResult {

    @Getter
    @Setter
    private List<AbstractCommand> commands;

    //Constructor
    public ParseResult(List<AbstractCommand> commands) {
        this.commands = commands;
    }
}
