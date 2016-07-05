package com.pmoradi.system;

import com.pmoradi.entities.URL;
import com.pmoradi.entities.Collaborator;
import com.pmoradi.entities.dao.NamespaceDao;
import com.pmoradi.entities.dao.URLDao;
import com.pmoradi.entities.dao.CollaboratorDao;
import com.pmoradi.rest.entries.GroupEntry;
import com.pmoradi.security.Role;

import javax.inject.Inject;
import javax.security.auth.login.CredentialException;
import javax.ws.rs.NotFoundException;

public class AdminFacade {

    @Inject
    private NamespaceDao namespaceDAO;
    @Inject
    private URLDao urlDAO;
    @Inject
    private CollaboratorDao collaboratorDao;

    public void deleteGroup(String groupName) throws NotFoundException {
//        Group group = groupDAO.findByName(groupName);
//        if(group == null)
            throw new NotFoundException("Group not found.");
//        groupDAO.delete(group);
    }

    public void deleteURL(String groupName, String urlName) throws NotFoundException {
        URL url = urlDAO.findById(groupName, urlName);
        if(url == null)
            throw new NotFoundException("The URL was not found.");
        urlDAO.delete(url);
    }

    public void addUser(String username, String password, Role role) throws CredentialException {
//        if(collaboratorDao.findByName(username) != null) {
//            throw new CredentialException("Username is taken.");
//        }
//
//        Collaborator user = new Collaborator();
//        user.setUsername(username);
//        user.setPassword(SecureStrings.md5(password + SecureStrings.getSalt()));
//        user.setRole(role);
//        collaboratorDao.save(user);
    }

    public void removeUser(String username) throws NotFoundException {
        Collaborator user = collaboratorDao.findByName(username);
        if(user == null)
            throw new NotFoundException("User not found.");
        collaboratorDao.deleteByName(username);
    }

    public void changeRole(String username, Role newRole) throws NotFoundException {
        Collaborator user = collaboratorDao.findByName(username);
        if(user == null)
            throw new NotFoundException("User not found.");
        collaboratorDao.updateRole(username, newRole);
    }

    public GroupEntry viewGroupData(String groupName) throws NotFoundException {
//        Group group = groupDAO.findByName(groupName);
//        if(group == null)
            throw new NotFoundException("Group not found.");

//        return Marshaller.marshall(group);
    }
}