package com.pmoradi.rest;

import com.pmoradi.essentials.WebUtil;
import com.pmoradi.system.AdminFacade;
import com.pmoradi.system.ApplicationSettings;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import java.io.IOException;

@Path("admin")
public class AdminResource {

    private static final String WRONG_LOGIN_MSG = "You are not authorized to perform this action.";

    @Inject
    private AdminFacade facade;
    @Inject
    private ApplicationSettings settings;
    @Context
    private HttpServletRequest request;
    @Context
    private HttpServletResponse response;

    @GET
    @Path("delete/group/{groupName}")
    @Produces("text/plain")
    public Response deleteGroup(@PathParam("groupName") String groupName,
                                @QueryParam("username") String username,
                                @QueryParam("password") String password) throws IOException {
        if(!settings.isAdmin(username, password))
            return wrongLogin(groupName, "");

        try {
            facade.deleteGroup(groupName);
            return Response.ok("Group <i>" + groupName + "</i> was successfully removed!").build();
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect(request.getContextPath() + WebUtil.errorPage(500, "", groupName, "Internal Error"));
            return Response.status(500).build();
        }
    }

    private Response wrongLogin(String groupName, String urlName) throws IOException {
        response.sendRedirect(request.getContextPath() + WebUtil.errorPage(401, urlName, groupName, WRONG_LOGIN_MSG));
        return Response.status(401).entity(WRONG_LOGIN_MSG).build();
    }
}
