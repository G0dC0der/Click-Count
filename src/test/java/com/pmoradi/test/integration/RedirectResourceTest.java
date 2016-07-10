package com.pmoradi.test.integration;

import com.pmoradi.rest.entries.AddInEntry;
import com.pmoradi.rest.entries.AddOutEntry;
import com.pmoradi.rest.entries.GenericMessage;
import com.pmoradi.test.integration.clients.DataResourceClient;
import com.pmoradi.test.integration.clients.RedirectResourceClient;
import com.pmoradi.test.integration.rest.RestResponse;
import com.pmoradi.test.util.Randomization;
import org.apache.commons.io.IOUtils;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class RedirectResourceTest {

    private RedirectResourceClient redirectClient;
    private DataResourceClient dataClient;

    @Before
    public void setup() {
        dataClient = new DataResourceClient();
        redirectClient = new RedirectResourceClient();
    }

    @Test
    public void validLinkWithDefaultGroup() {
        AddInEntry entry = new AddInEntry();
        entry.setUrlName(Randomization.randomString());
        entry.setLink("http://clickcount.se");

        RestResponse<GenericMessage, AddOutEntry> resp = dataClient.add(entry);
        assertTrue(resp.isOk());

        RestResponse<String, GenericMessage> linkResp = redirectClient.getLink(entry.getUrlName());
        assertTrue(linkResp.isRedirection());
        assertNotNull(linkResp.successEntity);
        assertEquals(entry.getLink(), linkResp.successEntity);
    }

    @Test
    public void validLinkWithRandomGroup() {
        AddInEntry entry = Randomization.randomDataEntry();

        RestResponse<GenericMessage, AddOutEntry> resp = dataClient.add(entry);
        assertTrue(resp.isOk());

        RestResponse<String, GenericMessage> linkResp = redirectClient.getLink(entry.getUrlName(), entry.getGroupName());
        assertTrue(linkResp.isRedirection());
        assertNotNull(linkResp.successEntity);
        assertEquals(entry.getLink(), linkResp.successEntity);
    }

    @Test
    public void invalidLinkWithDefaultGroup() {
        RestResponse<String, GenericMessage> linkResp = redirectClient.getLink(Randomization.randomString());
        assertTrue(linkResp.isClientError());
    }

    @Test
    public void invalidLinkWithRandomGroup() {
        RestResponse<String, GenericMessage> linkResp = redirectClient.getLink(Randomization.randomString(), Randomization.randomString());
        assertTrue(linkResp.isClientError());
    }

    @Test
    public void validGroupWithInvalidLink(){
        AddInEntry entry = Randomization.randomDataEntry();

        RestResponse<GenericMessage, AddOutEntry> resp = dataClient.add(entry);
        assertTrue(resp.isOk());

        RestResponse<String, GenericMessage> linkResp = redirectClient.getLink(Randomization.randomString(), entry.getGroupName());
        assertTrue(linkResp.isClientError());
    }
}