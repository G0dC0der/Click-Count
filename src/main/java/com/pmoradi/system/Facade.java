package com.pmoradi.system;

import com.pmoradi.entities.Namespace;
import com.pmoradi.entities.URL;
import com.pmoradi.entities.dao.NamespaceDao;
import com.pmoradi.entities.dao.URLDao;
import com.pmoradi.essentials.Marshaller;
import com.pmoradi.essentials.UrlUnavailableException;
import com.pmoradi.rest.entries.GroupEntry;
import com.pmoradi.rest.entries.UrlEntry;
import com.pmoradi.security.SecureStrings;

import javax.inject.Inject;
import javax.persistence.RollbackException;
import javax.security.auth.login.CredentialException;
import javax.ws.rs.core.UriBuilder;
import java.util.concurrent.ExecutorService;

public class Facade {

    @Inject
    private NamespaceDao namespaceDAO;
    @Inject
    private URLDao urlDAO;
    @Inject
    private Application app;

    private Namespace defaultNamespace;
    private final ExecutorService executorService;

    public Facade(final ExecutorService executorService) {
        this.executorService = executorService;
    }

    public void addUrl(String urlName, String link, String groupName, String password) throws UrlUnavailableException, CredentialException {
        Namespace namespace = namespaceDAO.findByName(groupName, false);
        String hash = SecureStrings.md5(password + SecureStrings.getSalt());

        if (namespace == null) {
            namespace = new Namespace(groupName, hash);
            namespaceDAO.save(namespace);
        } else if (!hash.equals(namespace.getPassword())) {
            throw new CredentialException("Group name and password mismatch.");
        }

        URL url = new URL();
        url.setNamespace(namespace);
        url.setAlias(urlName);
        url.setLink(link);
        url.setClicks(0L);
        url.setAdded(System.currentTimeMillis());

        try {
            urlDAO.save(url);
        } catch (RollbackException e) {
            throw new UrlUnavailableException("The URL for the given group already exists.");
        }
    }

    public void addUrl(String urlName, String link) throws UrlUnavailableException {
        URL url = urlDAO.findById("default", urlName);
        if (url != null)
            throw new UrlUnavailableException("The URL for the default group is already in use.");

        url = new URL();
        url.setAlias(urlName);
        url.setLink(link);
        url.setAdded(System.currentTimeMillis());
        url.setClicks(0L);
        url.setNamespace(getDefaultNamespace());

        try {
            urlDAO.save(url);
        } catch (RollbackException e) {
            throw new UrlUnavailableException("The given URL for the default group already exists.");
        }
    }

    public String getLinkAndClick(String groupName, String urlName) {
        String link = urlDAO.findLinkById(groupName, urlName);
        if (link != null) {
            executorService.submit(()-> urlDAO.click(groupName, urlName));
            return link;
        }
        return null;
    }

    public UrlEntry getUrlData(String groupName, String urlName) {
        URL url = urlDAO.findById(groupName, urlName);
        return url != null ? Marshaller.marshall(url) : null;
    }

    public GroupEntry getGroupData(String groupName, String password) {
        String hash = SecureStrings.md5(password + SecureStrings.getSalt());
        Namespace namespace = namespaceDAO.findByCredentials(groupName, hash);
        return namespace != null ? Marshaller.marshall(namespace) : null;
    }

    public String constructRedirectURL(String urlName) {
        return UriBuilder
                .fromPath(app.getRestPath())
                .path(urlName)
                .build()
                .toString();
    }

    public String constructRedirectURL(String groupName, String urlName) {
        return UriBuilder
                .fromPath(app.getRestPath())
                .path(groupName)
                .path(urlName)
                .build()
                .toString();
    }

    private Namespace getDefaultNamespace(){
        if (defaultNamespace == null) {
            synchronized (this) {
                if (defaultNamespace == null) {
                    defaultNamespace = namespaceDAO.findByName("default", false);
                }
            }
        }
        return defaultNamespace;
    }
}