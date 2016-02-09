package com.pmoradi.rest;

import com.pmoradi.essentials.WebUtil;
import com.pmoradi.security.Role;
import com.pmoradi.system.AdminFacade;

import javax.inject.Inject;
import javax.security.auth.login.CredentialException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import java.io.IOException;

@Path("admin")
public class AdminResource {

    private static final String UNAUTHORIZED_MSG = "Unauthorized";

    @Inject
    private AdminFacade facade;
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
        try {
            facade.authenticate(username, password, Role.MAINTAINER);

            facade.deleteGroup(groupName);
            return Response.ok("Group <i>" + groupName + "</i> was successfully removed!").build();
        } catch (CredentialException e) {
            return wrongLogin(groupName, "", e.getMessage());
        } catch (NotFoundException e){
            e.printStackTrace();
            response.sendRedirect(request.getContextPath() + WebUtil.errorPage(404, "", groupName, "Can't delete a group that doesn't exist."));
            return Response.status(404).build();
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect(request.getContextPath() + WebUtil.errorPage(500, "", groupName, "Internal Error"));
            return Response.status(500).build();
        }
    }

    private Response wrongLogin(String groupName, String urlName, String msg) throws IOException {
        response.sendRedirect(request.getContextPath() + WebUtil.errorPage(401, urlName, groupName, msg));
        return Response.status(401).entity(msg).build();
    }
}
