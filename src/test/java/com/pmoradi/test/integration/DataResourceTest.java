package com.pmoradi.test.integration;

import com.pmoradi.rest.entries.AddInEntry;
import com.pmoradi.rest.entries.AddOutEntry;
import com.pmoradi.rest.entries.GenericMessage;
import com.pmoradi.test.integration.clients.DataResourceClient;
import com.pmoradi.test.integration.clients.RedirectResourceClient;
import com.pmoradi.test.integration.rest.RestResponse;
import com.pmoradi.test.util.Randomization;
import org.junit.Before;
import org.junit.Test;

import javax.ws.rs.core.Response.Status;

import static org.junit.Assert.*;

public class DataResourceTest {

    private DataResourceClient dataClient;
    private RedirectResourceClient redirectClient;

    @Before
    public void setup() {
        dataClient = new DataResourceClient();
        redirectClient = new RedirectResourceClient();
    }

    @Test
    public void withGroupAndUrl() {
        AddInEntry entry = Randomization.randomDataEntry();
        RestResponse<GenericMessage, AddOutEntry> resp = dataClient.add(entry);

        assertTrue(resp.isOk());
        assertTrue(resp.successEntity.getMessage().endsWith(entry.getGroupName() + "/" + entry.getAlias()));
    }

    @Test
    public void reservedUrl() {
        AddInEntry entry = Randomization.randomDataEntry();
        entry.setAlias("default");

        RestResponse<GenericMessage, AddOutEntry> resp = dataClient.add(entry);
        assertTrue(resp.isClientError());
        assertFalse(resp.failEntity.getAliasError().isEmpty());
    }

    @Test
    public void illegalUrlChars() {
        AddInEntry entry = Randomization.randomDataEntry();
        entry.setAlias(Randomization.randomString() + "ö");

        RestResponse<GenericMessage, AddOutEntry> resp = dataClient.add(entry);
        assertTrue(resp.isClientError());
        assertFalse(resp.failEntity.getAliasError().isEmpty());
    }

    @Test
    public void reservedGroup() {
        AddInEntry entry = Randomization.randomDataEntry();
        entry.setGroupName("default");

        RestResponse<GenericMessage, AddOutEntry> resp = dataClient.add(entry);
        assertTrue(resp.isClientError());
        assertFalse(resp.failEntity.getGroupError().isEmpty());
    }

    @Test
    public void illegalGroupChars() {
        AddInEntry entry = Randomization.randomDataEntry();
        entry.setGroupName(Randomization.randomString() + "ö");

        RestResponse<GenericMessage, AddOutEntry> resp = dataClient.add(entry);
        assertTrue(resp.isClientError());
        assertFalse(resp.failEntity.getGroupError().isEmpty());
    }

    @Test
    public void passwordWithNoGroup() {
        AddInEntry entry = new AddInEntry();
        entry.setPassword("info");
        entry.setSourceUrl("google.se");
        entry.setAlias(Randomization.randomString());

        RestResponse<GenericMessage, AddOutEntry> resp = dataClient.add(entry);
        assertTrue(resp.isClientError());
        assertFalse(resp.failEntity.getPasswordError().isEmpty());
    }

    @Test
    public void emptyLink() {
        AddInEntry entry = new AddInEntry();
        entry.setGroupName(Randomization.randomString());
        entry.setAlias(Randomization.randomString());

        RestResponse<GenericMessage, AddOutEntry> resp = dataClient.add(entry);
        assertTrue(resp.isClientError());
        assertFalse(resp.failEntity.getSourceUrlError().isEmpty());
    }

    @Test
    public void redirectToTwitter() {
        AddInEntry entry = Randomization.randomDataEntry();
        entry.setSourceUrl("http://www.twitter.com");

        RestResponse<GenericMessage, AddOutEntry> resp = dataClient.add(entry);
        assertTrue(resp.isOk());
    }

    @Test
    public void redirectToTinyURL() {
        AddInEntry entry = Randomization.randomDataEntry();
        entry.setSourceUrl("http://tinyurl.com/dtrkv");

        RestResponse<GenericMessage, AddOutEntry> resp = dataClient.add(entry);
        assertTrue(resp.isOk());
    }

