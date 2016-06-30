package com.pmoradi.entities.dao;

import com.pmoradi.entities.Namespace;
import com.pmoradi.system.SessionFactory;

import javax.persistence.EntityManager;
import javax.persistence.Query;

public class NamespaceDao {

    private final SessionFactory sessionFactory;

    public NamespaceDao(final SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public void save(Namespace namespace){
        EntityManager manager = sessionFactory.newSession();
        manager.getTransaction().begin();
        manager.persist(namespace);
        manager.getTransaction().commit();

        manager.close();
    }

//    public void delete(Group group){
//        EntityManager manager = sessionFactory.newSession();
//        manager.getTransaction().begin();
//        manager.remove(manager.contains(group) ? group : manager.merge(group));
//        manager.getTransaction().commit();
//        manager.close();
//    }
//
    public Namespace findByName(String name){
        EntityManager manager = sessionFactory.newSession();
        Query query = manager.createQuery("from Namespace where name = :name");
        query.setParameter("name", name);

        Namespace namespace = query.getResultList().size() == 0 ? null : (Namespace) query.getSingleResult();

        manager.close();
        return namespace;
    }
//
//    public Group findByCredentials(String groupName, String password){
//        EntityManager manager = sessionFactory.newSession();
//        Query query = manager.createQuery("from Group where groupName = :groupName and password = :password");
//        query.setParameter("groupName", groupName);
//        query.setParameter("password", password);
//
//        Group group = query.getResultList().size() == 0 ? null : (Group) query.getSingleResult();
//
//        manager.close();
//        return group;
//    }
}
