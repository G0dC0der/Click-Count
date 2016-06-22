package com.pmoradi.test.integration.clients;

import com.pmoradi.rest.entries.GenericMessage;
import com.pmoradi.rest.entries.GroupEntry;
import com.pmoradi.rest.entries.UserEntry;
import com.pmoradi.test.integration.rest.RestInfo;
import com.pmoradi.test.integration.rest.RestResponse;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

public class AdminResourceClient {

    private String restUrl, username, password;
    private Client client;

    public AdminResourceClient() {
        this(RestInfo.LOCAL_REST_URL);
    }

    public AdminResourceClient(String restUrl) {
        this.restUrl = restUrl;
        this.client = ClientBuilder.newClient();
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public RestResponse<GroupEntry, GenericMessage> viewGroup(String groupName) {
        Response resp = client.target(restUrl)
                .path("admin")
                .path("group")
                .path(groupName)
                .path("view")
                .request(MediaType.APPLICATION_JSON)
                .get();

        return RestResponse.fromResponse(resp, GroupEntry.class, GenericMessage.class);
    }

    public RestResponse<GenericMessage, GenericMessage> deleteGroup(String groupName) {
        Response resp = client.target(restUrl)
                .path("admin")
                .path("group")
                .path(groupName)
                .path("delete")
                .request(MediaType.APPLICATION_JSON)
                .delete();

        return RestResponse.fromResponse(resp, GenericMessage.class, GenericMessage.class);
    }

    public RestResponse<GenericMessage, GenericMessage> deleteUrl(String groupName, String urlName) {
        Response resp = client.target(restUrl)
                .path("admin")
                .path("group")
                .path(groupName)
                .path(urlName)
                .path("delete")
                .request(MediaType.APPLICATION_JSON)
                .delete();

        return RestResponse.fromResponse(resp, GenericMessage.class, GenericMessage.class);
    }

    public RestResponse<GenericMessage, GenericMessage> addUser(UserEntry userEntry) {
        Response resp = client.target(restUrl)
                .path("admin")
                .path("user")
                .path("add")
                .request(MediaType.APPLICATION_JSON)
                .post(Entity.entity(userEntry, MediaType.APPLICATION_JSON));

        return RestResponse.fromResponse(resp, GenericMessage.class, GenericMessage.class);
    }

    public RestResponse<GenericMessage, GenericMessage> changeRole(UserEntry userEntry) {
        Response resp = client.target(restUrl)
                .path("admin")
                .path("user")
                .path("role")
                .request(MediaType.APPLICATION_JSON)
                .post(Entity.entity(userEntry, MediaType.APPLICATION_JSON));

        return RestResponse.fromResponse(resp, GenericMessage.class, GenericMessage.class);
    }

    public RestResponse<GenericMessage, GenericMessage> removeUser(UserEntry userEntry) {
        Response resp = client.target(restUrl)
                .path("admin")
                .path("user")
                .path("delete")
                .request(MediaType.APPLICATION_JSON)
                .post(Entity.entity(userEntry, MediaType.APPLICATION_JSON));
        
        return RestResponse.fromResponse(resp, GenericMessage.class, GenericMessage.class);
    }
}
