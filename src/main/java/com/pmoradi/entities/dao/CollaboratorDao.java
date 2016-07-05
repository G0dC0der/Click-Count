package com.pmoradi.entities.dao;

import com.pmoradi.entities.Collaborator;
import com.pmoradi.security.Role;
import com.pmoradi.system.SessionProvider;
import org.hibernate.Session;

import javax.persistence.Query;

public class CollaboratorDao {

    private final SessionProvider sessionProvider;

    public CollaboratorDao(final SessionProvider sessionProvider) {
        this.sessionProvider = sessionProvider;
    }

    public Collaborator findByName(String username) {
        Session session = sessionProvider.newSession();
        Query query = session.createQuery("from Collaborator where username = :username");
        query.setParameter("username", username);

        Collaborator user = query.getResultList().isEmpty() ? null : (Collaborator) query.getSingleResult();

        session.close();
        return user;
    }

    public void deleteByName(String username) {
        Session session = sessionProvider.newSession();
        session.getTransaction().begin();
        Query query = session.createQuery("delete from Collaborator where username = :username");
        query.setParameter("username", username);
        query.executeUpdate();
        session.getTransaction().commit();
        session.close();
    }

    public void save(Collaborator user) {
        Session session = sessionProvider.newSession();
        session.getTransaction().begin();
        session.persist(user);
        session.getTransaction().commit();
        session.close();
    }

    public void updateRole(String username, Role newRole) {
        Session session = sessionProvider.newSession();
        session.getTransaction().begin();
        Query query = session.createQuery("update Collaborator set role = :role where username = :username");
        query.setParameter("role", newRole);
        query.setParameter("username", username);
        query.executeUpdate();
        session.getTransaction().commit();
        session.close();
    }
}