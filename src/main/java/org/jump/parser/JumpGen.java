package org.jump.parser;

import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.BailErrorStrategy;
import java.util.*;

import org.jump.parser.grammer.*;

public class JumpGen {
    @SuppressWarnings("unchecked")
    public static void main(String[] args) {
        String str = "";
        str += "   # hello world \n sql() {\"yes\"} \n";

        str += "    # text";
        if (args.length > 0) {
            str = args[0];
        }

        ANTLRInputStream input = new ANTLRInputStream(str);
        JumpLexer lexer = new JumpLexer(input);

        CommonTokenStream tokens = new CommonTokenStream(lexer);
        JumpParser parser = new JumpParser(tokens);
        parser.setErrorHandler(new BailErrorStrategy());

        ParseTree tree = parser.statement();
        Analyzer builder = new Analyzer();
        ArrayList<Object> res = (ArrayList<Object>) builder.visit(tree);
        System.out.println("Finally obtained string is: " + res.toString());
    }

    @SuppressWarnings("unchecked")
    public ParseResult parse(String queryString) {
        ANTLRInputStream input = new ANTLRInputStream(queryString);
        JumpLexer lexer = new JumpLexer(input);
        lexer.removeErrorListeners();
        lexer.addErrorListener(DescriptiveErrorListener.INSTANCE);

        CommonTokenStream tokens = new CommonTokenStream(lexer);
        JumpParser parser = new JumpParser(tokens);
        parser.removeErrorListeners();
        parser.addErrorListener(DescriptiveErrorListener.INSTANCE);

        ParseTree tree = parser.statement();
        Analyzer builder = new Analyzer();
        ArrayList<AbstractCommand> res = (ArrayList<AbstractCommand>) builder.visit(tree);

        return new ParseResult(res);
    }
}
