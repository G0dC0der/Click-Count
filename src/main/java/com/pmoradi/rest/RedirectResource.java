package com.pmoradi.rest;

import com.pmoradi.entities.URL;
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
        String link = logic.getLinkAndClick(url);
        response.sendRedirect(link != null ? link : request.getContextPath() + "/" + String.format("error.html?status=404&url=%s", url));
    }

    @GET
    @Path("{group}/{url}")
    public void redirectGroupUrl(@Context HttpServletRequest request,
                                 @Context HttpServletResponse response,
                                 @PathParam("group") String group,
                                 @PathParam("url") String url) throws ServletException, IOException {
        String link = logic.getLinkAndClick(group, url);
        response.sendRedirect(link != null ? link : request.getContextPath() + "/" + String.format("error.html?status=404&url=%s&group=%s", url, group));
    }
}
