package com.pmoradi.test.integration;

import com.pmoradi.rest.entries.AddInEntry;
import com.pmoradi.rest.entries.GenericMessage;
import com.pmoradi.rest.entries.GroupEntry;
import com.pmoradi.rest.entries.UserEntry;
import com.pmoradi.security.Role;
import com.pmoradi.test.integration.clients.AdminResourceClient;
import com.pmoradi.test.integration.clients.DataResourceClient;
import com.pmoradi.test.integration.clients.RedirectResourceClient;
import com.pmoradi.test.integration.rest.RestResponse;
import com.pmoradi.test.util.Equality;
import com.pmoradi.test.util.Randomization;
import org.junit.Before;
import org.junit.Test;

import javax.ws.rs.core.Response.Status;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class AdminResourceTest {

    private AdminResourceClient adminClient, unauthenticatedAdminClient;
    private DataResourceClient dataClient;
    private RedirectResourceClient redirectClient;

    @Before
    public void setup() {
        dataClient = new DataResourceClient();
        redirectClient = new RedirectResourceClient();
        adminClient = new AdminResourceClient();
        adminClient.setUsername("admin");
        adminClient.setPassword("Pojahn1092");

        unauthenticatedAdminClient = new AdminResourceClient();
    }

    @Test
    public void abortAdminTaskForMaintainer(){
        unauthenticatedAdminClient.setUsername("maintainer");
        unauthenticatedAdminClient.setPassword("Pojahn1092");

        UserEntry newUser = new UserEntry();
        newUser.setUsername(Randomization.randomString());
        newUser.setPassword(Randomization.randomString());
        newUser.setRole(Role.ADMINISTRATOR);
        RestResponse<GenericMessage, GenericMessage> resp = unauthenticatedAdminClient.addUser(newUser);

        assertEquals(Status.UNAUTHORIZED.getStatusCode(), resp.statusCode);
    }

    @Test
    public void abortMaintainerTaskForTrusted(){
        unauthenticatedAdminClient.setUsername("trusted");
        unauthenticatedAdminClient.setPassword("Pojahn1092");

        AddInEntry addInEntry = Randomization.randomDataEntry();
        assertTrue(dataClient.add(addInEntry).isOk());

        RestResponse<GenericMessage, GenericMessage> resp = unauthenticatedAdminClient.deleteUrl(addInEntry.getGroupName(), addInEntry.getAlias());
        assertEquals(Status.UNAUTHORIZED.getStatusCode(), resp.statusCode);
    }

    @Test
    public void abortAdminTaskForTrusted(){
        unauthenticatedAdminClient.setUsername("trusted");
        unauthenticatedAdminClient.setPassword("Pojahn1092");

        UserEntry newUser = new UserEntry();
        newUser.setUsername(Randomization.randomString());
        newUser.setPassword(Randomization.randomString());
        newUser.setRole(Role.ADMINISTRATOR);
        RestResponse<GenericMessage, GenericMessage> resp = unauthenticatedAdminClient.addUser(newUser);

        assertEquals(Status.UNAUTHORIZED.getStatusCode(), resp.statusCode);
    }

    @Test
    public void denyOnWrongPassword() {
        AddInEntry entry = Randomization.randomDataEntry();
        assertTrue(dataClient.add(entry).isOk());

        adminClient.setPassword(Randomization.randomString());
        assertTrue(adminClient.viewGroup(entry.getGroupName()).isClientError());
    }

    @Test
    public void denyOnWrongUsername() {
        AddInEntry entry = Randomization.randomDataEntry();
        assertTrue(dataClient.add(entry).isOk());

        adminClient.setUsername(Randomization.randomString());
        assertTrue(adminClient.viewGroup(entry.getGroupName()).isClientError());
    }

    @Test
    public void denyOnMissingCredentials(){
        UserEntry newUser = new UserEntry();
        newUser.setUsername(Randomization.randomString());
        newUser.setPassword(Randomization.randomString());
        newUser.setRole(Role.ADMINISTRATOR);
        RestResponse<GenericMessage, GenericMessage> resp = unauthenticatedAdminClient.addUser(newUser);

        assertEquals(Status.FORBIDDEN.getStatusCode(), resp.statusCode);
    }

    @Test
    public void viewGroup() {
        AddInEntry firstEntry = Randomization.randomDataEntry();
        AddInEntry secondEntry = new AddInEntry();
        secondEntry.setGroupName(firstEntry.getGroupName());
        secondEntry.setPassword(firstEntry.getPassword());
        secondEntry.setSourceUrl(firstEntry.getSourceUrl());
        secondEntry.setAlias(Randomization.randomString());

        dataClient.add(firstEntry);
        dataClient.add(secondEntry);
        redirectClient.getSourceURL(secondEntry.getAlias(), secondEntry.getGroupName());

        RestResponse<GroupEntry, GenericMessage> resp = adminClient.viewGroup(firstEntry.getGroupName());
        assertTrue(resp.isOk());

        GroupEntry result = resp.successEntity;

        assertEquals(firstEntry.getGroupName(), result.getGroupName());
        assertEquals(2, result.getUrls().length);
        assertEquals(firstEntry.getAlias(), result.getUrls()[0].getAlias());
        assertEquals(secondEntry.getAlias(), result.getUrls()[1].getAlias());
        assertEquals(0L, result.getUrls()[0].getClicks().longValue());
        assertEquals(1L, result.getUrls()[1].getClicks().longValue());
    }

    @Test
    public void deleteGroup(){
        AddInEntry entry = Randomization.randomDataEntry();
        assertTrue(dataClient.add(entry).isOk());

        AddInEntry entry2 = new AddInEntry();
        entry2.setGroupName(entry.getGroupName());
        entry2.setPassword(entry.getPassword());
        entry2.setAlias(Randomization.randomString());
        entry2.setSourceUrl("clickcount.se");
        assertTrue(dataClient.add(entry2).isOk());

        RestResponse<GenericMessage, GenericMessage> resp = adminClient.deleteGroup(entry.getGroupName());
        assertTrue(resp.isOk());

        assertEquals(Status.NOT_FOUND.getStatusCode(), redirectClient.getSourceURL(entry.getAlias(), entry.getGroupName()).statusCode);
        assertEquals(Status.NOT_FOUND.getStatusCode(), redirectClient.getSourceURL(entry2.getAlias(), entry2.getGroupName()).statusCode);
    }

    @Test
    public void deleteUrl(){
        AddInEntry entry = Randomization.randomDataEntry();
        assertTrue(dataClient.add(entry).isOk());

        AddInEntry entry2 = new AddInEntry();
        entry2.setGroupName(entry.getGroupName());
        entry2.setPassword(entry.getPassword());
        entry2.setAlias(Randomization.randomString());
        entry2.setSourceUrl("clickcount.se");
        assertTrue(dataClient.add(entry2).isOk());

        RestResponse<GenericMessage, GenericMessage> resp = adminClient.deleteUrl(entry.getGroupName(), entry.getAlias());
        assertTrue(resp.isOk());

        assertEquals(Status.NOT_FOUND.getStatusCode(), redirectClient.getSourceURL(entry.getAlias(), entry.getGroupName()).statusCode);
        assertTrue(redirectClient.getSourceURL(entry2.getAlias(), entry2.getGroupName()).isRedirection());
    }

    @Test
    public void addUserWithoutRole(){
        UserEntry newUser = new UserEntry();
        newUser.setUsername(Randomization.randomString());
        newUser.setPassword(Randomization.randomString());
        RestResponse<GenericMessage, GenericMessage> resp = adminClient.addUser(newUser);

        assertTrue(resp.isServerError());
    }

    @Test
    public void addUser(){
        UserEntry newUser = new UserEntry();
        newUser.setUsername(Randomization.randomString());
        newUser.setPassword(Randomization.randomString());
        newUser.setRole(Role.ADMINISTRATOR);

        RestResponse<GenericMessage, GenericMessage> resp = adminClient.addUser(newUser);
        assertTrue(resp.isOk());

        AddInEntry entry = Randomization.randomDataEntry();
        assertTrue(dataClient.add(entry).isOk());

        unauthenticatedAdminClient.setUsername(newUser.getUsername());
        unauthenticatedAdminClient.setPassword(newUser.getPassword());

        RestResponse<GroupEntry, GenericMessage> resp2 = unauthenticatedAdminClient.viewGroup(entry.getGroupName());
        assertTrue(resp2.isOk());
        assertTrue(Equality.equals(resp2.successEntity, entry));
    }

    @Test
    public void changeRole(){
        UserEntry newUser = new UserEntry();
        newUser.setUsername(Randomization.randomString());
        newUser.setPassword(Randomization.randomString());
        newUser.setRole(Role.MAINTAINER);

        RestResponse<GenericMessage, GenericMessage> resp = adminClient.addUser(newUser);
        assertTrue(resp.isOk());

        AddInEntry entry = Randomization.randomDataEntry();
        assertTrue(dataClient.add(entry).isOk());

        unauthenticatedAdminClient.setUsername(newUser.getUsername());
        unauthenticatedAdminClient.setPassword(newUser.getPassword());
        assertTrue(unauthenticatedAdminClient.deleteGroup(entry.getGroupName()).isOk());

        newUser.setRole(Role.TRUSTED);
        assertTrue(adminClient.changeRole(newUser).isOk());
        entry = Randomization.randomDataEntry();
        assertTrue(dataClient.add(entry).isOk());
        assertFalse(unauthenticatedAdminClient.deleteGroup(entry.getGroupName()).isOk());

    }

    @Test
    public void removeUser(){
        UserEntry newUser = new UserEntry();
        newUser.setUsername(Randomization.randomString());
        newUser.setPassword(Randomization.randomString());
        newUser.setRole(Role.MAINTAINER);

        RestResponse<GenericMessage, GenericMessage> resp = adminClient.addUser(newUser);
        assertTrue(resp.isOk());

        AddInEntry entry = Randomization.randomDataEntry();
        assertTrue(dataClient.add(entry).isOk());

        unauthenticatedAdminClient.setUsername(newUser.getUsername());
        unauthenticatedAdminClient.setPassword(newUser.getPassword());
        assertTrue(unauthenticatedAdminClient.deleteGroup(entry.getGroupName()).isOk());

        assertTrue(adminClient.removeUser(newUser).isOk());
        entry = Randomization.randomDataEntry();
        assertTrue(dataClient.add(entry).isOk());
        assertFalse(unauthenticatedAdminClient.deleteGroup(entry.getGroupName()).isOk());
    }
}
