package com.pmoradi.rest;

import com.pmoradi.system.Facade;
import com.pmoradi.essentials.UrlUnavailableException;
import com.pmoradi.rest.entries.DataEntry;
import com.pmoradi.rest.entries.AddOutEntry;
import com.pmoradi.security.Captcha;
import com.pmoradi.essentials.WebUtil;

import javax.inject.Inject;
import javax.security.auth.login.CredentialException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.StreamingOutput;
import java.io.IOException;

@Path("/")
public class DataResource {

    @Inject
    private Facade logic;

    @POST
    @Path("add")
    @Produces("text/json")
    public Response add(@Context HttpServletResponse response,
                        @Context HttpServletRequest request,
                        DataEntry in) throws IOException {
        logic.fix(in);
        in.setLink(WebUtil.addHttp(in.getLink()));
        AddOutEntry out = new AddOutEntry(in);

        Captcha captcha = (Captcha) request.getSession().getAttribute("captcha");
        request.getSession().removeAttribute("captcha");
        out.setCaptcha("");

        if(in.getUrlName().isEmpty()){
            String randomUrl = WebUtil.randomUrl();
            out.setUrlName(randomUrl);
            in.setUrlName(randomUrl);
        }

        boolean error = false;
        if(WebUtil.isReserved(in.getUrlName())) {
            out.setUrlError("The url can not be equal to a reserved word.");
            error = true;
        } else if(!WebUtil.validUrl(in.getUrlName())) {
            out.setUrlError("URL contains illegal characters. Use A-Z a-z 0-9 .-_~");
            error = true;
        }

        if(!in.getGroupName().isEmpty()) {
            if(WebUtil.isReserved(in.getGroupName())) {
                out.setGroupError("The group name is a reserved word.");
                error = true;
            } else if(!WebUtil.validUrl(in.getGroupName())) {
                out.setGroupError("The group name contains illegal characters. Use A-Z a-z 0-9 .-_~");
                error = true;
            }
        } else if(!in.getPassword().isEmpty()) {
            out.setGroupError("Group can not be blank if password is used.");
            error = true;
        }

        if(in.getLink().isEmpty()) {
            out.setLinkError("The link may not be empty.");
            error = true;
        } /*else if(in.getLink().contains(settings.getServerDomain()) || in.getLink().contains(settings.getServerIP())) {
            out.setLink("Referring to this website is disallowed.");
            error = true;
        } */

        if(captcha == null){
            out.setCaptchaError("Captcha was never requested or browser are not accepting cookies to be stored.");
            error = true;
        } else if(captcha.hasExpired()) {
            out.setCaptchaError("Captcha has expired.");
            error = true;
        } else if(!captcha.isCorrect(in.getCaptcha())) {
            out.setCaptchaError("Captcha is incorrect.");
            error = true;
        }

        if(error)
            return Response.status(403).entity(out).build();

        try {
            if(in.getGroupName().isEmpty())
                logic.addUrl(in.getUrlName(), in.getLink());
            else
                logic.addUrl(in.getUrlName(), in.getLink(), in.getGroupName(), in.getPassword());
        } catch(UrlUnavailableException e) {
            out.setUrlError(e.getMessage());
            error = true;
        } catch (CredentialException e) {
            out.setGroupError(e.getMessage());
            error = true;
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect(request.getContextPath() + WebUtil.errorPage(500, "", "", "Internal Error."));
            return Response.status(500).build();
        }

        return error ?
                Response.status(403).entity(out).build() :
                Response.ok(out).build();
    }

    @POST
    @Path("delete")
    public Response delete(DataEntry in){
        logic.fix(in);

        if(in.getGroupName().isEmpty())
            return Response.status(403).entity("Group name shall not be empty.").build();
        else if(in.getGroupName().equals("default"))
            return Response.status(403).entity("Group name can not be default.").build();
        else if(in.getUrlName().isEmpty())
            return Response.status(403).entity("Must specify a url to delete.").build();

        try {
            logic.delete(in.getGroupName(), in.getPassword(), in.getUrlName());
            return Response.ok().build();
        } catch (CredentialException e) {
            return Response.status(403).entity(e.getMessage()).build();
        } catch (Exception e) {
            e.printStackTrace();
            return Response.status(500).entity("Internal Error").build();
        }
    }

    @GET
    @Path("captcha")
    @Produces("application/octet-stream")
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