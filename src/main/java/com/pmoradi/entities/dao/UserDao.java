package com.pmoradi.entities.dao;

import com.pmoradi.entities.User;
import com.pmoradi.security.Role;
import com.pmoradi.system.SessionFactory;

import javax.persistence.EntityManager;
import javax.persistence.Query;

public class UserDao {

    private final SessionFactory sessionFactory;

    public UserDao(final SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public User findByName(String username) {
        EntityManager manager = sessionFactory.newSession();
        Query query = manager.createQuery("from User where username = :username");
        query.setParameter("username", username);

        User user = query.getResultList().isEmpty() ? null : (User) query.getSingleResult();

        manager.close();
        return user;

    }

    public void deleteByName(String username) {
        EntityManager manager = sessionFactory.newSession();
        manager.getTransaction().begin();
        Query query = manager.createQuery("delete from User where username = :username");
        query.setParameter("username", username);
        query.executeUpdate();
        manager.getTransaction().commit();
        manager.close();
    }

    public void save(User user) {
        EntityManager manager = sessionFactory.newSession();
        manager.getTransaction().begin();
        manager.persist(user);
        manager.getTransaction().commit();
        manager.close();
    }

    public void updateRole(String username, Role newRole) {
        EntityManager manager = sessionFactory.newSession();
        manager.getTransaction().begin();
        Query query = manager.createQuery("update User set role = :role where username = :username");
        query.setParameter("role", newRole);
        query.setParameter("username", username);
        query.executeUpdate();
        manager.getTransaction().commit();
        manager.close();
    }
}