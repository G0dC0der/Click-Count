package com.pmoradi.rest;

import com.pmoradi.system.Engineering;

import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import java.io.IOException;

@Path("/")
@Produces("text/json")
public class RedirectResource {

    @Inject
    private Engineering logic;

    @GET
    @Path("{group}/{url}")
    public void redirectGroupUrl(@Context HttpServletRequest request,
                                 @Context HttpServletResponse response,
                                 @PathParam("group") String group,
                                 @PathParam("url") String url) throws ServletException, IOException {
        if(url == null){
            url = group;
            group = "default";
        }
        String link = logic.getLinkAndClick(group, url);
        response.sendRedirect(link != null ? link : request.getContextPath() + "/" + String.format("error.html?status=404&url=%s&group=%s", url, group));
    }
}
