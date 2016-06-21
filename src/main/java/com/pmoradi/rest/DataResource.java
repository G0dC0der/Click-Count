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
import org.apache.commons.lang3.exception.ExceptionUtils;

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
    public Response addHiddenURL(AddInEntry in) {
        EntryUtil.shrink(in);
        AddOutEntry out = new AddOutEntry();

        if (in.getUrlName().isEmpty()) {
            in.setUrlName(WebUtil.randomUrl());
        } else {
            if (WebUtil.isReserved(in.getUrlName())) {
                out.setUrlError("The url can not be equal to a reserved word.");
            } else if (!WebUtil.validUrl(in.getUrlName())) {
                out.setUrlError("URL contains illegal characters. Use A-Z a-z 0-9 .-_~");
            }
        }

        if (!in.getGroupName().isEmpty()) {
            if (WebUtil.isReserved(in.getGroupName())) {
                out.setGroupError("The group name is a reserved word.");
            } else if (!WebUtil.validUrl(in.getGroupName())) {
                out.setGroupError("The group name contains illegal characters. Use A-Z a-z 0-9 .-_~");
            }
        } else if (!in.getPassword().isEmpty()) {
            out.setPasswordError("Password must be used along with group.");
        }

        if (in.getLink().isEmpty()) {
            out.setLinkError("The link may not be empty.");
        } else {
            if (!WebUtil.isProtocolBased(in.getLink())) {
                in.setLink("http://" + in.getLink());
            }
            if (!WebUtil.exists(in.getLink())) {
                out.setLinkError("The link does not exist or responded with a redirect status.");
            }
        }

        if (!out.containErrors()) {
            try {
                String url;
                if (in.getGroupName().isEmpty()) {
                    logic.addUrl(in.getUrlName(), in.getLink());
                    url = logic.constructRedirectURL(in.getUrlName());

                } else {
                    logic.addUrl(in.getUrlName(), in.getLink(), in.getGroupName(), in.getPassword());
                    url = logic.constructRedirectURL(in.getGroupName(), in.getUrlName());
                }
                return Response.ok(url).build();
            } catch (UrlUnavailableException e) {
                out.setUrlError(e.getMessage());
            } catch (CredentialException e) {
                out.setGroupError(e.getMessage());
            }
        }

        return Response.status(Status.FORBIDDEN).entity(out).build();
    }
}