package org.jump.parser;

import org.antlr.v4.runtime.*;
import org.jump.exception.ParseFailureException;

public class DescriptiveErrorListener extends BaseErrorListener {
    public static DescriptiveErrorListener INSTANCE = new DescriptiveErrorListener();

    @Override
    public void syntaxError(Recognizer<?, ?> recognizer, Object offendingSymbol,
                            int line, int charPositionInLine,
                            String msg, RecognitionException e)
    {

        String sourceName = recognizer.getInputStream().getSourceName();

        if (!sourceName.isEmpty()) {
            sourceName = msg;
        }

        String offendingToken = "N/A";
        if (e != null && e.getOffendingToken() != null && e.getOffendingToken().getText() != null) {
            offendingToken = e.getOffendingToken().getText();
        }

        throw new ParseFailureException(sourceName, line, charPositionInLine, offendingToken);
    }
}
