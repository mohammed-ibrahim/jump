package org.jump.datagen;

import java.util.Random;

import org.jump.parser.FieldConfig;
import org.jump.util.Utility;

public class RandomRangeGenerator implements IField {

    private Integer min;

    private Integer max;

    private Random randomizer = new Random();

    public RandomRangeGenerator(FieldConfig fieldConfig) {

        if (fieldConfig.getParams().size() != 2) {
            throw new RuntimeException("random_between takes 2 arguments.");
        }

        if (!Utility.isNumeric(fieldConfig.getParams().get(0))
            || !Utility.isNumeric(fieldConfig.getParams().get(1))) {

            throw new RuntimeException("Argument for function random_between have to be integers");
        }

        Double p1 = Double.parseDouble(fieldConfig.getParams().get(0));
        Double p2 = Double.parseDouble(fieldConfig.getParams().get(1));

        this.min = new Double(Math.min(p1.doubleValue(), p2.doubleValue())).intValue();
        this.max = new Double(Math.max(p1.doubleValue(), p2.doubleValue())).intValue();

        if (this.min.equals(this.max)) {
            throw new RuntimeException("min and max value for random_between cannot be same.");
        }
    }

    @Override
    public String getNext() {
        Integer result = randomizer.nextInt(this.max - this.min) + this.min;
        return Utility.wrapAndEscape(String.valueOf(result.intValue()));
    }

}
