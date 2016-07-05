package com.pmoradi.test;

import com.pmoradi.entities.Client;
import com.pmoradi.entities.Namespace;
import com.pmoradi.entities.URL;
import com.pmoradi.entities.dao.ClientDao;
import com.pmoradi.entities.dao.NamespaceDao;
import com.pmoradi.entities.dao.URLDao;
import com.pmoradi.essentials.Loop;
import com.pmoradi.system.SessionProvider;
import com.pmoradi.test.util.Randomization;
import org.hibernate.SessionFactory;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class NewTests {

    private static SessionFactory sessionFactory;
    private SessionProvider sessionProvider;

    @BeforeClass
    public static void setup() {
        sessionFactory = (SessionFactory) Persistence.createEntityManagerFactory("hibernate-engine");
    }

    @AfterClass
    public static void clean() {
        sessionFactory.close();
    }

    @Before
    public void prepareTest() {
        sessionProvider = sessionFactory::openSession;
    }

    @Test
    public void namespaceTest() throws InterruptedException {
        NamespaceDao nameDao = new NamespaceDao(sessionProvider);
        URLDao urlDao = new URLDao(sessionProvider);
        ClientDao clientDao = new ClientDao(sessionProvider);

        Namespace namespace = new Namespace();
        namespace.setName(Randomization.randomString() + "NAME");
        namespace.setPassword(Randomization.randomString() + "PASSWORD");

        nameDao.save(namespace);

        URL url = new URL();
        url.setNamespace(namespace);
        url.setLink("google.se");
        url.setAdded(System.currentTimeMillis());
        url.setAlias(Randomization.randomString());

        urlDao.save(url);

        URL found = urlDao.findById(namespace.getName(), url.getAlias());
        found = urlDao.findById(url.getNamespace().getName(), url.getAlias());

        Client client = new Client();
        client.setIdentifier(Randomization.randomString());
        client.setAlias(Randomization.randomString());
        client.setNamespace(Randomization.randomString());
        client.setExpire(System.currentTimeMillis() + 10000);

        clientDao.save(client);

        while (true) {
            Client client1 = clientDao.findByIdentifier(client.getIdentifier());
            if(client1 == null)
                break;
            clientDao.deleteAllExpired();

            Thread.sleep(100);
        }
    }
}
