package com.pmoradi.dao;

import com.pmoradi.entities.Group;
import com.pmoradi.entities.URL;
import com.pmoradi.system.Repository;

import javax.persistence.EntityManager;

public class EntityDao {

    public void save(Group group, URL url) {
        EntityManager em = Repository.getDatabase().createEntityManager();

        em.getTransaction().begin();
        em.persist(url);
        em.persist(group);
        em.getTransaction().commit();
        em.close();
    }
}
