package com.pmoradi.system;

import com.pmoradi.entities.dao.ClickDao;
import com.pmoradi.entities.dao.GroupDao;
import com.pmoradi.entities.dao.URLDao;
import com.pmoradi.entities.Click;
import com.pmoradi.entities.Group;
import com.pmoradi.entities.URL;
import com.pmoradi.essentials.Marshaller;
import com.pmoradi.essentials.UrlUnavailableException;
import com.pmoradi.rest.entries.DataEntry;
import com.pmoradi.rest.entries.GroupEntry;
import com.pmoradi.rest.entries.UrlEntry;
import com.pmoradi.rest.entries.ViewEntry;
import com.pmoradi.security.SecureStrings;
import com.pmoradi.system.LockManager.Key;
import com.pmoradi.essentials.WebUtil;

import javax.annotation.PostConstruct;
import javax.imageio.ImageIO;
import javax.inject.Inject;
import javax.security.auth.login.CredentialException;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
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
        this.executorService = Executors.newFixedThreadPool(100);
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

    public void addUrl(String urlName, String link, String groupName, String password) throws UrlUnavailableException, MalformedURLException, CredentialException {
        if (!WebUtil.validUrl(urlName))
            throw new MalformedURLException("URL contains illegal characters. Use A-Z a-z 0-9 .-_~");
        if (!WebUtil.validUrl(groupName))
            throw new MalformedURLException("Group contains illegal characters. Use A-Z a-z 0-9 .-_~");

        Key key = manager.lock("group:" + groupName);

        try {
            Group group = groupDAO.find(groupName);
            String hash = null;
            if (group != null) {
                hash = SecureStrings.md5(password + SecureStrings.getSalt());
                if (!hash.equals(group.getPassword()))
                    throw new CredentialException("Group name and password mismatch.");
            }

            URL url = getURL(groupName, urlName);
            if (url != null)
                throw new UrlUnavailableException("The URL for the given group is already in use.");

            url = new URL();
            url.setUrl(urlName);
            url.setLink(WebUtil.addHttp(link));
            url.setAddDate(new Timestamp(System.currentTimeMillis()));

            boolean saveGroup = group == null;
            if (saveGroup) {
                group = new Group();
                group.setGroupName(groupName);
                group.setPassword(hash != null ? hash : SecureStrings.md5(password + SecureStrings.getSalt()));
            }
            url.setGroup(group);

            if (saveGroup)
                groupDAO.save(group);
            urlDAO.save(url);
        } finally {
            manager.unlock(key);
        }
    }

    public void addUrl(String urlName, String link) throws UrlUnavailableException, MalformedURLException {
        if (!WebUtil.validUrl(urlName))
            throw new MalformedURLException("URL contains illegal characters. Use A-Z a-z 0-9 .-_~");

        Key key = manager.lock("url:" + urlName);

        try {
            URL url = getURL("default", urlName);
            if(url != null)
                throw new UrlUnavailableException("The URL for the default group is already in use.");

            url = new URL();
            url.setUrl(urlName);
            url.setLink(WebUtil.addHttp(link));
            url.setAddDate(new Timestamp(System.currentTimeMillis()));
            url.setGroup(getDefaultGroup());

            urlDAO.save(url);
        } finally {
            manager.unlock(key);
        }
    }

    public String getLinkAndClick(String groupName, String urlName) {
        URL url = getURL(groupName, urlName);
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

        urlDAO.deleteByName(groupName, urlName);
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

    public void lower(DataEntry entry) {
        entry.setUrlName(entry.getUrlName().toLowerCase());
        entry.setGroupName(entry.getGroupName().toLowerCase());
    }

    public void lower(ViewEntry entry) {
        entry.setGroupName(entry.getGroupName().toLowerCase());
    }

    private void click(URL url) {
        Click click = new Click();
        click.setTime(new Timestamp(System.currentTimeMillis()));
        click.setUrl(url);
        clickDAO.save(click);
    }

    private URL getURL(String groupName, String urlName) {
        return urlDAO.findByGroupAndUrl(groupName, urlName);
    }
}
