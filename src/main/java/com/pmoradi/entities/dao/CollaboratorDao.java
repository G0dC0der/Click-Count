package com.pmoradi.entities.dao;

import com.pmoradi.entities.Collaborator;
import com.pmoradi.security.Role;
import com.pmoradi.system.SessionFactory;

import javax.persistence.EntityManager;
import javax.persistence.Query;

public class CollaboratorDao {

    private final SessionFactory sessionFactory;

    public CollaboratorDao(final SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public Collaborator findByName(String username) {
        EntityManager manager = sessionFactory.newSession();
        Query query = manager.createQuery("from Collaborator where username = :username");
        query.setParameter("username", username);

        Collaborator user = query.getResultList().isEmpty() ? null : (Collaborator) query.getSingleResult();

        manager.close();
        return user;
    }

    public void deleteByName(String username) {
        EntityManager manager = sessionFactory.newSession();
        manager.getTransaction().begin();
        Query query = manager.createQuery("delete from Collaborator where username = :username");
        query.setParameter("username", username);
        query.executeUpdate();
        manager.getTransaction().commit();
        manager.close();
    }

    public void save(Collaborator user) {
        EntityManager manager = sessionFactory.newSession();
        manager.getTransaction().begin();
        manager.persist(user);
        manager.getTransaction().commit();
        manager.close();
    }

    public void updateRole(String username, Role newRole) {
        EntityManager manager = sessionFactory.newSession();
        manager.getTransaction().begin();
        Query query = manager.createQuery("update Collaborator set role = :role where username = :username");
        query.setParameter("role", newRole);
        query.setParameter("username", username);
        query.executeUpdate();
        manager.getTransaction().commit();
        manager.close();
    }
}