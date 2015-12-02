package com.pmoradi.system;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

@WebListener
public class SystemSetup implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        Repository.getDatabase();
        Repository.getLockManager();
        Repository.defaultGroup();
    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {
        Repository.getDatabase().close();
    }
}
