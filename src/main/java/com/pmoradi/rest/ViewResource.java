package com.pmoradi.rest;

import com.pmoradi.rest.entries.*;
import com.pmoradi.security.Captcha;
import com.pmoradi.system.Facade;
import com.pmoradi.essentials.WebUtil;
import org.apache.commons.io.IOUtils;

import javax.inject.Inject;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import java.io.IOException;

@Path("/")
@Produces(MediaType.APPLICATION_JSON)
public class ViewResource {

    @Inject
    private Facade logic;

    @GET
    @Path("view/total")
    public Response totalData(){
        TotalEntry totalOut = new TotalEntry();
        totalOut.setTotalUrls(logic.totalURLs());
        totalOut.setTotalClicks(logic.totalClicks());

        return Response.ok(totalOut).build();
    }

    @POST
    @Path("view/all")
    public Response viewAll(@Context HttpServletRequest request, ViewEntry in) throws IOException {
        String word = in.getCaptcha();
        Captcha captcha = (Captcha) request.getSession().getAttribute("captcha");
        request.getSession().removeAttribute("captcha");

        logic.fix(in);
        AddOutEntry out = new AddOutEntry();
        out.setGroupName(in.getGroupName());
        out.setPassword(in.getPassword());

        boolean error = false;

        if(captcha == null){
            out.setCaptchaError("Captcha has expired or was never requested.");
            error = true;
        } else if(!captcha.isCorrect(word)) {
            out.setCaptchaError("Captcha is incorrect.");
            error = true;
        }
        if(in.getGroupName().isEmpty()){
            out.setGroupError("Group can not be empty");
            error = true;
        } else if(in.getGroupName().equals("default")) {
            out.setGroupError("Can not fetch data from the default group");
            error = true;
        }

        if(error)
            return Response.status(Status.FORBIDDEN).entity(out).build();

        GroupEntry groupEntry = logic.getGroupData(in.getGroupName(), in.getPassword());
        if(groupEntry == null) {
            out.setGroupError("Group and password mismatch.");
            return Response.status(Status.NOT_FOUND).entity(out).build();
        } else {
            return Response.ok(groupEntry).build();
        }
    }

    @GET
    @Path("{url}/view")
    public Response viewSingle(@Context HttpServletRequest request,
                           @Context HttpServletResponse response,
                           @PathParam("url") String urlName) throws IOException {

        UrlEntry urlData = logic.getUrlData("default", urlName.trim().toLowerCase());
        if(urlData == null) {
            response.sendRedirect(request.getContextPath() + WebUtil.errorPage(Status.NOT_FOUND, urlName, "default", "URL not found."));
            return Response.status(Status.NOT_FOUND).entity("URL not found.").build();
        } else {
            ServletOutputStream out = null;
            try{
                out = response.getOutputStream();
                out.println("URL: " + urlData.getUrlName());
                out.println("Link: " + urlData.getLink());
                out.println("Clicks: " + urlData.getClicks().length);
                return Response.ok().build();
            } finally {
                IOUtils.closeQuietly(out);
            }
        }
    }

    @GET
    @Path("{group}/{url}/view")
    public Response viewSingle(@Context HttpServletRequest request,
                           @Context HttpServletResponse response,
                           @PathParam("group") String groupName,
                           @PathParam("url") String urlName) throws IOException {

        UrlEntry urlData = logic.getUrlData(groupName.toLowerCase(), urlName.toLowerCase());
        if(urlData == null) {
            response.sendRedirect(request.getContextPath() + WebUtil.errorPage(Status.NOT_FOUND, urlName, groupName, "URL not found."));
            return Response.status(Status.NOT_FOUND).entity("URL not found.").build();
        } else {
            ServletOutputStream out = null;
            try {
                out = response.getOutputStream();
                out.println("URL: " + urlData.getUrlName());
                out.println("Link: " + urlData.getLink());
                out.println("Group: " + groupName);
                return Response.ok().build();
            } finally {
                IOUtils.closeQuietly(out);
            }
        }
    }
}
