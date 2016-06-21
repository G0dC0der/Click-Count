package com.pmoradi.test.integration.clients;

import com.pmoradi.rest.entries.AddInEntry;
import com.pmoradi.rest.entries.AddOutEntry;
import com.pmoradi.test.integration.rest.RestInfo;
import com.pmoradi.test.integration.rest.RestResponse;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

public class DataResourceClient {

    private String restUrl;
    private Client client;

    public DataResourceClient() {
        this(RestInfo.LOCAL_REST_URL);
    }

    public DataResourceClient(String restUrl) {
        this.restUrl = restUrl;
        this.client = ClientBuilder.newClient();
    }

    public RestResponse<String, AddOutEntry> add(AddInEntry entry) {
        Response resp = client.target(restUrl)
                .path("add")
                .request(MediaType.APPLICATION_JSON)
                .post(Entity.json(entry));

        return RestResponse.fromResponse(resp, String.class, AddOutEntry.class);
    }
}
