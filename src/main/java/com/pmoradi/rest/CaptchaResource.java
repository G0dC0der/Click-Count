package com.pmoradi.rest;

import com.pmoradi.security.Captcha;
import com.pmoradi.system.Facade;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.StreamingOutput;

@Path("/")
public class CaptchaResource {

    @Inject
    private Facade logic;

    @GET
    @Path("captcha")
    @Produces(MediaType.APPLICATION_OCTET_STREAM)
    public Response captcha(@Context HttpServletRequest request) {
        Captcha captcha = new Captcha(200, 150, 6, 6, null, 60_000);
        request.getSession().setAttribute("captcha", captcha);

        StreamingOutput stream = (outputStream)->{
            for(byte b : logic.toBytes(captcha.create()))
                outputStream.write(b & 0xFF);
            outputStream.close();
        };

        return Response.ok(stream).build();
    }
}
