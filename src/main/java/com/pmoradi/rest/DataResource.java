package com.pmoradi.rest;

import com.pmoradi.essentials.CachedMap;
import com.pmoradi.system.Inventory;
import com.pmoradi.essentials.UrlUnavailableException;
import com.pmoradi.rest.entries.DataEntry;
import com.pmoradi.rest.entries.AddOutEntry;
import com.pmoradi.security.Captcha;
import com.pmoradi.util.WebUtil;

import javax.inject.Inject;
import javax.security.auth.login.CredentialException;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.StreamingOutput;
import java.net.MalformedURLException;
import java.util.Map;

@Path("/")
public class DataResource {

    static final Map<String, Captcha> CAPTCHAS = CachedMap.getCachedMap(60000);

    @Inject
    private Inventory logic;

    @POST
    @Path("add")
    @Produces("text/json")
    public Response add(@Context HttpServletRequest request, DataEntry in){
        AddOutEntry out = new AddOutEntry(in);

        String ip = request.getRemoteAddr();
        String word = in.getCaptcha();
        Captcha captcha = CAPTCHAS.get(ip);

        boolean error = false;
        if(in.getUrlName().isEmpty()){
            out.setUrlError("The url can not be empty.");
            error = true;
        } else if(WebUtil.isReserved(in.getUrlName().toLowerCase())) {
            out.setUrlError("The url can not be equal to a reserved word.");
            error = true;
        }
        if(!in.getGroupName().isEmpty() && WebUtil.isReserved(in.getGroupName().toLowerCase())) {
            out.setGroupError("The group name can not be equal to a reserved word.");
            error = true;
        }
        if(in.getGroupName().isEmpty() && !in.getPassword().isEmpty()) {
            out.setGroupError("Group can not be blank if password is used.");
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
            out.setCaptchaError("Captcha is incorrect.");
            error = true;
        }

        out.setCaptcha("");
        CAPTCHAS.remove(ip);

        if(error)
            return Response.status(403).entity(out).build();

        try{
            if(in.getGroupName().isEmpty())
                logic.addUrl(in.getUrlName(), in.getLink());
            else
                logic.addUrl(in.getUrlName(), in.getLink(), in.getGroupName(), in.getPassword());
        } catch(UrlUnavailableException e){
            out.setUrlError(e.getMessage());
            error = true;
        } catch (MalformedURLException e){
            String msg = e.getMessage();
            if(msg.startsWith("Group"))
                out.setGroupError(msg);
            else if(msg.startsWith("URL"))
                out.setUrlError(msg);
            error = true;
        } catch (CredentialException e) {
            out.setGroupError(e.getMessage());
            error = true;
        }

        return error ?
                Response.status(403).entity(out).build() :
                Response.ok(out).build();
    }

    @POST
    @Path("delete")
    public Response delete(@Context HttpServletRequest requestContext, DataEntry in){
        return null;
    }

    @GET
    @Path("captcha")
    @Produces("application/octet-stream")
    public Response captcha(@Context HttpServletRequest requestContext) {
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