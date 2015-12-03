package com.pmoradi.rest;

import com.pmoradi.rest.entries.GroupEntry;
import com.pmoradi.rest.entries.UrlEntry;
import com.pmoradi.rest.entries.ViewInEntry;
import com.pmoradi.system.Inventory;
import com.pmoradi.util.WebUtil;

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
    private Inventory logic;

    @POST
    @Path("view/all")
    public Response view(@Context HttpServletRequest request,
                         @Context HttpServletResponse response,
                         ViewInEntry in) throws IOException {

        if(in.getGroupName().equals("default")) {
            response.sendRedirect(request.getContextPath() + WebUtil.errorPage(403, "", in.getGroupName(), "Can not get an overview of the default group."));
            return Response.status(403).build();
        }

        GroupEntry groupEntry = logic.getGroupData(in.getGroupName(), in.getPassword());
        if(groupEntry == null) {
            response.sendRedirect(request.getContextPath() + WebUtil.errorPage(404, "", in.getGroupName(), "Group name and password mismatch."));
            return Response.status(404).build();
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
            out.println("URL: " + urlData.getUrl());
            out.println("Link: " + urlData.getLink());
            out.println("Clicks: " + urlData.getClicks().length);
            out.flush();
            out.close();
        }
    }
}
