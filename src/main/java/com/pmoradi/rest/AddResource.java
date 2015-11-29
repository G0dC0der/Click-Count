package com.pmoradi.rest;

import com.pmoradi.essentials.CachedMap;
import com.pmoradi.essentials.Engineering;
import com.pmoradi.rest.entries.AddInEntry;
import com.pmoradi.rest.entries.AddOutEntry;
import com.pmoradi.security.Captcha;
import com.pmoradi.util.LinkUtil;

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
    private Engineering logic;

    @POST
    @Path("add")
    @Produces("text/json")
    public Response add(@Context HttpServletRequest requestContext, AddInEntry in){
        AddOutEntry out = new AddOutEntry(in);

        String ip = requestContext.getRemoteAddr();
        String word = in.getCaptcha();
        Captcha captcha = CAPTCHAS.get(ip);

        boolean error = false;
        if(in.getUrl().isEmpty()){
            out.setUrlError("The url can not be empty.");
            error = true;
        }
        if(!LinkUtil.validUrl(in.getUrl())){
            out.setUrl("Url contains illegal characters.");
            error = true;
        }
        if(in.getLink().isEmpty()) {
            out.setLinkError("The link can not be empty.");
            error = true;
        }
        if(captcha == null){
            out.setCaptchaError("Captcha has expired or was never requested.");
            error = true;
        } else if(!captcha.isCorrect(word)) {
            out.setCaptchaError("The captcha is incorrect.");
            error = true;
        }
        if(!in.getGroup().isEmpty() && in.getPassword().isEmpty() && 4 > in.getPassword().length()){
            out.setPasswordError("Password not acceptable");
            error = true;
        }
        if(!in.getGroup().isEmpty() && LinkUtil.validUrl(in.getGroup())){
            out.setGroupError("Group contains illegal characters");
            error = true;
        }
        if(!in.getGroup().isEmpty() && !in.getPassword().isEmpty() && !logic.validate(in.getGroup(), in.getPassword())){
            out.setGroupError("Group name and password mismatch.");
            error = true;
        }
        out.setCaptcha("");

        if(error)
            return Response.status(403).entity(out).build();

        try{
            if(in.getGroup().isEmpty())
                logic.addUrl(in.getUrl(), in.getLink());
            else
                logic.addUrl(in.getUrl(), in.getLink(), in.getGroup(), in.getPassword());
        }catch(Exception e){
            out.setUrlError("The URL is already used."); //Be more specific. The data base might be down. Or, somebody might took the group name before we had a chance to save.
            return Response.status(403).entity(out).build();
        }

        CAPTCHAS.remove(ip);
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
            for(byte b : logic.toBytes(captcha.create()))
                outputStream.write(b & 0xFF);
            outputStream.close();
        };

        return Response.ok(stream).build();
    }
}
