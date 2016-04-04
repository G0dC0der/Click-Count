package com.pmoradi.rest.entries;

public class AddOutEntry {

    private String urlError, linkError, groupError, passwordError;

    public String getUrlError() {
        return urlError;
    }

    public void setUrlError(String urlError) {
        this.urlError = urlError;
    }

    public String getLinkError() {
        return linkError;
    }

    public void setLinkError(String linkError) {
        this.linkError = linkError;
    }

    public String getGroupError() {
        return groupError;
    }

    public void setGroupError(String groupError) {
        this.groupError = groupError;
    }

    public String getPasswordError() {
        return passwordError;
    }

    public void setPasswordError(String passwordError) {
        this.passwordError = passwordError;
    }

    public boolean containErrors() {
        return urlError != null || linkError != null || passwordError != null;
    }
}
