package com.pmoradi.entities.dao;

import com.pmoradi.entities.User;
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

        User user = query.getResultList().size() == 0 ? null : (User) query.getSingleResult();

        manager.close();
        return user;

    }

    public void deleteByName(String username) {
        EntityManager manager = sessionFactory.newSession();
        Query query = manager.createQuery("delete from User where username = :username");
        query.setParameter("username", username);
        query.executeUpdate();

        manager.close();
    }

    public void save(User user) {
        EntityManager manager = sessionFactory.newSession();
        manager.getTransaction().begin();
        manager.persist(user);
        manager.getTransaction().commit();
        manager.close();
    }
}
