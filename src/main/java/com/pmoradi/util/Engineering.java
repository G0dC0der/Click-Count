package com.pmoradi.util;

import com.pmoradi.dao.ClickDao;
import com.pmoradi.dao.GroupDao;
import com.pmoradi.dao.URLDao;
import com.pmoradi.entities.Group;
import com.pmoradi.security.SecureStrings;

import javax.imageio.ImageIO;
import javax.inject.Inject;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

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

    public void addUrl(String url, String link, String group, String password) throws Exception{ //Find specific exceptions

    }

    public void addUrl(String url, String link){

    }

    public String getLink(String url){
        return null;
    }

    public boolean verify(String groupName, String password){
        String hash = SecureStrings.md5(password + SecureStrings.getSalt());
        Group group = groupDAO.getByName(groupName);

        return group == null || group.getPassword().equals(hash); //TODO: Do Hibernate return null if the object does not exist?
    }
}
