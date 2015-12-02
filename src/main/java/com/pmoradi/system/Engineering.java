package com.pmoradi.system;

import com.pmoradi.dao.ClickDao;
import com.pmoradi.dao.EntityDao;
import com.pmoradi.dao.GroupDao;
import com.pmoradi.dao.URLDao;
import com.pmoradi.entities.Click;
import com.pmoradi.entities.Group;
import com.pmoradi.entities.URL;
import com.pmoradi.essentials.UrlUnavailableException;
import com.pmoradi.security.SecureStrings;
import com.pmoradi.system.LockManager.Key;
import com.pmoradi.util.LinkUtil;

import javax.imageio.ImageIO;
import javax.inject.Inject;
import javax.security.auth.login.CredentialException;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.sql.Timestamp;

public class Engineering {

    @Inject
    private ClickDao clickDAO;
    @Inject
    private GroupDao groupDAO;
    @Inject
    private URLDao urlDAO;
    @Inject
    private EntityDao entityDao;

    public void addUrl(String urlName, String link, String groupName, String password) throws UrlUnavailableException, MalformedURLException, CredentialException {
        urlName = urlName.toLowerCase();
        groupName = groupName.toLowerCase();

        if (!LinkUtil.validUrl(urlName) || LinkUtil.isReserved(urlName))
            throw new MalformedURLException("URL contains illegal characters or a reserved word. Use A-Z a-z 0-9 .-_~");
        if (!LinkUtil.validUrl(groupName) || LinkUtil.isReserved(groupName))
            throw new MalformedURLException("Group contains illegal characters or a reserved word. Use A-Z a-z 0-9 .-_~");

        Key key = Repository.getLockManager().lock("group:" + groupName);

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
            url.setLink(LinkUtil.addHttp(link));

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
            Repository.getLockManager().unlock(key);
        }
    }

    public void addUrl(String urlName, String link) throws UrlUnavailableException, MalformedURLException {
        urlName = urlName.toLowerCase();

        if (!LinkUtil.validUrl(urlName) || LinkUtil.isReserved(urlName))
            throw new MalformedURLException("URL contains illegal characters or a reserved word. Use A-Z a-z 0-9 .-_~");

        Group defaultGroup = Repository.defaultGroup();
        Key key = Repository.getLockManager().lock("url:" + urlName);

        try {
            URL url = getURL("default", urlName);
            if(url != null)
                throw new UrlUnavailableException("The URL for the default group is already in use.");

            url = new URL();
            url.setUrl(urlName);
            url.setLink(LinkUtil.addHttp(link));
            url.setGroup(defaultGroup);

            urlDAO.save(url);
        } finally {
            Repository.getLockManager().unlock(key);
        }
    }

    public String getLinkAndClick(String groupName, String urlName) {
        URL url = getURL(groupName, urlName);
        if (url != null) {
            click(url);
            return url.getLink();
        }
        return null;
    }

    public URL getURL(String groupName, String urlName) {
        return urlDAO.findByGroupAndUrl(groupName.toLowerCase(), urlName.toLowerCase());
    }

    public void click(URL url) {
        Click click = new Click();
        click.setTime(new Timestamp(System.currentTimeMillis()));
        click.setUrl(url);
        clickDAO.save(click);
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
}
