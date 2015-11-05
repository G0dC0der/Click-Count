package com.pmoradi.rest;

import com.pmoradi.rest.entries.AddInEntry;
import com.pmoradi.system.Engineering;

import javax.inject.Inject;
import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

@Path("/")
@Produces("text/json")
public class AddResource {

    @Inject
    private Engineering engineering;

    @POST
    @Path("add")
    public Response viewAll(AddInEntry entry/*@FormParam("url") String url,
                            @FormParam("link") String link,
                            @FormParam("group") String group,
                            @FormParam("password") String password,
                            @FormParam("captcha") String captcha*/){
        Object resp = "lol";

        return Response.ok(entry).build();
    }
}
