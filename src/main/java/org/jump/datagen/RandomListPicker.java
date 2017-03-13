package org.jump.datagen;

import java.util.List;
import java.util.Random;

import org.jump.parser.FieldConfig;
import org.jump.util.Utility;

public class RandomListPicker implements IField {

    private List<String> items = null;

    private Random randomizer = new Random();

    public RandomListPicker(FieldConfig fieldConfig) {
        if (fieldConfig.getParams().size() < 1) {
            throw new RuntimeException("one_of function needs atleast 1 parameter");
        }

        this.items = fieldConfig.getParams();
    }

    @Override
    public String getNext() {
        return Utility.wrapAndEscape(items.get(randomizer.nextInt(items.size())));
    }

}
