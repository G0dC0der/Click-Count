package com.pmoradi.essentials;

import com.pmoradi.dao.ClickDao;
import com.pmoradi.dao.GroupDao;
import com.pmoradi.dao.URLDao;
import com.pmoradi.entities.Group;
import com.pmoradi.entities.URL;
import com.pmoradi.security.SecureStrings;
import com.pmoradi.util.LinkUtil;

import javax.imageio.ImageIO;
import javax.inject.Inject;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Arrays;

public class Engineering {

    @Inject
    private ClickDao clickDAO;
    @Inject
    private GroupDao groupDAO;
    @Inject
    private URLDao urlDAO;

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

    public void addUrl(String urlName, String link, String groupName, String password){
        URL url = new URL();
        url.setUrl(urlName);
        url.setLink(LinkUtil.addHttp(link));

        String hash = SecureStrings.md5(password + SecureStrings.getSalt());
        Group group = groupDAO.find(groupName, hash);
        if(group == null){
            group = new Group();
            group.setGroupName(groupName);
            group.setPassword(hash);
            group.setUrls(Arrays.asList(url));
        }
        url.setGroup(group);

        groupDAO.save(group);
        urlDAO.save(url);
    }

    public void addUrl(String urlName, String link){
        URL url = new URL();
        url.setUrl(urlName);
        url.setLink(LinkUtil.addHttp(link));
        urlDAO.save(url);
    }

    public String getLink(String urlName){
        URL url = urlDAO.findByUrlName(urlName);
        if(url == null)
            return null;
        return url.getLink();
    }

    public boolean validate(String groupName, String password){
        Group group = groupDAO.find(groupName);
        if(group == null)
            return true;

        String hash = SecureStrings.md5(password + SecureStrings.getSalt());
        return group.getPassword().equals(hash);
    }
}
