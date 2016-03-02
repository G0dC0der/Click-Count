package com.pmoradi.entities.dao;

import com.pmoradi.entities.Click;
import com.pmoradi.system.SessionFactory;

import javax.persistence.EntityManager;
import javax.persistence.Query;

public class ClickDao {

    private final SessionFactory sessionFactory;

    public ClickDao(final SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public void save(Click click){
        EntityManager manager = sessionFactory.newSession();
        manager.getTransaction().begin();

        manager.persist(click);

        manager.getTransaction().commit();
        manager.close();
    }

    public void delete(Click click){
        EntityManager manager = sessionFactory.newSession();
        manager.getTransaction().begin();
        manager.remove(manager.contains(click) ? click : manager.merge(click));
        manager.getTransaction().commit();
        manager.close();
    }

    public Click findById(Integer id){
        EntityManager manager = sessionFactory.newSession();
        Click click = manager.find(Click.class, id);
        manager.close();

        return click;
    }

    public long clicks() {
        EntityManager manager = sessionFactory.newSession();

        Query query = manager.createQuery("select count(*) from Click");
        long count = (long) query.getResultList().get(0);

        manager.close();
        return count;
    }
}
