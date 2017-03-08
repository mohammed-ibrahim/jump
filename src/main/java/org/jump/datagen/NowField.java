package org.jump.datagen;

public class NowField implements IField {

    @Override
    public String getNext() {
        return "now()";
    }

}
