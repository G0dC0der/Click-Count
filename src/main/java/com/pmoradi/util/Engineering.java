package com.pmoradi.util;

import com.pmoradi.dao.ClickDao;
import com.pmoradi.dao.GroupDao;
import com.pmoradi.dao.URLDao;

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

    public byte[] toBytes(BufferedImage img) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(img, "jpeg", baos);
        return baos.toByteArray();
    }
}
