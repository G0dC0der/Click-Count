package com.pmoradi.essentials;

import com.pmoradi.rest.entries.*;

public class EntryUtil {

    public static DataOutEntry merge(DataEntry entry1, ErrorEntry entry2) {
        DataOutEntry out = new DataOutEntry();
        out.setLink(entry1.getLink());
        out.setLinkError(entry2.getLinkError());
        out.setPassword(entry1.getPassword());
        out.setPasswordError(entry2.getPasswordError());
        out.setCaptcha(entry1.getCaptcha());
        out.setCaptchaError(entry2.getCaptchaError());
        out.setUrlName(entry1.getUrlName());
        out.setUrlError(entry2.getUrlError());
        out.setGroupName(entry1.getGroupName());
        out.setGroupError(entry2.getGroupError());

        return out;
    }

    public static void shrink(DataEntry entry) {
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
