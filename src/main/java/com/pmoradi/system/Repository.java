package com.pmoradi.system;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class Repository {

    private static EntityManagerFactory entityManagerFactory;

    public static EntityManagerFactory getEntityManagerFactory(){
        if(entityManagerFactory == null)
            entityManagerFactory = Persistence.createEntityManagerFactory("hibernate-engine");

        return entityManagerFactory;
    }
}
