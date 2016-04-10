package com.pmoradi.rest;

import com.pmoradi.essentials.EntryUtil;
import com.pmoradi.essentials.UrlUnavailableException;
import com.pmoradi.essentials.WebUtil;
import com.pmoradi.rest.entries.AddInEntry;
import com.pmoradi.rest.entries.AddOutEntry;
import com.pmoradi.rest.entries.GenericMessage;
import com.pmoradi.rest.entries.UrlEditEntry;
import com.pmoradi.security.RequestInterval;
import com.pmoradi.system.Facade;

import javax.inject.Inject;
import javax.security.auth.login.CredentialException;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

@Path("/")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class DataResource {

    @Inject
    private Facade logic;

    @POST
    @RequestInterval(3000)
    @Path("add")
    public Response addURL(AddInEntry in) {
        EntryUtil.shrink(in);
        AddOutEntry out = new AddOutEntry();

        if(in.getUrlName().isEmpty()) {
            out.setUrlError("URL field must be valid");
        }else if(WebUtil.isReserved(in.getUrlName())) {
            out.setUrlError("The url can not be equal to a reserved word.");
        }else if(!WebUtil.validUrl(in.getUrlName())) {
            out.setUrlError("URL contains illegal characters. Use A-Z a-z 0-9 .-_~");
        }

        if(!in.getGroupName().isEmpty()) {
            if(WebUtil.isReserved(in.getGroupName())) {
                out.setGroupError("The group name is a reserved word.");
            } else if(!WebUtil.validUrl(in.getGroupName())) {
                out.setGroupError("The group name contains illegal characters. Use A-Z a-z 0-9 .-_~");
            }
        } else if(!in.getPassword().isEmpty()) {
            out.setPasswordError("Password must be used along with group.");
        }

        if(in.getLink().isEmpty()) {
            out.setLinkError("The link may not be empty.");
        } else {
            if(!WebUtil.protocolBased(in.getLink())) {
                in.setLink("http://" + in.getLink());
            }
            if(!WebUtil.exists(in.getLink())) {
                out.setLinkError("The given link is invalid. Please use verify that the link is type of either http, https, ftp or sftp and exists.");
            }
        }

        if(!out.containErrors()) {
            try {
                if(in.getGroupName().isEmpty())
                    logic.addUrl(in.getUrlName(), in.getLink());
                else
                    logic.addUrl(in.getUrlName(), in.getLink(), in.getGroupName(), in.getPassword());
            } catch (UrlUnavailableException e) {
                out.setUrlError(e.getMessage());
            } catch (CredentialException e) {
                out.setGroupError(e.getMessage());
            } catch (Exception e) {
                e.printStackTrace();
                return Response.serverError().build();
            }
        }

        return out.containErrors() ?
                Response.status(Status.FORBIDDEN).entity(out).build() :
                Response.ok(new GenericMessage("Success!")).build();
    }

    @POST
    @Path("delete")
    public Response deleteURL(UrlEditEntry in) {
        EntryUtil.shrink(in);

        if(in.getGroupName().isEmpty())
            return Response.status(Status.FORBIDDEN).entity(new GenericMessage("Group name shall not be empty.")).build();
        else if(in.getGroupName().equals("default"))
            return Response.status(Status.FORBIDDEN).entity(new GenericMessage("Group name can not be default.")).build();
        else if(in.getUrlName().isEmpty())
            return Response.status(Status.FORBIDDEN).entity(new GenericMessage("Must specify a url to delete.")).build();

        try {
            logic.deleteUrl(in.getGroupName(), in.getPassword(), in.getUrlName());
            return Response.ok(new GenericMessage("Success!")).build();
        } catch (CredentialException | NotFoundException e) {
            return Response.status(Status.FORBIDDEN).entity(new GenericMessage(e.getMessage())).build();
        } catch (Exception e) {
            e.printStackTrace();
            return Response.serverError().entity(new GenericMessage("Internal Error")).build();
        }
    }
}