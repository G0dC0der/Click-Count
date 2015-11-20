package com.pmoradi.dao;

import com.pmoradi.entities.Group;
import com.pmoradi.system.Repository;

import javax.persistence.EntityManagerFactory;

public class GroupDao {

    private EntityManagerFactory db;

    public GroupDao(){
        db = Repository.getEntityManagerFactory();
    }

    public Group getByName(String groupName){
        return null;
    }
}
