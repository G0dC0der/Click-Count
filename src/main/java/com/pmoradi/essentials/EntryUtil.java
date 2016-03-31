package com.pmoradi.essentials;

import com.pmoradi.rest.entries.*;

public class EntryUtil {

    public static void shrink(AddInEntry entry) {
        entry.setLink(requireNonNull(entry.getLink()).trim().toLowerCase());
        entry.setGroupName(requireNonNull(entry.getGroupName()).trim().toLowerCase());
        entry.setUrlName(requireNonNull(entry.getUrlName()).trim().toLowerCase());
        entry.setPassword(requireNonNull(entry.getPassword()).trim().toLowerCase());
    }

    public static void shrink(UrlEditEntry entry) {
        entry.setGroupName(requireNonNull(entry.getGroupName()).trim().toLowerCase());
        entry.setUrlName(requireNonNull(entry.getUrlName()).trim().toLowerCase());
        entry.setPassword(requireNonNull(entry.getPassword()).trim().toLowerCase());
    }

    public static void shrink(ViewEntry entry) {
        entry.setGroupName(requireNonNull(entry.getGroupName()).trim().toLowerCase());
        entry.setPassword(requireNonNull(entry.getPassword()).trim().toLowerCase());
    }

    public static String requireNonNull(String str) {
        return str == null ? "" : str;
    }
}
