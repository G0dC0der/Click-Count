package com.pmoradi.system;


public interface ApplicationSettings {

    String getServerIP();

    String getServerDomain();

    boolean isAdmin(String username, String password);
}
