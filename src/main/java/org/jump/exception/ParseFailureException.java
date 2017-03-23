package org.jump.exception;

import lombok.Getter;
import lombok.Setter;

public class ParseFailureException extends RuntimeException {

    /**
     *
     */
    private static final long serialVersionUID = -8469823104129978765L;

    public ParseFailureException(String sourceName, int line, int charPositionInLine, String offendingToken) {
        this.sourceName = sourceName;
        this.line = line;
        this.charPositionInLine = charPositionInLine;
        this.offendingToken = offendingToken;
    }

    @Getter
    @Setter
    private String sourceName;

    @Getter
    @Setter
    private int line;

    @Getter
    @Setter
    private int charPositionInLine;

    @Getter
    @Setter
    private String offendingToken;
}
