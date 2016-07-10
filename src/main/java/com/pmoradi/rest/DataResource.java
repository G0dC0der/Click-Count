package com.pmoradi.rest;

import com.pmoradi.essentials.UrlUnavailableException;
import com.pmoradi.essentials.WebUtil;
import com.pmoradi.rest.entries.AddInEntry;
import com.pmoradi.rest.entries.AddOutEntry;
import com.pmoradi.rest.entries.GenericMessage;
import com.pmoradi.system.Facade;

import javax.inject.Inject;
import javax.security.auth.login.CredentialException;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
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
    @Path("add")
    public Response addURL(AddInEntry in) {
        final AddOutEntry out = new AddOutEntry();
        String urlName = WebUtil.shrink(in.getUrlName());
        String link = WebUtil.shrink(in.getLink());
        String groupName = WebUtil.shrink(in.getGroupName());
        String password = in.getPassword() == null ? "" : in.getPassword();

        if (urlName.isEmpty()) {
            urlName = WebUtil.randomUrl();
        } else {
            if (WebUtil.isReserved(urlName)) {
                out.setUrlError("The url can not be equal to a reserved word.");
            } else if (!WebUtil.validUrl(urlName)) {
                out.setUrlError("URL contains illegal characters. Use A-Z a-z 0-9 .-_~");
            }
        }

        if (!groupName.isEmpty()) {
            if (WebUtil.isReserved(groupName)) {
                out.setGroupError("The group name is a reserved word.");
            } else if (!WebUtil.validUrl(groupName)) {
                out.setGroupError("The group name contains illegal characters. Use A-Z a-z 0-9 .-_~");
            }
        } else if (!password.isEmpty()) {
            out.setPasswordError("Password must be used along with group.");
        }

        if (link.isEmpty()) {
            out.setLinkError("The link may not be empty.");
        } else {
            if (!WebUtil.isProtocolBased(link)) {
                link = "http://" + link;
            }
            if (!WebUtil.exists(link)) {
                out.setLinkError("The link does not exist or responded with a non-ok status.");
            }
        }

        if (!out.containErrors()) {
            try {
                String url;
                if (groupName.isEmpty()) {
                    logic.addUrl(urlName, link);
                    url = logic.constructRedirectURL(urlName);

                } else {
                    logic.addUrl(urlName, link, groupName, password);
                    url = logic.constructRedirectURL(groupName, urlName);
                }
                return Response.ok(new GenericMessage(url)).build();
            } catch (UrlUnavailableException e) {
                out.setUrlError(e.getMessage());
            } catch (CredentialException e) {
                out.setGroupError(e.getMessage());
            }
        }

        return Response.status(Status.FORBIDDEN).entity(out).build();
    }
}