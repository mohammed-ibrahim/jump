package org.jump.datagen;

import java.util.Random;

import org.jump.entity.DataType;
import org.jump.parser.FieldConfig;
import org.jump.util.Utility;

public class RandomRangeGenerator implements IField {

    private Double min;

    private Double max;

    private Random randomizer = new Random();

    private DataType dataType;

    public RandomRangeGenerator(FieldConfig fieldConfig) {

        if (fieldConfig.getParams().size() != 2) {
            throw new RuntimeException("random_between takes 2 arguments.");
        }

        if (!Utility.isNumeric(fieldConfig.getParams().get(0)) || !Utility.isNumeric(fieldConfig.getParams().get(1))) {

            throw new RuntimeException("Argument for function random_between have to be either integer or double");
        }

        Double p1 = Double.parseDouble(fieldConfig.getParams().get(0));
        Double p2 = Double.parseDouble(fieldConfig.getParams().get(1));

        this.min = new Double(Math.min(p1.doubleValue(), p2.doubleValue()));
        this.max = new Double(Math.max(p1.doubleValue(), p2.doubleValue()));

        if (this.min.equals(this.max)) {
            throw new RuntimeException("min and max value for random_between cannot be same.");
        }

        if (fieldConfig.getParams().get(0).contains(".") || fieldConfig.getParams().get(1).contains(".")) {
            this.dataType = DataType.DOUBLE;
        } else {
            this.dataType = DataType.INTEGER;
        }
    }

    @Override
    public String getNext() {
        String result = null;
        switch (this.dataType) {
            case INTEGER:
                Integer intValue = randomizer.nextInt(this.max.intValue() - this.min.intValue()) + this.min.intValue();
                result = String.valueOf(intValue);
                break;

            case DOUBLE:
                Double doubleValue = randomInRange(this.min, this.max);
                result = String.valueOf(doubleValue);
                break;

            default:
                String message = "Unsupported dataType: " + this.dataType.toString();
                throw new RuntimeException(message);
        }

        return Utility.wrapAndEscape(result);
    }

    private double randomInRange(double min, double max) {
        double range = max - min;
        double scaled = randomizer.nextDouble() * range;
        double shifted = scaled + min;
        return shifted; // == (rand.nextDouble() * (max-min)) + min;
    }
}
