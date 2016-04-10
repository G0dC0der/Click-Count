package com.pmoradi.test.integration.clients;

import com.pmoradi.rest.entries.AddInEntry;
import com.pmoradi.rest.entries.AddOutEntry;
import com.pmoradi.rest.entries.UrlEditEntry;
import com.pmoradi.test.integration.rest.RestResponse;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

public class DataResourceClient {

    private String restUrl;
    private Client client;

    public DataResourceClient(String restUrl) {
        this.restUrl = restUrl;
        this.client = ClientBuilder.newClient();
    }

    public RestResponse<AddOutEntry> add(AddInEntry entry) {
        Response resp = client.target(restUrl)
                .path("add")
                .request(MediaType.APPLICATION_JSON)
                .post(Entity.json(entry));

        return getResponse(resp, AddOutEntry.class);
    }

    public RestResponse<String> delete(UrlEditEntry entry) {
        Response resp = client.target(restUrl)
                .path("delete")
                .request(MediaType.TEXT_PLAIN)
                .post(Entity.json(entry));

        return getResponse(resp, String.class);
    }

    private <T> RestResponse<T> getResponse(Response resp, Class<T> clazz) {
        return new RestResponse<>(resp.getStatus(), resp.readEntity(clazz));
    }
}
