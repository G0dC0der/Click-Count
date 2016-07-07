package com.pmoradi.test.integration.clients;

import com.pmoradi.rest.entries.GenericMessage;
import com.pmoradi.test.integration.rest.RestInfo;
import com.pmoradi.test.integration.rest.RestResponse;
import org.glassfish.jersey.client.ClientProperties;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status.Family;

public class RedirectResourceClient {

    private String restUrl;
    private Client client;

    public RedirectResourceClient() {
        this(RestInfo.LOCAL_REST_URL);
    }

    public RedirectResourceClient(String restUrl) {
        this.restUrl = restUrl;
        this.client = ClientBuilder.newClient();
    }

    public RestResponse<String, GenericMessage> getLink(String url) {
        Response resp = client.target(restUrl)
                .path(url)
                .property(ClientProperties.FOLLOW_REDIRECTS, false)
                .request(MediaType.APPLICATION_JSON)
                .get();

        return parse(resp);
    }

    public RestResponse<String, GenericMessage> getLink(String url, String group) {
        Response resp = client.target(restUrl)
                .path(group)
                .path(url)
                .property(ClientProperties.FOLLOW_REDIRECTS, false)
                .request(MediaType.APPLICATION_JSON)
                .get();

        return parse(resp);
    }

    private RestResponse<String, GenericMessage> parse(Response resp) {
        if (resp.getStatusInfo().getFamily() == Family.REDIRECTION)
            return new RestResponse<>(resp.getStatus(), resp.getLocation().toString(), null);
        else
            return new RestResponse<>(resp.getStatus(), null, resp.readEntity(GenericMessage.class));
    }
}
