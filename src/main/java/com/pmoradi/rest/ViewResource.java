package com.pmoradi.rest;

import com.pmoradi.rest.entries.ViewInEntry;
import com.pmoradi.system.Engineering;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;

@Path("/")
@Produces("text/json")
public class ViewResource {

    @Inject
    private Engineering logic;

    @POST
    @Path("view/all")
    public Response view(ViewInEntry in){
        return null;
    }

    @GET
    @Path("{url}/view")
    public Response viewSingle(@PathParam("url") String url){
        return null;
    }
}
