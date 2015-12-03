package com.pmoradi.rest.entries;

public class UrlEntry {

    private String url;
    private String link;
    private long[] clicks;

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

    public long[] getClicks() {
        return clicks;
    }

    public void setClicks(long[] clicks) {
        this.clicks = clicks;
    }
}
