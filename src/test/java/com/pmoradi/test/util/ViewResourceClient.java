package com.pmoradi.test.util;

import com.pmoradi.rest.entries.DataOutEntry;
import com.pmoradi.rest.entries.GroupEntry;
import com.pmoradi.rest.entries.TotalEntry;
import com.pmoradi.rest.entries.ViewEntry;

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

    public RestResponse<TotalEntry> getTotalData() {
        Response resp = client.target(restUrl)
                .path("view/total")
                .request(MediaType.APPLICATION_JSON)
                .get();

        return getResponse(resp, TotalEntry.class);
    }

    public RestResponse<Object> viewAll(ViewEntry entry) {
        Response resp = client.target(restUrl)
                .path("view/all")
                .request(MediaType.APPLICATION_JSON)
                .post(Entity.entity(entry, MediaType.APPLICATION_JSON));

        int status = resp.getStatus();
        Class<?> clazz = resp.getStatus() == 200 ? GroupEntry.class : DataOutEntry.class;
        return new RestResponse<>(resp.getStatus(), resp.readEntity(clazz));
    }

    private <T> RestResponse<T> getResponse(Response resp, Class<T> clazz) {
        return new RestResponse<>(resp.getStatus(), resp.readEntity(clazz));
    }
}
