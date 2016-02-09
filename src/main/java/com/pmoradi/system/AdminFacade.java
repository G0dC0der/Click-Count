package com.pmoradi.system;

import com.pmoradi.entities.Group;
import com.pmoradi.entities.User;
import com.pmoradi.entities.dao.ClickDao;
import com.pmoradi.entities.dao.GroupDao;
import com.pmoradi.entities.dao.URLDao;
import com.pmoradi.entities.dao.UserDao;
import com.pmoradi.security.Role;
import com.pmoradi.security.SecureStrings;

import javax.inject.Inject;
import javax.security.auth.login.CredentialException;
import javax.ws.rs.NotFoundException;
import java.util.Calendar;

public class AdminFacade {

    @Inject
    private ClickDao clickDAO;
    @Inject
    private GroupDao groupDAO;
    @Inject
    private URLDao urlDAO;
    @Inject
    private UserDao userDao;

    public void deleteGroup(String groupName) {
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

    public void cheat(String groupName, String urlName, int countManipulation) {

    }

    public void addUser(String username, String password, Role role) {

    }

    public void removeUser(String username) {

    }

    public void authenticate(String username, String password, Role requiredRole) throws CredentialException {
        String hash = (SecureStrings.md5(password + SecureStrings.getSalt()));
        User user = userDao.findByName(username);

        if(user == null || !user.getPassword().equals(hash))
            throw new CredentialException("Username and password mismatch.");
        if(requiredRole.isAbove(user.getRole()))
            throw new CredentialException("Role not sufficient.");
    }
}