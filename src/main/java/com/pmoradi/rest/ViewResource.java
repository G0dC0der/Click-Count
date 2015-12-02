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

    @GET
    @Path("view/all")
    public Response viewAll(ViewInEntry in) {
        return null;
    }

    @GET
    @Path("view/single/{url}")
    public Response viewSingle(@PathParam("url") String url){
        //Assumes group is default
        return null;
    }
}
