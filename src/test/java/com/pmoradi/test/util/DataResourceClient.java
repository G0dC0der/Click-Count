package com.pmoradi.test.util;

import com.pmoradi.rest.entries.DataEntry;
import com.pmoradi.rest.entries.DataOutEntry;
import com.pmoradi.rest.entries.UrlEditEntry;

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

    public RestResponse<DataOutEntry> add(DataEntry entry) {
        Response resp = client.target(restUrl)
                .path("add")
                .request(MediaType.APPLICATION_JSON)
                .post(Entity.json(entry));

        return getResponse(resp, DataOutEntry.class);
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
