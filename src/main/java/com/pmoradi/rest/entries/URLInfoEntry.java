package com.pmoradi.rest.entries;

public class URLInfoEntry {

    private String url;
    private String link;
    private String group;
    private Long clicks;

    public URLInfoEntry() {}

    public URLInfoEntry(String url, String link, String group, Long clicks) {
        this.url = url;
        this.link = link;
        this.group = group;
        this.clicks = clicks;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public Long getClicks() {
        return clicks;
    }

    public void setClicks(Long clicks) {
        this.clicks = clicks;
    }
}
