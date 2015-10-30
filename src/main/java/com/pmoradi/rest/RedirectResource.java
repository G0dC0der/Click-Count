package com.pmoradi.rest;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

@Path("/")
@Produces("text/json")
public class RedirectResource {

    @GET
    @Path("{url}")
    public Response redirect(@PathParam("url") String url){
        Object resp = "lol";
        System.out.println("Redirect");
        return Response.ok(resp).build();
    }
}
