package com.pmoradi.rest;

import com.pmoradi.essentials.EntryUtil;
import com.pmoradi.rest.entries.AddOutEntry;
import com.pmoradi.rest.entries.UrlEditEntry;
import com.pmoradi.system.Facade;
import com.pmoradi.essentials.UrlUnavailableException;
import com.pmoradi.rest.entries.AddInEntry;
import com.pmoradi.security.Captcha;
import com.pmoradi.essentials.WebUtil;

import javax.inject.Inject;
import javax.security.auth.login.CredentialException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.StreamingOutput;
import java.io.IOException;

@Path("/")
public class DataResource {

    @Inject
    private Facade logic;

    @POST
    @Path("add")
    @Produces(MediaType.APPLICATION_JSON)
    public Response add(@Context HttpServletResponse response,
                        @Context HttpServletRequest request,
                        AddInEntry in) throws IOException {
        EntryUtil.shrink(in);
        AddOutEntry errors = new AddOutEntry();
        boolean error = false;

        if(in.getUrlName().isEmpty()){
            in.setUrlName(WebUtil.randomUrl());
        } else {
            if(WebUtil.isReserved(in.getUrlName())) {
                errors.setUrlError("The url can not be equal to a reserved word.");
                error = true;
            } else if(!WebUtil.validUrl(in.getUrlName())) {
                errors.setUrlError("URL contains illegal characters. Use A-Z a-z 0-9 .-_~");
                error = true;
            }
        }

        if(!in.getGroupName().isEmpty()) {
            if(WebUtil.isReserved(in.getGroupName())) {
                errors.setGroupError("The group name is a reserved word.");
                error = true;
            } else if(!WebUtil.validUrl(in.getGroupName())) {
                errors.setGroupError("The group name contains illegal characters. Use A-Z a-z 0-9 .-_~");
                error = true;
            }
        } else if(!in.getPassword().isEmpty()) {
            errors.setPasswordError("Password must be used with group.");
            error = true;
        }

        if(in.getLink().isEmpty()) {
            errors.setLinkError("The link may not be empty.");
            error = true;
        } /*else if(in.getLink().contains(settings.getServerDomain()) || in.getLink().contains(settings.getServerIP())) {
            out.setLink("Referring to this website is disallowed.");
            error = true;
        } */
        in.setLink(WebUtil.addHttp(in.getLink()));

        if(!WebUtil.isLocalAddress(request.getRemoteAddr())) {
            Captcha captcha = (Captcha) request.getSession().getAttribute("captcha");
            request.getSession().removeAttribute("captcha");
            if(captcha == null){
                errors.setCaptchaError("Captcha was never requested or browser are not accepting cookies to be stored.");
                error = true;
            } else if(captcha.hasExpired()) {
                errors.setCaptchaError("Captcha has expired.");
                error = true;
            } else if(!captcha.isCorrect(in.getCaptcha())) {
                errors.setCaptchaError("Captcha is incorrect.");
                error = true;
            }
        }
        in.setCaptcha("");

        if(!error) {
            try {
                if(in.getGroupName().isEmpty())
                    logic.addUrl(in.getUrlName(), in.getLink());
                else
                    logic.addUrl(in.getUrlName(), in.getLink(), in.getGroupName(), in.getPassword());
            } catch (UrlUnavailableException e) {
                errors.setUrlError(e.getMessage());
                error = true;
            } catch (CredentialException e) {
                errors.setGroupError(e.getMessage());
                error = true;
            } catch (Exception e) {
                e.printStackTrace();
                return Response.serverError().build();
            }
        }

        return Response.status(error ? Status.FORBIDDEN : Status.OK).entity(errors).build();
    }

    @POST
    @Path("delete")
    public Response deleteUrl(UrlEditEntry in){
        EntryUtil.shrink(in);

        if(in.getGroupName().isEmpty())
            return Response.status(Status.FORBIDDEN).entity("Group name shall not be empty.").build();
        else if(in.getGroupName().equals("default"))
            return Response.status(Status.FORBIDDEN).entity("Group name can not be default.").build();
        else if(in.getUrlName().isEmpty())
            return Response.status(Status.FORBIDDEN).entity("Must specify a url to delete.").build();

        try {
            logic.deleteUrl(in.getGroupName(), in.getPassword(), in.getUrlName());
            return Response.ok().build();
        } catch (CredentialException | NotFoundException e) {
            return Response.status(Status.FORBIDDEN).entity(e.getMessage()).build();
        } catch (Exception e) {
            e.printStackTrace();
            return Response.serverError().entity("Internal Error").build();
        }
    }

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