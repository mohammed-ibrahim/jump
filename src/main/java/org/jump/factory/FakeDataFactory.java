package org.jump.factory;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;

import org.apache.commons.lang3.StringUtils;

import com.github.mirreck.FakeFactory;

public class FakeDataFactory {
    private enum FakeFieldType {
        NAME,
        NAME_TITLE,
        INT,
        LONG,
        FLOAT,
        DOUBLE,
        FIRST_NAME,
        LAST_NAME,
        MONTH,
        YEAR,
        DATE,
        CURRENT_DATE,
        TIME_STAMP,
        ZIPCODE,
        COUNTRY,
        CITY,
        ADDRESS,
        STREET_ADDRESS,
        LATITUDE,
        LONGITUDE,
        COLOR,
        HEIGHT,
        PHONE_NUMBER,
        EMAIL,
        GENDER,
        TRUE_FALSE,
        BOOLEAN,
        SLUG,
        WEBSITE,
        URL,
        TEN_DIG_PHONE_NUM,
        SENTENCE,
        PARAGRAPH;
    }

    private static FakeFactory factory = new FakeFactory();

    private static Random randomizer = new Random();

    private static List<String> months = Arrays.asList("January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December");

    private static List<String> genders = Arrays.asList("M", "F");

    private static List<String> trueFalse = Arrays.asList("true", "false");

    private static List<String> boolValues = Arrays.asList("1", "0");

    public static String getData(String name) {
        FakeFieldType fakeFieldType = null;
        try {

            fakeFieldType = FakeFieldType.valueOf(name.toUpperCase());
        } catch (Exception e) {

            List<String> types = new ArrayList<String>();
            for (FakeFieldType type: FakeFieldType.values()) {
                types.add(type.toString());
            }

            @SuppressWarnings("unchecked")
            String message = String.format("Unknown fake method: [%s], Only supported values are: %s", name.toUpperCase(), StringUtils.join(types));

            throw new RuntimeException(message);
        }

        switch (fakeFieldType) {
            case NAME:
                return factory.name();

            case NAME_TITLE:
                return factory.nameTitle();

            case INT:
                return factory.digits(random(2,8));

            case LONG:
                return factory.digits(random(4,11));

            case FLOAT:
                return factory.digits(random(1, 3)) + "." + factory.digits(random(1, 2));

            case DOUBLE:
                return factory.digits(random(3, 5)) + "." + factory.digits(random(1, 2));

            case FIRST_NAME:
                return factory.firstName();

            case LAST_NAME:
                return factory.lastName();

            case MONTH:
                return months.get(randomizer.nextInt(months.size()));

            case SENTENCE:
                return factory.sentence();

            case PARAGRAPH:
                return factory.paragraph();

            case YEAR:
                return String.valueOf(random(1990, 2050));

            case DATE:
                return getRandomDate();

            case TIME_STAMP:
                return getRandomDate();

            case CURRENT_DATE:
                return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S").format(new Date());

            case ZIPCODE:
                return factory.zipCode();

            case COUNTRY:
                return factory.country();

            case CITY:
                return factory.city();

            case ADDRESS:
                return StringUtils.join(factory.fullAddress(), " ");

            case STREET_ADDRESS:
                return factory.streetAddress();

            case LATITUDE:
                return String.valueOf(factory.coordinatesLatLng()[0]);

            case LONGITUDE:
                return String.valueOf(factory.coordinatesLatLng()[1]);

            case COLOR:
                return factory.eyeColor();

            case HEIGHT:
                return String.valueOf(factory.height());

            case PHONE_NUMBER:
                return factory.phoneNumber();

            case EMAIL:
                return factory.email();

            case GENDER:
                return genders.get(randomizer.nextInt(genders.size()));

            case TRUE_FALSE:
                return trueFalse.get(randomizer.nextInt(trueFalse.size()));

            case BOOLEAN:
                return boolValues.get(randomizer.nextInt(boolValues.size()));

            case SLUG:
                return StringUtils.join(factory.words(random(2,5)), "").toLowerCase();

            case URL:
            case WEBSITE:
                return "www." + factory.letters(random(10,15)).toLowerCase() + ".com";

            case TEN_DIG_PHONE_NUM:
                return String.valueOf(8943767676L + random(1, 1000000));

            default:
                String message = String.format("Fake method: [%s] is not implemented yet. Please contact support.");

                throw new RuntimeException(message);
        }

    }

    private static String getRandomDate() {
        int factor = -1;

        if ((random(0,9) %2) == 0) {
            factor = 1;
        }

        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, factor * random(10, 20000));
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S").format(cal.getTime());
    }

    private static int random(int low, int high) {
        return randomizer.nextInt(high - low) + low;
    }
}
