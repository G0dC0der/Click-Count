package com.pmoradi.system;

import com.pmoradi.dao.ClickDao;
import com.pmoradi.dao.EntityDao;
import com.pmoradi.dao.GroupDao;
import com.pmoradi.dao.URLDao;
import com.pmoradi.entities.Click;
import com.pmoradi.entities.Group;
import com.pmoradi.entities.URL;
import com.pmoradi.essentials.GroupUnavailableException;
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
import java.util.Arrays;

public class Engineering {

    @Inject
    private ClickDao clickDAO;
    @Inject
    private GroupDao groupDAO;
    @Inject
    private URLDao urlDAO;
    @Inject
    private EntityDao entityDao;

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

    public void addUrl(String urlName, String link, String groupName, String password) throws GroupUnavailableException, UrlUnavailableException, MalformedURLException, CredentialException {
        urlName = urlName.toLowerCase();
        groupName = groupName.toLowerCase();

        if (!LinkUtil.validUrl(urlName))
            throw new MalformedURLException("URL contains illegal characters. Use A-Z a-z 0-9 .-_~");
        if (!LinkUtil.validUrl(groupName))
            throw new MalformedURLException("Group contains illegal characters. Use A-Z a-z 0-9 .-_~");

        Key key = Repository.getLockManager().lock(groupName);
        try {
            Group group = groupDAO.find(groupName);
            String hash = null;
            if(group != null) {
                hash = SecureStrings.md5(password + SecureStrings.getSalt());
                if(!hash.equals(group.getPassword()))
                    throw new CredentialException("Group name and password mismatch.");
            }

            URL url = getURL(groupName, urlName);
            if (url != null)
                throw new UrlUnavailableException("The URL for the given group is already in use.");

            url = new URL();
            url.setUrl(urlName);
            url.setLink(LinkUtil.addHttp(link));

            boolean saveGroup = false;
            if (group == null) {
                group = new Group();
                group.setGroupName(groupName);
                group.setPassword(hash != null ? hash : SecureStrings.md5(password + SecureStrings.getSalt()));
                group.setUrls(Arrays.asList(url));
                saveGroup = true;
            }
            url.setGroup(group);

            if (saveGroup)
                groupDAO.save(group);
            urlDAO.save(url);
        } finally {
            Repository.getLockManager().unlock(key);
        }
    }

    public void addUrl(String urlName, String link) throws GroupUnavailableException, UrlUnavailableException, MalformedURLException, CredentialException {
        addUrl(urlName, link, "default", "super_secret_password");
    }

    public String getLinkAndClick(String urlName){
        URL url = getURL(urlName);
        if(url != null){
            click(url);
            return url.getLink();
        }
        return null;
    }

    public String getLinkAndClick(String  group, String urlName){
        URL url = getURL(group, urlName);
        if(url != null){
            click(url);
            return url.getLink();
        }
        return null;
    }

    public URL getURL(String urlName){
        return urlDAO.findByUrlName(urlName.toLowerCase());
    }

    public URL getURL(String group, String urlName) {
        return urlDAO.findByGroupAndUrl(group.toLowerCase(), urlName.toLowerCase());
    }

    public void click(URL url){
        Click click = new Click();
        click.setTime(new Timestamp(System.currentTimeMillis()));
        click.setUrl(url);
        clickDAO.save(click);
    }

    public boolean validate(String groupName, String password){
        Group group = groupDAO.find(groupName.toLowerCase());
        if(group == null)
            return true;

        String hash = SecureStrings.md5(password + SecureStrings.getSalt());
        return group.getPassword().equals(hash);
    }
}