    @Test
    public void invalidLink() {
        AddInEntry entry = new AddInEntry();
        entry.setGroupName(Randomization.randomString());
        entry.setAlias(Randomization.randomString());
        entry.setSourceUrl(Randomization.randomLink());

        RestResponse<GenericMessage, AddOutEntry> resp = dataClient.add(entry);
        assertTrue(resp.isClientError());
        assertFalse(resp.failEntity.getSourceUrlError().isEmpty());
    }

//    @Test
//    public void seeOtherLink() {
//        AddInEntry dummyEntry = Randomization.randomDataEntry();
//        RestResponse<GenericMessage, AddOutEntry> addResp = dataClient.add(dummyEntry);
//        assertTrue(addResp.isOk());
//
//        AddInEntry entry = new AddInEntry();
//        entry.setGroupName(Randomization.randomString());
//        entry.setAlias(Randomization.randomString());
//        entry.setSourceUrl(addResp.successEntity.getMessage());
//
//        RestResponse<GenericMessage, AddOutEntry> resp = dataClient.add(entry);
//        assertTrue(resp.isClientError());
//        assertFalse(resp.failEntity.getSourceUrlError().isEmpty());
//    }

    @Test
    public void notFoundLink() {
        AddInEntry entry = new AddInEntry();
        entry.setGroupName(Randomization.randomString());
        entry.setAlias(Randomization.randomString());
        entry.setSourceUrl("http://google.com/i_dont_really_exists");

        RestResponse<GenericMessage, AddOutEntry> resp = dataClient.add(entry);
        assertTrue(resp.isClientError());
        assertFalse(resp.failEntity.getSourceUrlError().isEmpty());
    }

    @Test
    public void addUrlWithDefaultGroup() {
        AddInEntry entry = new AddInEntry();
        entry.setAlias(Randomization.randomString());
        entry.setSourceUrl("http://clickcount.se");

        RestResponse<GenericMessage, AddOutEntry> resp = dataClient.add(entry);
        assertTrue(resp.isOk());

        RestResponse<String, GenericMessage> resp2 = redirectClient.getSourceURL(entry.getAlias());
        assertEquals(Status.SEE_OTHER.getStatusCode(), resp2.statusCode);
        assertEquals(entry.getSourceUrl(), resp2.successEntity);
    }

    @Test
    public void addTakenUrlWithDefaultGroup() throws InterruptedException {
        AddInEntry entry = new AddInEntry();
        entry.setAlias(Randomization.randomString());
        entry.setSourceUrl("clickcount.se");

        RestResponse<GenericMessage, AddOutEntry> resp = dataClient.add(entry);
        assertTrue(resp.isOk());

        assertEquals(303, redirectClient.getSourceURL(entry.getAlias()).statusCode);

        resp = dataClient.add(entry);
        assertTrue(resp.isClientError());
        assertFalse(resp.failEntity.getAliasError().isEmpty());
    }

    @Test
    public void addWithGroup() {
        AddInEntry entry = Randomization.randomDataEntry();

        RestResponse<GenericMessage, AddOutEntry> resp = dataClient.add(entry);
        assertTrue(resp.isOk());

        assertEquals(303, redirectClient.getSourceURL(entry.getAlias(), entry.getGroupName()).statusCode);
    }

    @Test
    public void addWitGroupWrongPassword() {
        AddInEntry entry = Randomization.randomDataEntry();

        RestResponse<GenericMessage, AddOutEntry> resp = dataClient.add(entry);
        assertTrue(resp.isOk());

        assertEquals(303, redirectClient.getSourceURL(entry.getAlias(), entry.getGroupName()).statusCode);

        entry.setPassword(Randomization.randomLink());
        entry.setAlias(Randomization.randomString());

        resp = dataClient.add(entry);
        assertTrue(resp.isClientError());
        assertFalse(resp.failEntity.getGroupError().isEmpty());
    }

    @Test
    public void addWitGroupTakenUrl() {
        AddInEntry entry = Randomization.randomDataEntry();

        RestResponse<GenericMessage, AddOutEntry> resp = dataClient.add(entry);
        assertTrue(resp.isOk());

        resp = dataClient.add(entry);
        assertTrue(resp.isClientError());
        assertFalse(resp.failEntity.getAliasError().isEmpty());
    }
}
