package org.jump.parser;

import java.util.*;
import org.jump.parser.grammer.*;

public class SqlCommand implements Command {
    private ArrayList<String> sqls = new ArrayList<String>();

    public ArrayList<String> getSqls() {
        return this.sqls;
    }

    public void setSqls(ArrayList<String> sqls) {
        this.sqls = sqls;
    }

    public String toString() {
        return this.sqls.toString();
    }
}
