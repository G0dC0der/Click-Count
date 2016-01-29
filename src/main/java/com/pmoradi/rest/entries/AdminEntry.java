package com.pmoradi.rest.entries;

public class AdminEntry {

    private String username;
    private String password;

    public AdminEntry(){}

    public AdminEntry(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof AdminEntry) {
            AdminEntry that = (AdminEntry) obj;

            return this.username.equals(that.username) && this.password.equals(that.password);
        }
        return false;
    }
}
