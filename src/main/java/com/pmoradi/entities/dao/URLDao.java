package com.pmoradi.entities.dao;

import com.pmoradi.entities.URL;
import com.pmoradi.system.SessionFactory;
import org.hibernate.Hibernate;

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

    public void deleteByName(String groupName, String urlName){
        EntityManager manager = sessionFactory.newSession();

        Query query = manager.createQuery("delete from URL as u inner join u.group as g where u.url = :urlname and g.groupName = :groupname");
        query.setParameter("urlname", urlName);
        query.setParameter("groupname", groupName);
        query.executeUpdate();
        manager.close();
    }

    public URL findById(Integer id){
        EntityManager manager = sessionFactory.newSession();
        manager.getTransaction().begin();
        URL url = manager.find(URL.class, id);
        Hibernate.initialize(url.getClicks());
        manager.getTransaction().commit();
        manager.close();

        return url;
    }

    public URL findByGroupAndUrl(String groupName, String urlName){
        EntityManager manager = sessionFactory.newSession();

        Query query = manager.createQuery("from URL as u inner join u.group as g where u.url = :urlname and g.groupName = :groupname");
        query.setParameter("urlname", urlName);
        query.setParameter("groupname", groupName);
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

    public URL clickInit(URL url) {
        EntityManager manager = sessionFactory.newSession();
        manager.getTransaction().begin();
        url = manager.merge(url);
        manager.close();

        return url;
    }

    public long urls() {
        EntityManager manager = sessionFactory.newSession();

        Query query = manager.createQuery("select count(*) from URL");
        long count = (long) query.getResultList().get(0);

        manager.close();
        return count;
    }
}
