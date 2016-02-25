package com.pmoradi.rest;

import com.pmoradi.system.Facade;
import com.pmoradi.essentials.WebUtil;

import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.IOException;

@Path("/")
@Produces(MediaType.APPLICATION_JSON)
public class RedirectResource {

    @Inject
    private Facade logic;

    @GET
    @Path("{url}")
    public Response redirect(@Context HttpServletRequest request,
                             @Context HttpServletResponse response,
                             @PathParam("url") String urlName) throws ServletException, IOException {

        String link = logic.getLinkAndClick("default", urlName.toLowerCase());
        if(link != null) {
            response.sendRedirect(link);
            return Response.ok().build();
        } else {
            response.sendRedirect(request.getContextPath() + WebUtil.errorPage(404, urlName, "default", "URL not found."));
            return Response.status(404).build();
        }
    }

    @GET
    @Path("{group}/{url}")
    public Response redirect(@Context HttpServletRequest request,
                             @Context HttpServletResponse response,
                             @PathParam("group") String groupName,
                             @PathParam("url") String urlName) throws ServletException, IOException {

        String link = logic.getLinkAndClick(groupName.toLowerCase(), urlName.toLowerCase());
        if(link != null) {
            response.sendRedirect(link);
            return Response.ok().build();
        } else {
            response.sendRedirect(request.getContextPath() + WebUtil.errorPage(404, urlName, groupName, "URL not found."));
            return Response.status(404).build();
        }
    }
}
