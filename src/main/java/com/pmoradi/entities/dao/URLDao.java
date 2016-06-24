package com.pmoradi.entities.dao;

import com.pmoradi.entities.URL;
import com.pmoradi.system.SessionFactory;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.List;

public class URLDao {

    private final SessionFactory sessionFactory;

    public URLDao(final SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public void save(URL url){
        EntityManager manager = sessionFactory.newSession();
        manager.getTransaction().begin();
        manager.persist(url);
        manager.getTransaction().commit();

        manager.close();
    }

    public void delete(URL url){
        EntityManager manager = sessionFactory.newSession();
        manager.getTransaction().begin();
        manager.remove(manager.contains(url) ? url : manager.merge(url));
        manager.getTransaction().commit();
        manager.close();
    }

    public URL findByGroupAndUrl(String groupName, String urlName){
        EntityManager manager = sessionFactory.newSession();

        Query query = manager.createQuery("from URL as u inner join u.group as g where u.url = :urlName and g.groupName = :groupName");
        query.setParameter("urlName", urlName);
        query.setParameter("groupName", groupName);
        URL url = query.getResultList().isEmpty() ? null : (URL)((Object[])query.getSingleResult())[0];
        manager.close();

        return url;
    }

    public List<URL> findByLink(String link){
        EntityManager manager = sessionFactory.newSession();
        Query query = manager.createQuery("from URL where link = :link");
        query.setParameter("link", link);
        List<URL> urls = query.getResultList();
        manager.close();

        return urls;
    }

    public long urls() {
        EntityManager manager = sessionFactory.newSession();

        Query query = manager.createQuery("select count(*) from URL");
        long count = (long) query.getResultList().get(0);

        manager.close();
        return count;
    }

    public void click(URL url) {
        EntityManager manager = sessionFactory.newSession();
        manager.getTransaction().begin();

        Query query  = manager.createQuery("update URL as u set u.clicks = u.clicks + 1 where u.id = :id");
        query.setParameter("id", url.getId());
        query.executeUpdate();

        manager.getTransaction().commit();
        manager.close();
    }
}
