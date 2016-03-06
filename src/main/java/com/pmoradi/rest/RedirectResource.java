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
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.UriBuilder;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

@Path("/")
public class RedirectResource {

    @Inject
    private Facade logic;

    @GET
    @Path("{url}")
    public Response redirect(@Context HttpServletRequest request,
                             @Context HttpServletResponse response,
                             @PathParam("url") String urlName) throws ServletException, IOException, URISyntaxException {

        String link = logic.getLinkAndClick("default", urlName.toLowerCase());
        if(link != null) {
            return Response.seeOther(UriBuilder.fromPath(link).build()).build();
        } else {
            return Response.status(Status.NOT_FOUND).build();
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
            return Response.seeOther(UriBuilder.fromPath(link).build()).build();
        } else {
            return Response.status(Status.NOT_FOUND).build();
        }
    }
}
