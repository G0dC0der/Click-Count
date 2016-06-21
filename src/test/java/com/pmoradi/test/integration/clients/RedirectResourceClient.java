package com.pmoradi.test.integration.clients;

import com.pmoradi.rest.entries.GenericMessage;
import com.pmoradi.test.integration.rest.RestInfo;
import com.pmoradi.test.integration.rest.RestResponse;
import org.glassfish.jersey.client.ClientProperties;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

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
                .request(MediaType.TEXT_PLAIN)
                .get();

        return RestResponse.fromResponse(resp, String.class, GenericMessage.class);
    }

    public RestResponse<String, GenericMessage> getLink(String url, String group) {
        Response resp = client.target(restUrl)
                .path(group + "/" + url)
                .property(ClientProperties.FOLLOW_REDIRECTS, false)
                .request(MediaType.TEXT_PLAIN)
                .get();

        return RestResponse.fromResponse(resp, String.class, GenericMessage.class);
    }
}
