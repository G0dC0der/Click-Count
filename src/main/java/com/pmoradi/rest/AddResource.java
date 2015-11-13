package com.pmoradi.rest;

import com.pmoradi.rest.entries.AddInEntry;
import com.pmoradi.rest.entries.AddOutEntry;
import com.pmoradi.security.Captcha;
import com.pmoradi.util.CachedMap;
import com.pmoradi.util.Engineering;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
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

    @POST
    @Path("captcha")
    public Response getCaptcha(@Context HttpServletRequest requestContext) throws IOException { //TODO: Remove throws. Handle it instead.
        Captcha captcha = new Captcha();
        String IP = requestContext.getRemoteAddr();
        CAPTCHAS.put(IP, captcha);

        return Response.ok(engineering.toBytes(captcha.create())).build();
    }
}
