package com.pmoradi.system;

import com.pmoradi.entities.Namespace;
import com.pmoradi.entities.URL;
import com.pmoradi.entities.Collaborator;
import com.pmoradi.entities.dao.NamespaceDao;
import com.pmoradi.entities.dao.URLDao;
import com.pmoradi.entities.dao.CollaboratorDao;
import com.pmoradi.essentials.Marshaller;
import com.pmoradi.rest.entries.GroupEntry;
import com.pmoradi.security.Role;
import com.pmoradi.security.SecureStrings;

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
        Namespace namespace = namespaceDAO.findByName(groupName, false);
        if (namespace != null) {
            urlDAO.bulkDelete(namespace.getName());
            namespaceDAO.delete(namespace);
        } else {
            throw new NotFoundException("Group not found.");
        }
    }

    public void deleteURL(String groupName, String urlName) throws NotFoundException {
        URL url = urlDAO.findById(groupName, urlName);
        if(url == null)
            throw new NotFoundException("The URL was not found.");
        urlDAO.delete(url);
    }

    public void addUser(String username, String password, Role role) throws CredentialException {
        if(collaboratorDao.findByName(username) != null) {
            throw new CredentialException("Username is taken.");
        }

        Collaborator user = new Collaborator();
        user.setUsername(username);
        user.setPassword(SecureStrings.md5(password + SecureStrings.getSalt()));
        user.setRole(role);
        collaboratorDao.save(user);
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

    public GroupEntry sneakPeek(String groupName) throws NotFoundException {
        Namespace namespace = namespaceDAO.findByName(groupName, true);
        if (namespace != null) {
            return Marshaller.marshall(namespace);
        } else {
            throw new NotFoundException("Group not found.");
        }
    }
}