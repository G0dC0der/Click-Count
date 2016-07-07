package com.pmoradi.test.unit;

import com.pmoradi.entities.dao.ClientDao;
import com.pmoradi.entities.dao.CollaboratorDao;
import com.pmoradi.entities.dao.NamespaceDao;
import com.pmoradi.entities.dao.URLDao;
import com.pmoradi.system.SessionProvider;
import org.hibernate.SessionFactory;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;

import javax.persistence.Persistence;

public abstract class AbstractDaoTest {

    private static SessionFactory sessionFactory;
    private static SessionProvider sessionProvider;
    protected static NamespaceDao namespaceDao;
    protected static URLDao urlDao;
    protected static CollaboratorDao collaboratorDao;
    protected static ClientDao clientDao;

    @BeforeClass
    public static void setup() {
        sessionFactory = (SessionFactory) Persistence.createEntityManagerFactory("hibernate-engine");
        sessionProvider = sessionFactory::openSession;

        namespaceDao = new NamespaceDao(sessionProvider);
        urlDao = new URLDao(sessionProvider);
        collaboratorDao = new CollaboratorDao(sessionProvider);
        clientDao = new ClientDao(sessionProvider);
    }

    @AfterClass
    public static void clean() {
        sessionFactory.close();
    }
}
