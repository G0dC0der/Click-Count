package com.pmoradi.system;

import com.pmoradi.entities.Click;
import com.pmoradi.entities.Group;
import com.pmoradi.entities.URL;
import com.pmoradi.entities.dao.ClickDao;
import com.pmoradi.entities.dao.GroupDao;
import com.pmoradi.entities.dao.URLDao;
import com.pmoradi.essentials.Marshaller;
import com.pmoradi.essentials.UrlUnavailableException;
import com.pmoradi.essentials.WebUtil;
import com.pmoradi.rest.entries.DataEntry;
import com.pmoradi.rest.entries.GroupEntry;
import com.pmoradi.rest.entries.UrlEntry;
import com.pmoradi.rest.entries.ViewEntry;
import com.pmoradi.security.SecureStrings;
import com.pmoradi.system.LockManager.Key;

import javax.imageio.ImageIO;
import javax.inject.Inject;
import javax.security.auth.login.CredentialException;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Facade {

    @Inject
    private ClickDao clickDAO;
    @Inject
    private GroupDao groupDAO;
    @Inject
    private URLDao urlDAO;

    private Group defaultGroup;
    private final LockManager manager;
    private final ExecutorService executorService;

    public Facade(final LockManager manager) {
        this.manager = manager;
        this.executorService = Executors.newFixedThreadPool(50);
    }

    public void addUrl(String urlName, String link, String groupName, String password) throws UrlUnavailableException, CredentialException {
        Key key = manager.lock("group:" + groupName);

        try {
            Group group = groupDAO.find(groupName);
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
            executorService.submit(()-> click(url));
            return url.getLink();
        }
        return null;
    }

    public UrlEntry getUrlData(String groupName, String urlName) {
        URL url = urlDAO.findByGroupAndUrl(groupName, urlName);
        return url != null ? Marshaller.marshall(urlDAO.clickInit(url)) : null;
    }

    public GroupEntry getGroupData(String groupName, String password) {
        String hash = SecureStrings.md5(password + SecureStrings.getSalt());
        Group group = groupDAO.find(groupName, hash);

        return group != null ? Marshaller.marshall(groupDAO.fullInit(group)) : null;
    }

    public long totalURLs() {
        return urlDAO.urls();
    }

    public long totalClicks() {
        return clickDAO.clicks();
    }

    public void delete(String groupName, String password, String urlName) throws CredentialException {
        String hash = SecureStrings.md5(password + SecureStrings.getSalt());

        Group group = groupDAO.find(groupName);
        if (group == null || !hash.equals(group.getPassword()))
            throw new CredentialException("Group name and password mismatch.");

        urlDAO.delete(urlDAO.findByGroupAndUrl(groupName, urlName));
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

    public void fix(DataEntry entry) {
        entry.setUrlName(polish(entry.getUrlName()));
        entry.setGroupName(polish(entry.getGroupName()));
    }

    public void fix(ViewEntry entry) {
        entry.setGroupName(polish(entry.getGroupName()));
    }

    public String polish(String string) {
        return string.trim().toLowerCase();
    }

    private void click(URL url) {
        Click click = new Click();
        click.setTime(new Timestamp(System.currentTimeMillis()));
        click.setUrl(url);
        clickDAO.save(click);
    }

    private Group getDefaultGroup(){
        if(defaultGroup == null) {
            synchronized (this) {
                if(defaultGroup == null) {
                    defaultGroup = groupDAO.find("default");
                }
            }
        }
        return defaultGroup;
    }

}
