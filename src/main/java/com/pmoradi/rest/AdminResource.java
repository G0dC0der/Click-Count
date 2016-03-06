package com.pmoradi.rest;

import com.pmoradi.rest.entries.GroupEntry;
import com.pmoradi.rest.entries.UserEntry;
import com.pmoradi.security.Guarded;
import com.pmoradi.security.Role;
import com.pmoradi.system.AdminFacade;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

@Path("admin")
public class AdminResource {

    @Inject
    private AdminFacade adminFacade;

    @GET
    @Guarded(Role.TRUSTED)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("group/{groupName}/view")
    public Response viewGroup(@PathParam("groupName") String groupName) {
        try {
            GroupEntry data = adminFacade.viewGroupData(groupName);
            return Response.ok().entity(data).build();
        } catch (NotFoundException e) {
            return Response.status(Status.NOT_FOUND).entity("Group '" + groupName + "' was not found.").build();
        } catch (Exception e) {
            e.printStackTrace();
            return Response.serverError().build();
        }
    }

    @DELETE
    @Guarded(Role.MAINTAINER)
    @Produces(MediaType.TEXT_PLAIN)
    @Path("group/{groupName}/delete")
    public Response deleteGroup(@PathParam("groupName") String groupName) {
        if(groupName.equals("default")) {
            return Response.status(Status.FORBIDDEN).entity("The default group can not be removed.").build();
        }

        try {
            adminFacade.deleteGroup(groupName);
            return Response.ok("Group '" + groupName + "' was successfully removed!").build();
        } catch (NotFoundException e) {
            return Response.status(Status.NOT_FOUND).entity("Can't delete a group that doesn't exist.").build();
        } catch (Exception e) {
            e.printStackTrace();
            return Response.serverError().build();
        }
    }

    @DELETE
    @Guarded(Role.MAINTAINER)
    @Produces(MediaType.TEXT_PLAIN)
    @Path("url/{groupName}/{urlName}/delete")
    public Response deleteUrl(@PathParam("groupName") String groupName,
                              @PathParam("urlName") String urlName) {
        try {
            adminFacade.deleteURL(groupName, urlName);
            return Response.ok("URL '" + urlName + "' from group '" + groupName + "' was successfully removed!").build();
        } catch (NotFoundException e) {
            return Response.status(Status.NOT_FOUND).entity("Url or group doesn't exist.").build();
        } catch (Exception e) {
            e.printStackTrace();
            return Response.serverError().build();
        }
    }

    @PUT
    @Guarded(Role.ADMINISTRATOR)
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.TEXT_PLAIN)
    @Path("user/add")
    public Response addUser(UserEntry user) {
        try {
            adminFacade.addUser(user.getUsername(), user.getPassword(), user.getRole());
            return Response.ok("User '" + user.getUsername() + "' was successfully added to the database!").build();
        } catch (Exception e) {
            e.printStackTrace();
            return Response.serverError().build();
        }
    }

    @POST
    @Guarded(Role.ADMINISTRATOR)
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.TEXT_PLAIN)
    @Path("user/role")
    public Response changeRole(UserEntry user) {
        try {
            adminFacade.changeRole(user.getUsername(), user.getRole());
            return Response.ok("User '" + user.getUsername() + "' changed role to " + user.getRole() + "!").build();
        } catch (NotFoundException e){
            return Response.status(Status.NOT_FOUND).entity("The given user doesn't exist.").build();
        } catch (Exception e) {
            e.printStackTrace();
            return Response.serverError().build();
        }
    }

    @DELETE
    @Guarded(Role.ADMINISTRATOR)
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.TEXT_PLAIN)
    @Path("user/delete")
    public Response removeUser(UserEntry user) {
        try {
            adminFacade.removeUser(user.getUsername());
            return Response.ok("User '" + user.getUsername() + "' successfully removed!").build();
        } catch (NotFoundException e){
            return Response.status(Status.NOT_FOUND).entity("The given user doesn't exist.").build();
        } catch (Exception e) {
            e.printStackTrace();
            return Response.serverError().build();
        }
    }
}