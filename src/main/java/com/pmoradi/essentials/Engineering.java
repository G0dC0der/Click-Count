package com.pmoradi.essentials;

import com.pmoradi.dao.ClickDao;
import com.pmoradi.dao.EntityDao;
import com.pmoradi.dao.GroupDao;
import com.pmoradi.dao.URLDao;
import com.pmoradi.entities.Click;
import com.pmoradi.entities.Group;
import com.pmoradi.entities.URL;
import com.pmoradi.security.SecureStrings;
import com.pmoradi.util.LinkUtil;

import javax.imageio.ImageIO;
import javax.inject.Inject;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
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

    /**
     * TODO: Move all the validation here(url and group name verification). link escape. Throws proper execptions
     */
    public void addUrl(String urlName, String link, String groupName, String password){
        URL url = new URL();
        url.setUrl(urlName.toLowerCase());
        url.setLink(LinkUtil.addHttp(link));

        String hash = SecureStrings.md5(password + SecureStrings.getSalt());
        Group group = groupDAO.find(groupName, hash);
        if(group == null){
            group = new Group();
            group.setGroupName(groupName.toLowerCase());
            group.setPassword(hash);
            group.setUrls(Arrays.asList(url));
        }
        url.setGroup(group);

        groupDAO.save(group);
        urlDAO.save(url);
    }

    public void addUrl(String urlName, String link){
        URL url = new URL();
        url.setUrl(urlName.toLowerCase());
        url.setLink(LinkUtil.addHttp(link));
        urlDAO.save(url);
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
        return urlDAO.findByUrlName(urlName);
    }

    public URL getURL(String group, String urlName) {
        return urlDAO.findByGroupAndUrl(group, urlName);
    }

    public void click(URL url){
        Click click = new Click();
        click.setTime(new Timestamp(System.currentTimeMillis()));
        click.setUrl(url);
        clickDAO.save(click);
    }

    public boolean validate(String groupName, String password){
        Group group = groupDAO.find(groupName);
        if(group == null)
            return true;

        String hash = SecureStrings.md5(password + SecureStrings.getSalt());
        return group.getPassword().equals(hash);
    }
}
