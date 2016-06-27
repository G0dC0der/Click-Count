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
    public void reservedUrl() {
        AddInEntry entry = Randomization.randomDataEntry();
        entry.setUrlName("default");

        RestResponse<GenericMessage, AddOutEntry> resp = dataClient.add(entry);
        assertTrue(resp.isClientError());
        assertFalse(resp.failEntity.getUrlError().isEmpty());
    }

    @Test
    public void illegalUrlChars() {
        AddInEntry entry = Randomization.randomDataEntry();
        entry.setUrlName(Randomization.randomString() + "ö");

        RestResponse<GenericMessage, AddOutEntry> resp = dataClient.add(entry);
        assertTrue(resp.isClientError());
        assertFalse(resp.failEntity.getUrlError().isEmpty());
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
        entry.setLink("google.se");
        entry.setUrlName(Randomization.randomString());

        RestResponse<GenericMessage, AddOutEntry> resp = dataClient.add(entry);
        assertTrue(resp.isClientError());
        assertFalse(resp.failEntity.getPasswordError().isEmpty());
    }

    @Test
    public void emptyLink() {
        AddInEntry entry = new AddInEntry();
        entry.setGroupName(Randomization.randomString());
        entry.setUrlName(Randomization.randomString());

        RestResponse<GenericMessage, AddOutEntry> resp = dataClient.add(entry);
        assertTrue(resp.isClientError());
        assertFalse(resp.failEntity.getLinkError().isEmpty());
    }

    @Test
    public void invalidLink() {
        AddInEntry entry = new AddInEntry();
        entry.setGroupName(Randomization.randomString());
        entry.setUrlName(Randomization.randomString());
        entry.setLink(Randomization.randomLink());

        RestResponse<GenericMessage, AddOutEntry> resp = dataClient.add(entry);
        assertTrue(resp.isClientError());
        assertFalse(resp.failEntity.getLinkError().isEmpty());
    }

    @Test
    public void addUrlWithDefaultGroup() {
        AddInEntry entry = new AddInEntry();
        entry.setUrlName(Randomization.randomString());
        entry.setLink("http://www.google.se");

        RestResponse<GenericMessage, AddOutEntry> resp = dataClient.add(entry);
        assertTrue(resp.isOk());

        RestResponse<String, GenericMessage> resp2 = redirectClient.getLink(entry.getUrlName());
        assertEquals(Status.SEE_OTHER.getStatusCode(), resp2.statusCode);
        assertEquals(entry.getLink(), resp2.successEntity);
    }

    @Test
    public void addTakenUrlWithDefaultGroup() throws InterruptedException {
        AddInEntry entry = new AddInEntry();
        entry.setUrlName(Randomization.randomString());
        entry.setLink("google.se");

        RestResponse<GenericMessage, AddOutEntry> resp = dataClient.add(entry);
        assertTrue(resp.isOk());

        assertEquals(303, redirectClient.getLink(entry.getUrlName()).statusCode);

        resp = dataClient.add(entry);
        assertTrue(resp.isClientError());
        assertFalse(resp.failEntity.getUrlError().isEmpty());
    }

    @Test
    public void addWithGroup() {
        AddInEntry entry = Randomization.randomDataEntry();

        RestResponse<GenericMessage, AddOutEntry> resp = dataClient.add(entry);
        assertTrue(resp.isOk());

        assertEquals(303, redirectClient.getLink(entry.getUrlName(), entry.getGroupName()).statusCode);
    }

    @Test
    public void addWitGroupWrongPassword() {
        AddInEntry entry = Randomization.randomDataEntry();

        RestResponse<GenericMessage, AddOutEntry> resp = dataClient.add(entry);
        assertTrue(resp.isOk());

        assertEquals(303, redirectClient.getLink(entry.getUrlName(), entry.getGroupName()).statusCode);

        entry.setPassword(Randomization.randomLink());
        entry.setUrlName(Randomization.randomString());

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
        assertFalse(resp.failEntity.getUrlError().isEmpty());
    }
}
