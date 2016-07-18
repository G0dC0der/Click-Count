package com.pmoradi.rest;

import com.pmoradi.essentials.WebUtil;
import com.pmoradi.rest.entries.GenericMessage;
import com.pmoradi.rest.entries.GroupEntry;
import com.pmoradi.rest.entries.GroupView;
import com.pmoradi.rest.entries.UrlEntry;
import com.pmoradi.system.Facade;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import java.io.IOException;

@Path("/")
@Produces(MediaType.APPLICATION_JSON)
public class ViewResource {

    @Inject
    private Facade logic;

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("view/all")
    public Response viewAll(GroupView in) throws IOException {
        String groupName = WebUtil.shrink(in.getGroupName());

        if(groupName.isEmpty()){
            return Response.status(Status.FORBIDDEN).entity(new GenericMessage("Group can not be empty")).build();
        } else if(groupName.equals("default")) {
            return Response.status(Status.FORBIDDEN).entity(new GenericMessage("Can not fetch data from the default group")).build();
        }

        GroupEntry groupEntry = logic.getGroupData(groupName, in.getPassword());
        if(groupEntry == null) {
            return Response.status(Status.NOT_FOUND).entity(new GenericMessage("Group and password mismatch.")).build();
        } else {
            return Response.ok(groupEntry).build();
        }
    }

    @GET
    @Path("{alias}/view")
    public Response viewSingle(@PathParam("alias") String alias) throws IOException {
        alias = WebUtil.shrink(alias);
        UrlEntry urlData = logic.getUrlData("default", alias);

        if(urlData == null) {
            return Response.status(Status.NOT_FOUND).entity(new GenericMessage("Alias not found: " + alias)).build();
        } else {
            GroupEntry groupEntry = new GroupEntry();
            groupEntry.setGroupName("default");
            groupEntry.setUrls(new UrlEntry[]{urlData});

            return Response.ok(groupEntry).build();
        }
    }

    @GET
    @Path("{group}/{alias}/view")
    public Response viewSingle(@PathParam("group") String groupName,
                               @PathParam("alias")   String alias) throws IOException {
        groupName = WebUtil.shrink(groupName);
        alias = WebUtil.shrink(alias);
        UrlEntry urlData = logic.getUrlData(groupName, alias);

        if(urlData == null) {
            return Response.status(Status.NOT_FOUND).entity(new GenericMessage("URL not found:" + groupName + "/" + alias)).build();
        } else {
            urlData.setClicks(null);

            GroupEntry groupEntry = new GroupEntry();
            groupEntry.setGroupName(groupName);
            groupEntry.setUrls(new UrlEntry[]{urlData});

            return Response.ok(groupEntry).build();
        }
    }
}
