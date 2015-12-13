package com.pmoradi.rest.entries;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;

@XmlAccessorType(XmlAccessType.FIELD)
public class LinkEntry {

    private String link;
    private String groupName;
    private String urlName;

    public LinkEntry(){}

    public LinkEntry(String groupName, String urlName, String link) {
        this.link = link;
        this.groupName = groupName;
        this.urlName = urlName;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getUrlName() {
        return urlName;
    }

    public void setUrlName(String urlName) {
        this.urlName = urlName;
    }
}
