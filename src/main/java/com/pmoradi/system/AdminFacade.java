package com.pmoradi.system;

import com.pmoradi.entities.Group;
import com.pmoradi.entities.URL;
import com.pmoradi.entities.User;
import com.pmoradi.entities.dao.GroupDao;
import com.pmoradi.entities.dao.URLDao;
import com.pmoradi.entities.dao.UserDao;
import com.pmoradi.essentials.Marshaller;
import com.pmoradi.rest.entries.GroupEntry;
import com.pmoradi.security.Role;
import com.pmoradi.security.SecureStrings;

import javax.inject.Inject;
import javax.ws.rs.NotFoundException;

public class AdminFacade {

    @Inject
    private GroupDao groupDAO;
    @Inject
    private URLDao urlDAO;
    @Inject
    private UserDao userDao;

    public void deleteGroup(String groupName) throws NotFoundException {
        Group group = groupDAO.findByName(groupName);
        if(group == null)
            throw new NotFoundException("Group not found.");
        groupDAO.delete(group);
    }

    public void deleteURL(String groupName, String urlName) throws NotFoundException {
        URL url = urlDAO.findByGroupAndUrl(groupName, urlName);
        if(url == null)
            throw new NotFoundException("The URL was not found.");
        urlDAO.delete(url);
    }

    public void addUser(String username, String password, Role role) {
        User user = new User();
        user.setUsername(username);
        user.setPassword(SecureStrings.md5(password + SecureStrings.getSalt()));
        user.setRole(role);
        userDao.save(user);
    }

    public void removeUser(String username) throws NotFoundException {
        User user = userDao.findByName(username);
        if(user == null)
            throw new NotFoundException("Group not found.");
        userDao.deleteByName(username);
    }

    public void changeRole(String username, Role newRole) throws NotFoundException {
        User user = userDao.findByName(username);
        if(user == null)
            throw new NotFoundException("Group not found.");
        userDao.updateRole(username, newRole);
    }

    public GroupEntry viewGroupData(String groupName) throws NotFoundException {
        Group group = groupDAO.findByName(groupName);
        if(group == null)
            throw new NotFoundException("Group not found.");

        return Marshaller.marshall(group);
    }
}