package com.pmoradi.rest;

import com.pmoradi.rest.entries.GenericMessage;
import com.pmoradi.system.Facade;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.UriBuilder;

@Path("/")
@Produces(MediaType.APPLICATION_JSON)
public class RedirectResource {

    @Inject
    private Facade logic;

    @GET
    @Path("{url}")
    public Response redirect(@PathParam("url") String urlName) {

        String link = logic.getLinkAndClick("default", urlName.toLowerCase());
        if(link != null) {
            return Response.seeOther(UriBuilder.fromPath(link).build()).build();
        } else {
            return Response.status(Status.NOT_FOUND).entity(new GenericMessage("URL not found.")).build();
        }
    }

    @GET
    @Path("{group}/{url}")
    public Response redirect(@PathParam("group") String groupName,
                             @PathParam("url") String urlName) {

        String link = logic.getLinkAndClick(groupName.toLowerCase(), urlName.toLowerCase());
        if(link != null) {
            return Response.seeOther(UriBuilder.fromPath(link).build()).build();
        } else {
            return Response.status(Status.NOT_FOUND).entity(new GenericMessage("URL not found.")).build();
        }
    }
}
