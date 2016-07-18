package com.pmoradi.test.util;

import com.pmoradi.rest.entries.AddInEntry;
import com.pmoradi.rest.entries.GroupEntry;

public class Equality {

    public static boolean equals(GroupEntry groupEntry, AddInEntry entry) {
        return  groupEntry.getGroupName().equals(entry.getGroupName()) &&
                groupEntry.getUrls()[0].getAlias().equals(entry.getAlias()) &&
                groupEntry.getUrls()[0].getSourceUrl().equals(entry.getSourceUrl());
    }
}
