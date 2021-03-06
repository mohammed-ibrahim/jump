package org.jump.datagen;

import java.util.List;

import org.jump.parser.FieldConfig;
import org.jump.util.Utility;

public class SerialListItemPicker implements IField {

    private List<String> items = null;

    private int index;

    public SerialListItemPicker(FieldConfig fieldConfig) {
        if (fieldConfig.getParams().size() < 1) {
            throw new RuntimeException("Serial function needs atleast 1 parameter");
        }

        this.items = fieldConfig.getParams();
        this.index = 0;
    }

    @Override
    public String getNext() {
        if (index >= this.items.size()) {
            index = 0;
        }

        String value = this.items.get(this.index++);

        return Utility.wrapAndEscape(value);
    }

}
