package com.pmoradi.rest;

import com.pmoradi.security.Guarded;
import com.pmoradi.security.Role;
import com.pmoradi.system.AdminFacade;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.DELETE;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("admin")
@Produces(MediaType.TEXT_PLAIN)
public class AdminResource {

    @Inject
    private AdminFacade facade;
    @Context
    private HttpServletRequest request;
    @Context
    private HttpServletResponse response;

    @DELETE
    @Guarded(Role.MAINTAINER)
    @Path("delete/group/{groupName}")
    public Response deleteGroup(@PathParam("groupName") String groupName) {
        if(groupName.equals("default")) {
            return Response.status(403).entity("The default group can not be removed.").build();
        }

        try {
            facade.deleteGroup(groupName);
            return Response.ok("Group " + groupName + " was successfully removed!").build();
        } catch (NotFoundException e) {
            return Response.status(404).entity("Can't delete a group that doesn't exist.").build();
        } catch (Exception e) {
            e.printStackTrace();
            return Response.status(500).entity("Internal Error").build();
        }
    }
}
