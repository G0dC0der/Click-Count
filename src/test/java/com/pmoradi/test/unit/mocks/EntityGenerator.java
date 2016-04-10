package com.pmoradi.test.unit.mocks;

import com.pmoradi.entities.URL;
import com.pmoradi.test.util.Randomization;

import java.sql.Timestamp;

public class EntityGenerator {

    public static URL generateURL() {
        URL url = new URL();
        url.setLink(Randomization.randomLink());
        url.setAddDate(new Timestamp(System.currentTimeMillis()));
        url.setClicks(Randomization.randomLong());
        url.setId(Randomization.randomInt());
        url.setUrl(Randomization.randomLink());

        return url;
    }
}
