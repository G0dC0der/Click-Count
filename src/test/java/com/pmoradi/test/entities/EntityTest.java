package com.pmoradi.test.entities;

import com.mysql.jdbc.exceptions.jdbc4.MySQLIntegrityConstraintViolationException;
import com.pmoradi.entities.Click;
import com.pmoradi.entities.Group;
import com.pmoradi.entities.URL;
import com.pmoradi.system.Repository;
import org.junit.Assert;
import org.junit.Test;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public class EntityTest {

    @Test
    public void testEntityRelations(){
        final String GROUP = UUID.randomUUID().toString();
        final String PASSWORD = UUID.randomUUID().toString();
        final String URL = UUID.randomUUID().toString();
        final String LINK = UUID.randomUUID().toString();
        final Timestamp NOW = new Timestamp(System.currentTimeMillis());

        Group group = new Group();
        group.setPassword(PASSWORD);
        group.setGroupName(GROUP);

        URL url = new URL();
        url.setUrl(URL);
        url.setLink(LINK);

        Click click = new Click();
        click.setTime(NOW);

        group.setUrls(Arrays.asList(url));
        url.setGroup(group);
        click.setUrl(url);

        EntityManager em = Repository.getEntityManagerFactory().createEntityManager();
        em.getTransaction().begin();
        em.persist(group);
        em.persist(url);
        em.persist(click);
        em.getTransaction().commit();

        Click clickDb = em.find(Click.class, click.getId());
        URL urlDb = clickDb.getUrl();
        Group groupDb = urlDb.getGroup();

        Assert.assertTrue(NOW.equals(clickDb.getTime()));
        Assert.assertEquals(urlDb.getId(), url.getId());
        Assert.assertEquals(urlDb.getUrl(), url.getUrl());
        Assert.assertEquals(urlDb.getLink(), url.getLink());
        Assert.assertEquals(groupDb.getId(), group.getId());
        Assert.assertEquals(groupDb.getGroupName(), group.getGroupName());
        Assert.assertEquals(groupDb.getPassword(), group.getPassword());
        Assert.assertEquals(groupDb.getUrls().get(0).getId(), urlDb.getId());

        em.close();
    }

    @Test
    public void testURLToGroupRelation(){
        EntityManager em = Repository.getEntityManagerFactory().createEntityManager();
        final int SIZE = 20;

        Group group = new Group();
        group.setGroupName(UUID.randomUUID().toString());

        List<URL> urls = new ArrayList<>(SIZE);
        for (int i = 0; i < SIZE; i++) {
            URL url = new URL();
            url.setGroup(group);
            url.setLink(UUID.randomUUID().toString());
            url.setUrl(UUID.randomUUID().toString());
            urls.add(url);
        }

        group.setUrls(urls);

        em.getTransaction().begin();
        em.persist(group);
        for(URL el : urls)
            em.persist(el);
        em.getTransaction().commit();

        Group groupDb = em.find(Group.class, group.getId());

        Assert.assertEquals(SIZE, groupDb.getUrls().size());
        for(URL url : groupDb.getUrls()){
            Assert.assertEquals(url.getGroup().getId(), group.getId());
            Assert.assertNotNull(url.getId());
        }

        em.close();
    }

    @Test
    public void testUniqueColumns(){
        String groupname = UUID.randomUUID().toString();
        Group group = new Group();
        group.setGroupName(groupname);

        EntityManager em = Repository.getEntityManagerFactory().createEntityManager();
        em.getTransaction().begin();
        em.persist(group);
        em.getTransaction().commit();

        group = new Group();
        group.setGroupName(groupname);
        em.getTransaction().begin();
        try{
            em.persist(group);
            em.getTransaction().commit();
        }catch(PersistenceException e){
            em.getTransaction().rollback();
            Assert.assertTrue(e.getCause().getCause().getClass() == MySQLIntegrityConstraintViolationException.class);
        }

        em.close();
    }
}
