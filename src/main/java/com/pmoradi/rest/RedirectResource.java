package com.pmoradi.rest;

import com.pmoradi.rest.entries.LinkOutEntry;
import com.pmoradi.system.Engineering;

import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;
import java.io.IOException;

@Path("/")
@Produces("text/json")
public class RedirectResource {

    @Inject
    private Engineering logic;

    @GET
    @Path("{group}/{url}")
    public Response redirect(@PathParam("group") String group,
                                     @PathParam("url") String url) throws ServletException, IOException {
        if(url == null){
            url = group;
            group = "default";
        }
        LinkOutEntry linkOut = new LinkOutEntry(group, url, logic.getLinkAndClick(group, url));
        return linkOut.getLink() == null ? Response.status(404).entity(linkOut).build() : Response.ok(linkOut).build();
    }
}
