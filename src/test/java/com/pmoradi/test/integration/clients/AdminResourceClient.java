package com.pmoradi.test.integration.clients;

import com.pmoradi.test.integration.rest.RestInfo;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;

public class AdminResourceClient { //TODO:

    private String restUrl;
    private Client client;

    public AdminResourceClient() {
        this(RestInfo.LOCAL_REST_URL);
    }

    public AdminResourceClient(String restUrl) {
        this.restUrl = restUrl;
        this.client = ClientBuilder.newClient();
    }
}
