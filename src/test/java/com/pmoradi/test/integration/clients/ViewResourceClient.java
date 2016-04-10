package com.pmoradi.test.integration.clients;

import com.pmoradi.rest.entries.GroupEntry;
import com.pmoradi.rest.entries.ViewEntry;
import com.pmoradi.test.integration.rest.RestResponse;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

public class ViewResourceClient {

    private String restUrl;
    private Client client;

    public ViewResourceClient(String restUrl) {
        this.restUrl = restUrl;
        this.client = ClientBuilder.newClient();
    }

    public RestResponse<Object> viewAll(ViewEntry entry) {
        Response resp = client.target(restUrl)
                .path("view/all")
                .request(MediaType.APPLICATION_JSON)
                .post(Entity.entity(entry, MediaType.APPLICATION_JSON));

        int status = resp.getStatus();
        Class<?> clazz = resp.getStatus() == 200 ? GroupEntry.class : String.class;

        return new RestResponse<>(resp.getStatus(), resp.readEntity(clazz));
    }
}
