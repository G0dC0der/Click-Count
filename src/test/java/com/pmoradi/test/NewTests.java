package com.pmoradi.test;

import com.pmoradi.entities.Namespace;
import com.pmoradi.entities.URL;
import com.pmoradi.entities.dao.NamespaceDao;
import com.pmoradi.entities.dao.URLDao;
import com.pmoradi.system.SessionFactory;
import com.pmoradi.test.util.Randomization;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class NewTests {

    private static EntityManagerFactory entityManagerFactory;
    private SessionFactory sessionFactory;

    @BeforeClass
    public static void setup() {
        entityManagerFactory = Persistence.createEntityManagerFactory("hibernate-engine");
    }

    @AfterClass
    public static void clean() {
        entityManagerFactory.close();
    }

    @Before
    public void prepareTest() {
        sessionFactory = entityManagerFactory::createEntityManager;
    }

    @Test
    public void namespaceTest() {
        NamespaceDao dao = new NamespaceDao(sessionFactory);
        URLDao urlDao = new URLDao(sessionFactory);

        Namespace namespace = new Namespace();
        namespace.setName(Randomization.randomString());
        namespace.setPassword(Randomization.randomString());

        dao.save(namespace);

        URL url = new URL();
        url.setNamespace(namespace);
        url.setLink("google.se");
        url.setAdded(System.currentTimeMillis());
        url.setAlias(Randomization.randomString());

        urlDao.save(url);

        System.out.println(namespace.getName());
        System.out.println(url.getAlias());

//        URL url2 = new URL();
//        url2.setNamespace(namespace);
//        url2.setLink("google.se");
//        url2.setAdded(System.currentTimeMillis());
//        url2.setAlias(Randomization.randomString());
//
//        Set<URL> urls = new HashSet<>();
//        urls.add(url);
//        urls.add(url2);
//
//        namespace.setUrls(urls);

//        Namespace sp2 = dao.findByName(namespace.getName());
//        System.out.println(sp2.getUrls().size());
    }
}
