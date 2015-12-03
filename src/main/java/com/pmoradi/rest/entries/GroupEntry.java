package com.pmoradi.rest.entries;

public class GroupEntry {

    private String groupName;
    private UrlEntry[] urls;

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public UrlEntry[] getUrls() {
        return urls;
    }

    public void setUrls(UrlEntry[] urls) {
        this.urls = urls;
    }
}
