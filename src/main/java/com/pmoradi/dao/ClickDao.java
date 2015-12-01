package com.pmoradi.dao;

import com.pmoradi.entities.Click;
import com.pmoradi.system.Repository;

import javax.inject.Inject;
import javax.persistence.EntityManager;

public class ClickDao {

    @Inject
    private URLDao urlDao;

    public void save(Click click){
        EntityManager manager = Repository.newSession();
        manager.getTransaction().begin();

        manager.persist(click);

        manager.getTransaction().commit();
        manager.close();
    }

    public void delete(Click click){
        EntityManager manager = Repository.newSession();
        manager.getTransaction().begin();
        manager.remove(manager.contains(click) ? click : manager.merge(click));
        manager.getTransaction().commit();
        manager.close();
    }

    public Click findById(Integer id){
        EntityManager manager = Repository.newSession();
        Click click = manager.find(Click.class, id);
        manager.close();

        return click;
    }
}
