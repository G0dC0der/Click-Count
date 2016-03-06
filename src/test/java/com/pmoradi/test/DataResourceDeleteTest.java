package com.pmoradi.test;

import com.pmoradi.rest.entries.DataEntry;
import com.pmoradi.rest.entries.DataOutEntry;
import com.pmoradi.rest.entries.UrlEditEntry;
import com.pmoradi.test.util.*;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class DataResourceDeleteTest {

    private DataResourceClient dataClient;
    private RedirectResourceClient redirectClient;

    @Before
    public void setup() {
        dataClient = new DataResourceClient(RestInfo.LOCAL_REST_URL);
        redirectClient = new RedirectResourceClient(RestInfo.LOCAL_REST_URL);
    }

    @Test
    public void deleteWithEmptyGroup() {
        RestResponse<String> resp = dataClient.delete(new UrlEditEntry());
        assertTrue(resp.isClientError());
    }

    @Test
    public void deleteWithDefaultGroup() {
        UrlEditEntry entry = Randomization.randomUrlEditEntry();
        entry.setGroupName("default");

        RestResponse<String> resp = dataClient.delete(entry);
        assertTrue(resp.isClientError());
    }

    @Test
    public void deleteWithNoUrl() {
        UrlEditEntry entry = Randomization.randomUrlEditEntry();
        entry.setUrlName("");

        RestResponse<String> resp = dataClient.delete(entry);
        assertTrue(resp.isClientError());
    }

    @Test
    public void deleteValidUrl() {
        DataEntry entry = Randomization.randomDataEntry();

        RestResponse<DataOutEntry> resp = dataClient.add(entry);
        assertTrue(resp.isOk());

        UrlEditEntry editEntry = new UrlEditEntry();
        editEntry.setGroupName(entry.getGroupName());
        editEntry.setPassword(entry.getPassword());
        editEntry.setUrlName(entry.getUrlName());

        RestResponse<String> resp2 = dataClient.delete(editEntry);
        assertTrue(resp2.isOk());

        RestResponse<String> linkResp = redirectClient.getLink(entry.getUrlName(), entry.getGroupName());
        assertEquals(linkResp.statusCode, 404);
    }

    @Test
    public void deleteInvalidUrl() {
        DataEntry entry = Randomization.randomDataEntry();

        RestResponse<DataOutEntry> resp = dataClient.add(entry);
        assertTrue(resp.isOk());

        UrlEditEntry editEntry = new UrlEditEntry();
        editEntry.setGroupName(entry.getGroupName());
        editEntry.setPassword(entry.getPassword());
        editEntry.setUrlName(Randomization.randomString());

        RestResponse<String> resp2 = dataClient.delete(editEntry);
        assertTrue(resp2.isClientError());
    }

    @Test
    public void deleteWithInvalidGroup() {
        UrlEditEntry entry = Randomization.randomUrlEditEntry();

        RestResponse<String> resp = dataClient.delete(entry);
        assertTrue(resp.isClientError());
    }

    @Test
    public void deleteWithInvalidPassword() {
        DataEntry entry = Randomization.randomDataEntry();

        RestResponse<DataOutEntry> resp = dataClient.add(entry);
        assertTrue(resp.isOk());

        UrlEditEntry editEntry = new UrlEditEntry();
        editEntry.setGroupName(entry.getGroupName());
        editEntry.setPassword("");
        editEntry.setUrlName(Randomization.randomString());

        RestResponse<String> resp2 = dataClient.delete(editEntry);
        assertTrue(resp2.isClientError());
    }
}
