package com.pmoradi.system;

import com.pmoradi.entities.Client;
import com.pmoradi.entities.Namespace;
import com.pmoradi.entities.URL;
import com.pmoradi.entities.dao.ClientDao;
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
import java.util.concurrent.TimeUnit;

public class Facade {

    public static final long CLICK_DELAY = TimeUnit.HOURS.toMillis(1);

    @Inject
    private NamespaceDao namespaceDAO;
    @Inject
    private URLDao urlDAO;
    @Inject
    private ClientDao clientDAO;
    @Inject
    private Application app;

    private Namespace defaultNamespace;
    private final ExecutorService executorService;

    public Facade(final ExecutorService executorService) {
        this.executorService = executorService;
    }

    void init(){
        Thread cleanerThread = new Thread(()->{
            while (true) {
                try {
                    Thread.sleep(CLICK_DELAY / 10);
                    clientDAO.deleteAllExpired();
                } catch (InterruptedException e) {
                    return;
                }
            }
        });
        cleanerThread.setDaemon(true);
        cleanerThread.start();

        defaultNamespace = namespaceDAO.findByName("default", false);
    }

    public void addUrl(String alias, String sourceUrl, String groupName, String password) throws UrlUnavailableException, CredentialException {
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
        url.setAlias(alias);
        url.setSource(sourceUrl);
        url.setClicks(0L);
        url.setAdded(System.currentTimeMillis());

        try {
            urlDAO.save(url);
        } catch (RollbackException e) {
            throw new UrlUnavailableException("The alias for the given group already exists.");
        }
    }

    public void addUrl(String alias, String sourceUrl) throws UrlUnavailableException {
        URL url = urlDAO.findById("default", alias);
        if (url != null)
            throw new UrlUnavailableException("The alias for the default group is already in use.");

        url = new URL();
        url.setAlias(alias);
        url.setSource(sourceUrl);
        url.setAdded(System.currentTimeMillis());
        url.setClicks(0L);
        url.setNamespace(defaultNamespace);

        try {
            urlDAO.save(url);
        } catch (RollbackException e) {
            throw new UrlUnavailableException("The given alias for the default group already exists.");
        }
    }

    public String getSourceUrlAndClick(String identifier, String groupName, String alias) {
        String link = urlDAO.findSourceUrlById(groupName, alias);
        if (link != null) {
            executorService.submit(()-> click(identifier, groupName, alias));
            return link;
        }
        return null;
    }

    public UrlEntry getUrlData(String groupName, String alias) {
        URL url = urlDAO.findById(groupName, alias);
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

    public String constructRedirectURL(String groupName, String alias) {
        return UriBuilder
                .fromPath(app.getRestPath())
                .path(groupName)
                .path(alias)
                .build()
                .toString();
    }

    private void click(String identifier, String groupName, String alias){
        Client client = clientDAO.findById(identifier, groupName, alias);
        if (client == null) {
            client = new Client();
            client.setIdentifier(identifier);
            client.setNamespace(groupName);
            client.setAlias(alias);
            client.setExpire(System.currentTimeMillis() + CLICK_DELAY);

            clientDAO.save(client);
            urlDAO.click(groupName, alias);
        } else if (client.getExpire() < System.currentTimeMillis()){
            client.setExpire(System.currentTimeMillis() + CLICK_DELAY);

            clientDAO.update(client);
            urlDAO.click(groupName, alias);
        }
    }
}