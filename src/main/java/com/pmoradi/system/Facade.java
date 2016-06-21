package com.pmoradi.system;

import com.pmoradi.entities.Group;
import com.pmoradi.entities.URL;
import com.pmoradi.entities.dao.GroupDao;
import com.pmoradi.entities.dao.URLDao;
import com.pmoradi.essentials.Marshaller;
import com.pmoradi.essentials.UrlUnavailableException;
import com.pmoradi.rest.entries.GroupEntry;
import com.pmoradi.rest.entries.UrlEntry;
import com.pmoradi.security.SecureStrings;
import com.pmoradi.system.LockManager.Key;

import javax.imageio.ImageIO;
import javax.inject.Inject;
import javax.security.auth.login.CredentialException;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.core.UriBuilder;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.concurrent.ExecutorService;

public class Facade {

    @Inject
    private GroupDao groupDAO;
    @Inject
    private URLDao urlDAO;
    @Inject
    private Application app;

    private Group defaultGroup;
    private final LockManager manager;
    private final ExecutorService executorService;

    public Facade(final LockManager manager,
                  final ExecutorService executorService) {
        this.manager = manager;
        this.executorService = executorService;
    }

    public void addUrl(String urlName, String link, String groupName, String password) throws UrlUnavailableException, CredentialException {
        Key key = manager.lock("group:" + groupName);

        try {
            Group group = groupDAO.findByName(groupName);
            String hash = SecureStrings.md5(password + SecureStrings.getSalt());

            if (group != null && !hash.equals(group.getPassword()))
                throw new CredentialException("Group name and password mismatch.");

            URL url = urlDAO.findByGroupAndUrl(groupName, urlName);
            if (url != null)
                throw new UrlUnavailableException("The URL for the given group is already in use.");

            url = new URL();
            url.setUrl(urlName);
            url.setLink(link);
            url.setAddDate(new Timestamp(System.currentTimeMillis()));

            boolean saveGroup = group == null;
            if (saveGroup) {
                group = new Group();
                group.setGroupName(groupName);
                group.setPassword(hash);
            }
            url.setGroup(group);

            if (saveGroup)
                groupDAO.save(group);
            urlDAO.save(url);
        } finally {
            manager.unlock(key);
        }
    }

    public void addUrl(String urlName, String link) throws UrlUnavailableException {
        Key key = manager.lock("url:" + urlName);

        try {
            URL url = urlDAO.findByGroupAndUrl("default", urlName);
            if(url != null)
                throw new UrlUnavailableException("The URL for the default group is already in use.");

            url = new URL();
            url.setUrl(urlName);
            url.setLink(link);
            url.setAddDate(new Timestamp(System.currentTimeMillis()));
            url.setGroup(getDefaultGroup());

            urlDAO.save(url);
        } finally {
            manager.unlock(key);
        }
    }

    public String getLinkAndClick(String groupName, String urlName) {
        URL url = urlDAO.findByGroupAndUrl(groupName, urlName);
        if (url != null) {
            executorService.submit(()-> urlDAO.click(url));
            return url.getLink();
        }
        return null;
    }

    public UrlEntry getUrlData(String groupName, String urlName) {
        URL url = urlDAO.findByGroupAndUrl(groupName, urlName);
        return url != null ? Marshaller.marshall(url) : null;
    }

    public GroupEntry getGroupData(String groupName, String password) {
        String hash = SecureStrings.md5(password + SecureStrings.getSalt());
        Group group = groupDAO.findByCredentials(groupName, hash);

        return group != null ? Marshaller.marshall(group) : null;
    }

    public long totalURLs() {
        return urlDAO.urls();
    }

    public void deleteUrl(String groupName, String password, String urlName) throws CredentialException, NotFoundException {
        String hash = SecureStrings.md5(password + SecureStrings.getSalt());

        Group group = groupDAO.findByName(groupName);
        if (group == null || !hash.equals(group.getPassword()))
            throw new CredentialException("Group name and password mismatch.");

        URL url = urlDAO.findByGroupAndUrl(groupName, urlName);
        if(url == null)
            throw new NotFoundException("URL not found.");

        urlDAO.delete(url);
    }

    public byte[] toBytes(BufferedImage img) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            ImageIO.write(img, "jpeg", baos);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        return baos.toByteArray();
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

    private Group getDefaultGroup(){
        if(defaultGroup == null) {
            synchronized (this) {
                if(defaultGroup == null) {
                    defaultGroup = groupDAO.findByName("default");
                }
            }
        }
        return defaultGroup;
    }
}
