package com.pmoradi.test;

import com.pmoradi.rest.entries.DataEntry;
import com.pmoradi.rest.entries.DataOutEntry;
import com.pmoradi.test.util.*;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class DataResourceAddTest {

    private DataResourceClient dataClient;
    private RedirectResourceClient redirectClient;

    @Before
    public void setup() {
        dataClient = new DataResourceClient(RestInfo.LOCAL_REST_URL);
        redirectClient = new RedirectResourceClient(RestInfo.LOCAL_REST_URL);
    }

    @Test
    public void serverGenerateUrl() {
        RestResponse<DataOutEntry> resp = dataClient.add(new DataEntry());
        assertTrue(!resp.entity.getUrlName().isEmpty());
    }

    @Test
    public void reservedUrl() {
        DataEntry entry = new DataEntry();
        entry.setUrlName("default");

        RestResponse<DataOutEntry> resp = dataClient.add(entry);
        assertTrue(resp.isClientError());
        assertFalse(resp.entity.getUrlError().isEmpty());
    }

    @Test
    public void illegalUrlChars() {
        DataEntry entry = new DataEntry();
        entry.setUrlName("/@");

        RestResponse<DataOutEntry> resp = dataClient.add(entry);
        assertTrue(resp.isClientError());
        assertFalse(resp.entity.getUrlError().isEmpty());
    }
    @Test
    public void reservedGroup() {
        DataEntry entry = new DataEntry();
        entry.setGroupName("default");

        RestResponse<DataOutEntry> resp = dataClient.add(entry);
        assertTrue(resp.isClientError());
        assertFalse(resp.entity.getGroupError().isEmpty());
    }

    @Test
    public void illegalGroupChars() {
        DataEntry entry = new DataEntry();
        entry.setGroupName("/");

        RestResponse<DataOutEntry> resp = dataClient.add(entry);
        assertTrue(resp.isClientError());
        assertFalse(resp.entity.getGroupError().isEmpty());
    }

    @Test
    public void passwordWithNoGroup() {
        DataEntry entry = new DataEntry();
        entry.setPassword("info");

        RestResponse<DataOutEntry> resp = dataClient.add(entry);
        assertTrue(resp.isClientError());
        assertFalse(resp.entity.getPasswordError().isEmpty());
    }

    @Test
    public void emptyLink() {
        RestResponse<DataOutEntry> resp = dataClient.add(new DataEntry());
        assertTrue(!resp.entity.getLinkError().isEmpty());
    }

    @Test
    public void addHttpToLink() {
        DataEntry entry = new DataEntry();
        entry.setLink("www.google.com");

        RestResponse<DataOutEntry> resp = dataClient.add(entry);
        assertTrue(resp.entity.getLink().startsWith("http://"));
    }

    @Test
    public void addDefaultUrl() {
        DataEntry entry = new DataEntry();
        entry.setUrlName(Randomization.randomString());
        entry.setLink(Randomization.randomLink());

        RestResponse<DataOutEntry> resp = dataClient.add(entry);
        assertTrue(resp.isOk());

        assertEquals(303, redirectClient.getLink(entry.getUrlName()).statusCode);
    }

    @Test
    public void addTakenDefaultUrl() {
        DataEntry entry = new DataEntry();
        entry.setUrlName(Randomization.randomString());
        entry.setLink(Randomization.randomLink());

        RestResponse<DataOutEntry> resp = dataClient.add(entry);
        assertTrue(resp.isOk());

        assertEquals(303, redirectClient.getLink(entry.getUrlName()).statusCode);

        resp = dataClient.add(entry);
        assertTrue(resp.isClientError());
        assertFalse(resp.entity.getUrlError().isEmpty());
    }

    @Test
    public void addWithGroup() {
        DataEntry entry = Randomization.randomDataEntry();

        RestResponse<DataOutEntry> resp = dataClient.add(entry);
        assertTrue(resp.isOk());

        assertEquals(303, redirectClient.getLink(entry.getUrlName(), entry.getGroupName()).statusCode);
    }

    @Test
    public void addWitGroupWrongPassword() {
        DataEntry entry = Randomization.randomDataEntry();

        RestResponse<DataOutEntry> resp = dataClient.add(entry);
        assertTrue(resp.isOk());

        assertEquals(303, redirectClient.getLink(entry.getUrlName(), entry.getGroupName()).statusCode);

        entry.setPassword(Randomization.randomLink());

        resp = dataClient.add(entry);
        assertTrue(resp.isClientError());
        assertFalse(resp.entity.getGroupError().isEmpty());
    }

    @Test
    public void addWitGroupTakenUrl() {
        DataEntry entry = Randomization.randomDataEntry();

        RestResponse<DataOutEntry> resp = dataClient.add(entry);
        assertTrue(resp.isOk());

        resp = dataClient.add(entry);
        assertTrue(resp.isClientError());
        assertFalse(resp.entity.getUrlError().isEmpty());
    }
}
