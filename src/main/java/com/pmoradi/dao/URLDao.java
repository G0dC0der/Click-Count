package com.pmoradi.dao;

import com.pmoradi.entities.Click;
import com.pmoradi.entities.URL;
import com.pmoradi.system.Repository;
import org.hibernate.Hibernate;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.List;

public class URLDao {

    public void save(URL url){
        EntityManager manager = Repository.getDatabase().createEntityManager();
        manager.getTransaction().begin();
        manager.persist(url);
        manager.getTransaction().commit();

        manager.close();
    }

    public void delete(URL url){
        EntityManager manager = Repository.getDatabase().createEntityManager();
        manager.getTransaction().begin();
        manager.remove(manager.contains(url) ? url : manager.merge(url));
        manager.getTransaction().commit();
        manager.close();
    }

    public URL findById(Integer id){
        EntityManager manager = Repository.getDatabase().createEntityManager();
        manager.getTransaction().begin();
        URL url = manager.find(URL.class, id);
        Hibernate.initialize(url.getClicks());
        manager.getTransaction().commit();
        manager.close();

        return url;
    }

    public URL findByUrlName(String urlName){
        EntityManager manager = Repository.getDatabase().createEntityManager();

        Query query = manager.createQuery("from URL where url = :urlname");
        query.setParameter("urlname", urlName);
        URL result = query.getResultList().isEmpty() ? null : (URL) query.getSingleResult();
        manager.close();

        return result;
    }

    public URL findByGroupAndUrl(String group, String urlName){
        EntityManager manager = Repository.getDatabase().createEntityManager();

        Query query = manager.createQuery("from URL as u inner join u.group as g where u.url = :urlname and g.groupName = :groupname");
        query.setParameter("urlname", urlName);
        query.setParameter("groupname", group);
        URL url = query.getResultList().isEmpty() ? null : (URL)((Object[])query.getSingleResult())[0];
        manager.close();

        return url;
    }

    public List<URL> findByLink(String link){
        EntityManager manager = Repository.getDatabase().createEntityManager();
        Query query = manager.createQuery("from URL where link = :link");
        query.setParameter("link", link);
        List<URL> urls = query.getResultList();
        manager.close();

        return urls;
    }

    public URL clickInit(URL url) {
        EntityManager manager = Repository.getDatabase().createEntityManager();
        manager.getTransaction().begin();
        url = manager.merge(url);
        manager.close();

        return url;
    }

    public long urls() {
        EntityManager manager = Repository.getDatabase().createEntityManager();

        Query query = manager.createQuery("select count(*) from URL");
        long count = (long) query.getResultList().get(0);

        manager.close();
        return count;
    }
}
