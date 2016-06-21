package com.pmoradi.test.util;

import com.pmoradi.rest.entries.AddInEntry;
import com.pmoradi.rest.entries.UrlEditEntry;
import com.pmoradi.rest.entries.GroupView;

import java.util.Random;

public class Randomization {

    private static final char[] ALPHABET = {'a','b','c','d','e','f','g','h','i','j','k','l','m','n','o','p','q','r','s','t','u','v','w','x','y','z'};
    private static final Random RANDOM = new Random();

    public static String randomString() {
        return randomString(10);
    }

    public static String randomString(int length) {
        StringBuilder bu = new StringBuilder(length);
        for(int i = 0; i < length; i++){
            bu.append(ALPHABET[RANDOM.nextInt(ALPHABET.length)]);
        }

        return bu.toString();
    }

    public static String randomLink() {
        return "http://www." + randomString() + ".com";
    }

    public static AddInEntry randomDataEntry() {
        AddInEntry entry = new AddInEntry();
        entry.setGroupName(randomString());
        entry.setUrlName(randomString());
        entry.setLink(randomLink());
        entry.setPassword(randomString());

        return entry;
    }

    public static UrlEditEntry randomUrlEditEntry() {
        UrlEditEntry entry = new UrlEditEntry();
        entry.setGroupName(randomString());
        entry.setUrlName(randomString());
        entry.setPassword(randomString());

        return entry;
    }

    public static GroupView randomViewEntry() {
        GroupView entry = new GroupView();
        entry.setGroupName(randomString());
        entry.setPassword(randomString());

        return  entry;
    }

    public static long randomLong() {
        return RANDOM.nextLong();
    }

    public static int randomInt() {
        return RANDOM.nextInt();
    }

    public static int randomInt(int max) {
        return RANDOM.nextInt(max);
    }
}
