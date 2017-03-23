package org.jump.parser;

import java.util.*;
import org.jump.parser.grammer.*;

public class ParseResult {
    private List<Object> commands;

    private String errorMessage;

    private String stackTrace;

    //Constructor
    public ParseResult(List<Object> commands, String errorMessage, String stackTrace) {
        this.commands = commands;
        this.errorMessage = errorMessage;
        this.stackTrace = stackTrace;
    }

    //Getters
    public List<Object> getCommands() {
        return this.commands;
    }

    public String getErrorMessage() {
        return this.errorMessage;
    }

    public String getStackTrace() {
        return this.stackTrace;
    }

    //Setters
    public void setCommands(List<Object> commands) {
        this.commands = commands;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public void setStackTrace(String stackTrace) {
        this.stackTrace = stackTrace;
    }
}
