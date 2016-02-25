package com.pmoradi.system;

import com.pmoradi.entities.Group;
import com.pmoradi.entities.dao.ClickDao;
import com.pmoradi.entities.dao.GroupDao;
import com.pmoradi.entities.dao.URLDao;
import com.pmoradi.entities.dao.UserDao;
import com.pmoradi.security.Role;

import javax.inject.Inject;
import javax.ws.rs.NotFoundException;

public class AdminFacade {

    @Inject
    private ClickDao clickDAO;
    @Inject
    private GroupDao groupDAO;
    @Inject
    private URLDao urlDAO;
    @Inject
    private UserDao userDao;

    public void deleteGroup(String groupName) throws NotFoundException {
        Group group = groupDAO.find(groupName);
        if(group == null)
            throw new NotFoundException("Group not found.");
        groupDAO.delete(group);
    }

    public void deleteURL(String groupName, String urlName) {

    }

    public void renameGroup(String groupName) {

    }

    public void renameURL(String groupName, String urlName) {

    }

    public void addUser(String username, String password, Role role) {

    }

    public void removeUser(String username) {

    }

    public void changeRole(String username, String password, Role newRole) {

    }
}