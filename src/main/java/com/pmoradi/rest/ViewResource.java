package com.pmoradi.rest;

import com.pmoradi.rest.entries.*;
import com.pmoradi.security.Captcha;
import com.pmoradi.system.Facade;
import com.pmoradi.essentials.WebUtil;

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
import javax.ws.rs.core.Response;
import java.io.IOException;

@Path("/")
@Produces("text/json")
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
            return Response.status(403).entity(out).build();

        GroupEntry groupEntry = logic.getGroupData(in.getGroupName(), in.getPassword());
        if(groupEntry == null) {
            out.setGroupError("Group and password mismatch.");
            return Response.status(404).entity(out).build();
        } else {
            return Response.ok(groupEntry).build();
        }
    }

    @GET
    @Path("{url}/view")
    public void viewSingle(@Context HttpServletRequest request,
                           @Context HttpServletResponse response,
                           @PathParam("url") String urlName) throws IOException {

        UrlEntry urlData = logic.getUrlData("default", urlName);
        if(urlData == null) {
            response.sendRedirect(request.getContextPath() + WebUtil.errorPage(404, urlName, "default", "URL not found."));
        } else {
            ServletOutputStream out = response.getOutputStream();
            out.println("URL: " + urlData.getUrlName());
            out.println("Link: " + urlData.getLink());
            out.println("Clicks: " + urlData.getClicks().length);
            out.flush();
            out.close();
        }
    }

    @GET
    @Path("{group}/{url}/view")
    public void viewSingle(@Context HttpServletRequest request,
                           @Context HttpServletResponse response,
                           @PathParam("group") String groupName,
                           @PathParam("url") String urlName) throws IOException {

        UrlEntry urlData = logic.getUrlData(groupName, urlName);
        if(urlData == null) {
            response.sendRedirect(request.getContextPath() + WebUtil.errorPage(404, urlName, groupName, "URL not found."));
        } else {
            ServletOutputStream out = response.getOutputStream();
            out.println("URL: " + urlData.getUrlName());
            out.println("Link: " + urlData.getLink());
            out.println("Group: " + groupName);
            out.flush();
            out.close();
        }
    }
}
