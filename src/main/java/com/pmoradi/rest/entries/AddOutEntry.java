package com.pmoradi.rest.entries;

public class AddOutEntry {

    private String aliasError, sourceUrlError, groupError, passwordError;

    public String getAliasError() {
        return aliasError;
    }

    public void setAliasError(String aliasError) {
        this.aliasError = aliasError;
    }

    public String getSourceUrlError() {
        return sourceUrlError;
    }

    public void setSourceUrlError(String sourceUrlError) {
        this.sourceUrlError = sourceUrlError;
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
        return aliasError != null || sourceUrlError != null || passwordError != null || groupError != null;
    }
}
