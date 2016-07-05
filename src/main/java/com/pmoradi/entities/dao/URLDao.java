package com.pmoradi.entities.dao;

import com.pmoradi.entities.URL;
import com.pmoradi.system.SessionProvider;
import org.hibernate.Session;
import javax.persistence.Query;

public class URLDao {

    private final SessionProvider sessionProvider;

    public URLDao(final SessionProvider sessionProvider) {
        this.sessionProvider = sessionProvider;
    }

    public void save(URL url){
        Session session = sessionProvider.newSession();
        session.getTransaction().begin();
        session.saveOrUpdate(url);
        session.getTransaction().commit();
        session.close();
    }

    public void delete(URL url){
        Session session = sessionProvider.newSession();
        session.getTransaction().begin();
        session.remove(session.contains(url) ? url : session.merge(url));
        session.getTransaction().commit();
        session.close();
    }

    public void bulkDelete(String namespace) {
        Session session = sessionProvider.newSession();
        session.getTransaction().begin();
        Query query = session.createQuery("delete from URL u where u.namespace.name = :name");
        query.setParameter("name", namespace);
        query.executeUpdate();
        session.getTransaction().commit();
        session.close();
    }

    public URL findById(String namespace, String alias){
        Session session = sessionProvider.newSession();
        Query query = session.createQuery("from URL where alias = :alias and namespace.name = :name");
        query.setParameter("name", namespace);
        query.setParameter("alias", alias);

        URL url = query.getResultList().size() == 0 ? null : (URL) query.getSingleResult();

        session.close();
        return url;
    }

    public void click(String namespace, String alias) {
        Session session = sessionProvider.newSession();
        session.getTransaction().begin();

        Query query  = session.createQuery("update URL as u set u.clicks = u.clicks + 1 where u.alias = :alias and u.namespace.name = :name");
        query.setParameter("alias", alias);
        query.setParameter("name", namespace);
        query.executeUpdate();

        session.getTransaction().commit();
        session.close();
    }
}