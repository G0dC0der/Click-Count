package com.pmoradi.rest;

import com.pmoradi.rest.entries.AddInEntry;
import com.pmoradi.rest.entries.AddOutEntry;
import com.pmoradi.security.Captcha;
import com.pmoradi.util.CachedMap;
import com.pmoradi.util.Engineering;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.StreamingOutput;
import java.util.Map;

@Path("/")
public class AddResource {

    private static final Map<String, Captcha> CAPTCHAS = CachedMap.getCachedMap(60000);

    @Inject
    private Engineering engineering;

    @POST
    @Path("add")
    @Produces("text/json")
    public Response add(@Context HttpServletRequest requestContext, AddInEntry entry){
        AddOutEntry out = new AddOutEntry(entry);
        String ip = requestContext.getRemoteAddr();
        String word = entry.getCaptcha();
        Captcha captcha = CAPTCHAS.get(ip);

        if(captcha == null){
            out.setCaptchaError("Captcha has expired.");
            return Response.status(403).entity(out).build();
        } else if(!captcha.isCorrect(word)){
            out.setCaptchaError("The captcha is incorrect.");
            return Response.status(403).entity(out).build();
        }

        //If not using password, check if url is used.
        //Else, check is group + password is correct.

        out.setCaptcha("");
        out.setPassword("");
        return Response.ok(out).build();
    }

    @GET
    @Path("captcha")
    @Produces("application/octet-stream")
    public Response getCaptcha(@Context HttpServletRequest requestContext) {
        Captcha captcha = new Captcha();
        String IP = requestContext.getRemoteAddr();
        CAPTCHAS.put(IP, captcha);

        StreamingOutput stream = (outputStream)->{
            for(byte b : engineering.toBytes(captcha.create()))
                outputStream.write(b & 0xFF);
            outputStream.close();
        };

        return Response.ok(stream).build();
    }
}
