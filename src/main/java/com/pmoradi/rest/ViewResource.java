package com.pmoradi.rest;

import com.pmoradi.essentials.EntryUtil;
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

    @POST
    @Path("view/all")
    public Response viewAll(@Context HttpServletRequest request, ViewEntry in) throws IOException {
        EntryUtil.shrink(in);

        if(in.getGroupName().isEmpty()){
            return Response.status(Status.FORBIDDEN).entity("Group can not be empty").build();
        } else if(in.getGroupName().equals("default")) {
            return Response.status(Status.FORBIDDEN).entity("Can not fetch data from the default group").build();
        }

        GroupEntry groupEntry = logic.getGroupData(in.getGroupName(), in.getPassword());
        if(groupEntry == null) {
            return Response.status(Status.NOT_FOUND).entity("Group and password mismatch.").build();
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
            return Response.status(Status.NOT_FOUND).build();
        } else {
            ServletOutputStream out = null;
            try{
                out = response.getOutputStream();
                out.println("URL: " + urlData.getUrlName());
                out.println("Link: " + urlData.getLink());
                out.println("Clicks: " + urlData.getClicks());
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
            return Response.status(Status.NOT_FOUND).build();
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
