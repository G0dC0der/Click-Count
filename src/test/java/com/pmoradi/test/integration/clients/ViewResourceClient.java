package com.pmoradi.test.integration.clients;

import com.pmoradi.rest.entries.GenericMessage;
import com.pmoradi.rest.entries.GroupEntry;
import com.pmoradi.rest.entries.GroupView;
import com.pmoradi.test.integration.rest.RestInfo;
import com.pmoradi.test.integration.rest.RestResponse;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

public class ViewResourceClient {

    private String restUrl;
    private Client client;

    public ViewResourceClient() {
        this(RestInfo.LOCAL_REST_URL);
    }

    public ViewResourceClient(String restUrl) {
        this.restUrl = restUrl;
        this.client = ClientBuilder.newClient();
    }

    public RestResponse<GroupEntry, GenericMessage> viewAll(GroupView entry) {
        Response resp = client.target(restUrl)
                .path("view")
                .path("all")
                .request(MediaType.APPLICATION_JSON)
                .post(Entity.entity(entry, MediaType.APPLICATION_JSON));

        return RestResponse.fromResponse(resp, GroupEntry.class, GenericMessage.class);
    }

    public RestResponse<GroupEntry, GenericMessage> viewSingle(String urlName) {
        Response resp = client.target(restUrl)
                .path(urlName)
                .path("view")
                .request(MediaType.APPLICATION_JSON)
                .get();

        return RestResponse.fromResponse(resp, GroupEntry.class, GenericMessage.class);
    }

    public RestResponse<GroupEntry, GenericMessage> viewSingle(String urlName, String groupName) {
        Response resp = client.target(restUrl)
                .path(groupName)
                .path(urlName)
                .path("view")
                .request(MediaType.APPLICATION_JSON)
                .get();

        return RestResponse.fromResponse(resp, GroupEntry.class, GenericMessage.class);
    }
}
