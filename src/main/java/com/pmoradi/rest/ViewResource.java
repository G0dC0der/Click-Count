package com.pmoradi.rest;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;

@Path("/")
@Produces("text/json")
public class ViewResource {

//    @GET
//    @Path("viewgroup")
//    public Response viewAll(@FormParam("group") String group,
//                            @FormParam("password") String password){
//        Object resp = "lol";
//
//        return Response.ok(resp).build();
//    }

    @GET
    @Path("{url}/view")
    public Response viewSingle(@PathParam("url") String url){
        Object resp = "lol";
        System.out.println("View Single");
        return Response.ok(resp).build();
    }
}
