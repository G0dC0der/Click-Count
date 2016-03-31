package com.pmoradi.test;

import com.pmoradi.rest.entries.AddInEntry;
import com.pmoradi.rest.entries.AddOutEntry;
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
    public void reservedUrl() {
        AddInEntry entry = new AddInEntry();
        entry.setUrlName("default");

        RestResponse<AddOutEntry> resp = dataClient.add(entry);
        assertTrue(resp.isClientError());
        assertFalse(resp.entity.getUrlError().isEmpty());
    }

    @Test
    public void illegalUrlChars() {
        AddInEntry entry = new AddInEntry();
        entry.setUrlName("/@");

        RestResponse<AddOutEntry> resp = dataClient.add(entry);
        assertTrue(resp.isClientError());
        assertFalse(resp.entity.getUrlError().isEmpty());
    }
    @Test
    public void reservedGroup() {
        AddInEntry entry = new AddInEntry();
        entry.setGroupName("default");

        RestResponse<AddOutEntry> resp = dataClient.add(entry);
        assertTrue(resp.isClientError());
        assertFalse(resp.entity.getGroupError().isEmpty());
    }

    @Test
    public void illegalGroupChars() {
        AddInEntry entry = new AddInEntry();
        entry.setGroupName("/");

        RestResponse<AddOutEntry> resp = dataClient.add(entry);
        assertTrue(resp.isClientError());
        assertFalse(resp.entity.getGroupError().isEmpty());
    }

    @Test
    public void passwordWithNoGroup() {
        AddInEntry entry = new AddInEntry();
        entry.setPassword("info");

        RestResponse<AddOutEntry> resp = dataClient.add(entry);
        assertTrue(resp.isClientError());
        assertFalse(resp.entity.getPasswordError().isEmpty());
    }

    @Test
    public void emptyLink() {
        RestResponse<AddOutEntry> resp = dataClient.add(new AddInEntry());
        assertTrue(!resp.entity.getLinkError().isEmpty());
    }

    @Test
    public void addDefaultUrl() {
        AddInEntry entry = new AddInEntry();
        entry.setUrlName(Randomization.randomString());
        entry.setLink(Randomization.randomLink());

        RestResponse<AddOutEntry> resp = dataClient.add(entry);
        assertTrue(resp.isOk());

        assertEquals(303, redirectClient.getLink(entry.getUrlName()).statusCode);
    }

    @Test
    public void addTakenDefaultUrl() {
        AddInEntry entry = new AddInEntry();
        entry.setUrlName(Randomization.randomString());
        entry.setLink(Randomization.randomLink());

        RestResponse<AddOutEntry> resp = dataClient.add(entry);
        assertTrue(resp.isOk());

        assertEquals(303, redirectClient.getLink(entry.getUrlName()).statusCode);

        resp = dataClient.add(entry);
        assertTrue(resp.isClientError());
        assertFalse(resp.entity.getUrlError().isEmpty());
    }

    @Test
    public void addWithGroup() {
        AddInEntry entry = Randomization.randomDataEntry();

        RestResponse<AddOutEntry> resp = dataClient.add(entry);
        assertTrue(resp.isOk());

        assertEquals(303, redirectClient.getLink(entry.getUrlName(), entry.getGroupName()).statusCode);
    }

    @Test
    public void addWitGroupWrongPassword() {
        AddInEntry entry = Randomization.randomDataEntry();

        RestResponse<AddOutEntry> resp = dataClient.add(entry);
        assertTrue(resp.isOk());

        assertEquals(303, redirectClient.getLink(entry.getUrlName(), entry.getGroupName()).statusCode);

        entry.setPassword(Randomization.randomLink());

        resp = dataClient.add(entry);
        assertTrue(resp.isClientError());
        assertFalse(resp.entity.getGroupError().isEmpty());
    }

    @Test
    public void addWitGroupTakenUrl() {
        AddInEntry entry = Randomization.randomDataEntry();

        RestResponse<AddOutEntry> resp = dataClient.add(entry);
        assertTrue(resp.isOk());

        resp = dataClient.add(entry);
        assertTrue(resp.isClientError());
        assertFalse(resp.entity.getUrlError().isEmpty());
    }
}
