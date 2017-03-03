package org.jump.datagen;

import org.jump.parser.FieldConfig;
import org.jump.util.Utility;

public class BetweenFieldGenerator implements IField {

    private Integer start;

    private Integer end;

    private Integer current;

    public BetweenFieldGenerator(FieldConfig fieldConfig) {

        if (fieldConfig.getParams().size() != 2) {
            throw new RuntimeException("between function takes two arguments.");
        }

        String param1 = fieldConfig.getParams().get(0);
        String param2 = fieldConfig.getParams().get(1);
        if (!Utility.isNumeric(param1) || !Utility.isNumeric(param2)) {
            throw new RuntimeException("Parameters for between function should be numeric fields");
        }

        Double d1 = Double.parseDouble(param1);
        Double d2 = Double.parseDouble(param2);

        if (d1.equals(d2)) {
            throw new RuntimeException("First and Second parameters cannot be same for between function");
        }

        start = new Integer(Math.min(d1.intValue(), d2.intValue()));
        end = new Integer(Math.max(d1.intValue(), d2.intValue()));
        current = start;
    }

    @Override
    public String getNext() {

        String str = String.valueOf(current);
        this.current = this.current + 1;

        if (this.current > this.end) {
            this.current = this.start;
        }

        return str;
    }
}
