package com.pmoradi.rest;

import com.pmoradi.rest.entries.AddInEntry;
import com.pmoradi.rest.entries.AddOutEntry;
import com.pmoradi.security.Captcha;
import com.pmoradi.system.Engineering;
import org.apache.commons.collections4.map.PassiveExpiringMap;

import javax.imageio.ImageIO;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Collections;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.LockSupport;

@Path("/")
@Produces("text/json")
public class AddResource {

    private static final int CAPTCHA_TIMEOUT = 60000;
    private static final Map<String, Captcha> CAPTCHAS = Collections.synchronizedMap(new PassiveExpiringMap<>(CAPTCHA_TIMEOUT));
    static{
        Thread cleaner = new Thread(()->{
            while(!Thread.currentThread().isInterrupted()){
                try {
                    Thread.sleep(10000);
                } catch (InterruptedException e) {
                    return;
                }
                CAPTCHAS.isEmpty();
            }
        });
        cleaner.setDaemon(true);
        cleaner.start();
    }

    @Inject
    private Engineering engineering;

    @POST
    @Path("add")
    public Response add(@Context HttpServletRequest requestContext, AddInEntry entry){
        Object resp = "lol";

        return Response.ok(entry).build();
    }

    @POST
    @Path("getCaptcha")
    public Response getCaptcha(@Context HttpServletRequest requestContext){
        Captcha captcha = new Captcha();
        String IP = requestContext.getRemoteAddr();

        synchronized (CAPTCHAS){
            CAPTCHAS.put(IP, captcha);
        }

        AddOutEntry entry = new AddOutEntry();
        entry.setCaptcha("some url");

        return Response.ok(entry).build();
    }

    @POST
    @Path("test")
    @Produces("image/jpeg")
    public Response test(){
        BufferedImage img = new BufferedImage(100, 100, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = img.createGraphics();
        g.setColor(Color.RED);
        g.fillRect(0, 0, 100, 100);
        g.dispose();

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            ImageIO.write(img, "jpeg", baos);
        } catch (IOException e) {
            return Response.serverError().build();
        }
        byte[] imageData = baos.toByteArray();

        return Response.ok(imageData).build();
    }
}
