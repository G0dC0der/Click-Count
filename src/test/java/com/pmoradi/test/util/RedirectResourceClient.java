package com.pmoradi.test.util;

import org.glassfish.jersey.client.ClientProperties;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

public class RedirectResourceClient {

    private String restUrl;
    private Client client;

    public RedirectResourceClient(String restUrl) {
        this.restUrl = restUrl;
        this.client = ClientBuilder.newClient();
    }

    public RestResponse<String> getLink(String url) {
        Response resp = client.target(restUrl)
                .path(url)
                .property(ClientProperties.FOLLOW_REDIRECTS, false)
                .request(MediaType.TEXT_PLAIN)
                .get();

        return getResponse(resp);
    }

    public RestResponse<String> getLink(String url, String group) {
        Response resp = client.target(restUrl)
                .path(group + "/" + url)
                .property(ClientProperties.FOLLOW_REDIRECTS, false)
                .request(MediaType.TEXT_PLAIN)
                .get();

        return getResponse(resp);
    }

    private RestResponse<String> getResponse(Response resp) {
        int statusCode = resp.getStatus();
        return new RestResponse<>(statusCode, statusCode >= 400 ? null : (String) resp.getHeaders().get("Location").get(0));
    }
}
