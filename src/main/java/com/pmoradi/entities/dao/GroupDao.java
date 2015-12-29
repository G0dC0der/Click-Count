package com.pmoradi.entities.dao;

import com.pmoradi.entities.Group;
import com.pmoradi.entities.URL;
import com.pmoradi.system.SessionFactory;
import org.hibernate.Hibernate;

import javax.persistence.EntityManager;
import javax.persistence.Query;

public class GroupDao {

    private final SessionFactory sessionFactory;

    public GroupDao(final SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public void save(Group group){
        EntityManager manager = sessionFactory.newSession();
        manager.getTransaction().begin();
        manager.persist(group);
        manager.getTransaction().commit();

        manager.close();
    }

    public void delete(Group group){
        EntityManager manager = sessionFactory.newSession();
        manager.getTransaction().begin();
        manager.remove(manager.contains(group) ? group : manager.merge(group));
        manager.getTransaction().commit();
        manager.close();
    }

    public Group find(String groupName){
        EntityManager manager = sessionFactory.newSession();
        Query query = manager.createQuery("from Group where groupName = :groupName");
        query.setParameter("groupName", groupName);

        Group group = query.getResultList().size() == 0 ? null : (Group) query.getSingleResult();

        manager.close();
        return group;
    }

    public Group find(String groupName, String password){
        EntityManager manager = sessionFactory.newSession();
        Query query = manager.createQuery("from Group where groupName = :groupName and password = :password");
        query.setParameter("groupName", groupName);
        query.setParameter("password", password);

        Group group = query.getResultList().size() == 0 ? null : (Group) query.getSingleResult();

        manager.close();
        return group;
    }

    public Group fullInit(Group group){
        EntityManager manager = sessionFactory.newSession();
        manager.getTransaction().begin();
        group = manager.merge(group);

        Hibernate.initialize(group.getUrls());
        for(URL url : group.getUrls()){
            Hibernate.initialize(url.getClicks());
            Hibernate.initialize(url.getGroup());
        }

        manager.close();
        return group;
    }
}
