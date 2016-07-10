package com.pmoradi.entities.dao;

import com.pmoradi.entities.Client;
import com.pmoradi.system.SessionProvider;
import org.hibernate.Session;
import org.hibernate.query.Query;

public class ClientDao {

    private final SessionProvider sessionProvider;

    public ClientDao(final SessionProvider sessionProvider) {
        this.sessionProvider = sessionProvider;
    }

    public void save(Client client){
        Session session = sessionProvider.newSession();
        session.getTransaction().begin();
        session.save(client);
        session.getTransaction().commit();
        session.close();
    }

    public void update(Client client){
        Session session = sessionProvider.newSession();
        session.getTransaction().begin();
        session.update(client);
        session.getTransaction().commit();
        session.close();
    }

    public void delete(Client client){
        Session session = sessionProvider.newSession();
        session.getTransaction().begin();
        session.delete(client);
        session.getTransaction().commit();
        session.close();
    }

    public Client findById(String identifier, String namespace, String alias){
        Session session = sessionProvider.newSession();

        Query query = session.createQuery("from Client where identifier = :identifier and namespace = :namespace and alias = :alias");
        query.setParameter("identifier", identifier);
        query.setParameter("namespace", namespace);
        query.setParameter("alias", alias);

        Object client = query.getSingleResult();
        session.close();
        return (Client) client;
    }

    public Client findByURL(String namespace, String alias){
        Session session = sessionProvider.newSession();

        Query query = session.createQuery("from Client where namespace = :namespace and alias = :alias");
        query.setParameter("namespace", namespace);
        query.setParameter("alias", alias);

        Object client = query.getSingleResult();
        session.close();
        return (Client) client;
    }

    public void deleteAllExpired() {
        Session session = sessionProvider.newSession();
        session.getTransaction().begin();
        javax.persistence.Query query = session.createQuery("delete from Client c where c.expire < :expire");
        query.setParameter("expire", System.currentTimeMillis());
        query.executeUpdate();
        session.getTransaction().commit();
        session.close();
    }
}