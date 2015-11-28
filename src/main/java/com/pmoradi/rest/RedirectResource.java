package com.pmoradi.rest;

import com.pmoradi.essentials.Engineering;

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
    @Path("{url}")
    public void redirectUrl(@Context HttpServletRequest request,
                            @Context HttpServletResponse response,
                            @PathParam("url") String url) throws ServletException, IOException {
        String link = logic.getLink(url);
        if(link == null)
            link = "notfound.html?status=404&type=url&subject=" + url;

        response.sendRedirect(request.getContextPath() + "/" + link);
    }

    @GET
    @Path("{group}/{url}")
    public void redirectGroupUrl(@Context HttpServletRequest request,
                                 @Context HttpServletResponse response,
                                 @PathParam("url") String group,
                                 @PathParam("url") String url) throws ServletException, IOException {
        String link = logic.getLink(url);
        if(link == null)
            link = "notfound.html?status=404&type=url&subject=" + url;

        response.sendRedirect(request.getContextPath() + "/" + link);
    }
}
