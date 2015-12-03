package com.pmoradi.rest;

import com.pmoradi.system.Inventory;

import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import java.io.IOException;

@Path("/")
@Produces("text/json")
public class RedirectResource {

    @Inject
    private Inventory logic;

    @GET
    @Path("{url}")
    public Response redirect(@Context HttpServletRequest request,
                             @Context HttpServletResponse response,
                             @PathParam("url") String urlName) throws ServletException, IOException {

        String link = logic.getLinkAndClick("default", urlName);
        if(link != null) {
            response.sendRedirect(link);
            return Response.ok().build();
        } else {
            response.sendRedirect(request.getContextPath() + String.format("error.html?status=%d&url=%s&group=default", 404, urlName));
            return Response.status(404).build();
        }
    }

    @GET
    @Path("{group}/{url}")
    public Response redirect(@Context HttpServletRequest request,
                             @Context HttpServletResponse response,
                             @PathParam("group") String groupName,
                             @PathParam("url") String urlName) throws ServletException, IOException {

        String link = logic.getLinkAndClick(groupName, urlName);
        if(link != null) {
            response.sendRedirect(link);
            return Response.ok().build();
        } else {
            response.sendRedirect(request.getContextPath() + String.format("error.html?status=%d&url=%s&group=%s", 404, urlName, groupName));
            return Response.status(404).build();
        }
    }
}
