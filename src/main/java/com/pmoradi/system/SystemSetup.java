package com.pmoradi.system;

import org.hibernate.Session;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

@WebListener
public class SystemSetup implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
//        Repository.getDatabase();
        Repository.getEntityManagerFactory();
    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {
//        Repository.getDatabase().close();
        Repository.getEntityManagerFactory().close();
    }
}
