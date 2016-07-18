package com.pmoradi.rest;

import com.pmoradi.essentials.WebUtil;
import com.pmoradi.rest.entries.GenericMessage;
import com.pmoradi.system.Facade;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.UriBuilder;

@Path("/")
@Produces(MediaType.APPLICATION_JSON)
public class RedirectResource {

    @Inject
    private Facade logic;
    @Context
    private HttpServletRequest request;

    @GET
    @Path("{alias}")
    public Response redirect(@PathParam("alias") String alias) {
        alias = WebUtil.shrink(alias);
        String link = logic.getSourceUrlAndClick(request.getRemoteAddr(), "default", alias.toLowerCase());

        if(link != null) {
            return Response.seeOther(UriBuilder.fromPath(link).build()).build();
        } else {
            return Response.status(Status.NOT_FOUND).entity(new GenericMessage("URL not found: " + alias)).build();
        }
    }

    @GET
    @Path("{group}/{alias}")
    public Response redirect(@PathParam("group") String groupName,
                             @PathParam("alias") String alias) {
        groupName = WebUtil.shrink(groupName);
        alias = WebUtil.shrink(alias);
        String link = logic.getSourceUrlAndClick(request.getRemoteAddr(), groupName, alias);

        if(link != null) {
            return Response.seeOther(UriBuilder.fromPath(link).build()).build();
        } else {
            return Response.status(Status.NOT_FOUND).entity(new GenericMessage("URL not found: " + groupName + "/" + alias)).build();
        }
    }
}
