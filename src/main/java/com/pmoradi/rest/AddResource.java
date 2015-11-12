package com.pmoradi.rest;

import com.pmoradi.rest.entries.AddInEntry;
import com.pmoradi.rest.entries.AddOutEntry;
import com.pmoradi.security.Captcha;
import com.pmoradi.util.CachedMap;
import com.pmoradi.util.Engineering;

import javax.imageio.ImageIO;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Map;

@Path("/")
@Produces("text/json")
public class AddResource {

    private static final Map<String, Captcha> CAPTCHAS = CachedMap.getCachedMap(60000);

    @Inject
    private Engineering engineering;

    @POST
    @Path("add")
    public Response add(@Context HttpServletRequest requestContext, AddInEntry entry){
        String IP = requestContext.getRemoteAddr();
        String word = entry.getCaptcha();
        Captcha captcha = CAPTCHAS.get(IP);

        if(captcha == null){
            //Captcha expired
        } else if(!captcha.isCorrect(word)){
            //Wrong captcha
        } else {

        }

        return Response.ok(entry).build();
    }

    @POST
    @Path("getCaptcha")
    public Response getCaptcha(@Context HttpServletRequest requestContext){
        Captcha captcha = new Captcha();
        String IP = requestContext.getRemoteAddr();

        CAPTCHAS.put(IP, captcha);

        AddOutEntry entry = new AddOutEntry();
        entry.setCaptcha("some url"); //TODO: This is what test() is for.

        return Response.ok(entry).build();
    }

    @GET
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
