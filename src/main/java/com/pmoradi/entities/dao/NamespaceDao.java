package com.pmoradi.entities.dao;

import com.pmoradi.entities.Namespace;
import com.pmoradi.system.SessionProvider;
import org.hibernate.Session;
import org.hibernate.query.Query;

public class NamespaceDao {

    private final SessionProvider sessionProvider;

    public NamespaceDao(final SessionProvider sessionProvider) {
        this.sessionProvider = sessionProvider;
    }

    public void save(Namespace namespace){
        Session session = sessionProvider.provide();
        session.getTransaction().begin();
        session.save(namespace);
        session.getTransaction().commit();
        session.close();
    }

    public void delete(Namespace namespace){
        Session session = sessionProvider.provide();
        session.getTransaction().begin();
        session.remove(session.contains(namespace) ? namespace : session.merge(namespace));
        session.getTransaction().commit();
        session.close();
    }
    public Namespace findByName(String name, boolean eager){
        Session session = sessionProvider.provide();
        Namespace namespace;
        if (eager) {
            namespace = session.find(Namespace.class, name);
        } else {
            Query query = session.createQuery("select new Namespace(name, password) from Namespace where name = :name");
            query.setParameter("name", name);

            namespace = (Namespace) query.getSingleResult();
        }
        session.close();
        return namespace;
    }

    public Namespace findByCredentials(String name, String password) {
        Session session = sessionProvider.provide();

        Query query = session.createQuery("from Namespace where name = :name and password = :password");
        query.setParameter("name", name);
        query.setParameter("password", password);

        Object namespace = query.getSingleResult();
        session.close();
        return (Namespace) namespace;
    }
}
