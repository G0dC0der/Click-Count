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
        entry.setAlias(Randomization.randomString());
        entry.setSourceUrl("http://clickcount.se");

        RestResponse<GenericMessage, AddOutEntry> resp = dataClient.add(entry);
        assertTrue(resp.isOk());

        RestResponse<String, GenericMessage> linkResp = redirectClient.getSourceURL(entry.getAlias());
        assertTrue(linkResp.isRedirection());
        assertNotNull(linkResp.successEntity);
        assertEquals(entry.getSourceUrl(), linkResp.successEntity);
    }

    @Test
    public void validLinkWithRandomGroup() {
        AddInEntry entry = Randomization.randomDataEntry();

        RestResponse<GenericMessage, AddOutEntry> resp = dataClient.add(entry);
        assertTrue(resp.isOk());

        RestResponse<String, GenericMessage> linkResp = redirectClient.getSourceURL(entry.getAlias(), entry.getGroupName());
        assertTrue(linkResp.isRedirection());
        assertNotNull(linkResp.successEntity);
        assertEquals(entry.getSourceUrl(), linkResp.successEntity);
    }

    @Test
    public void invalidLinkWithDefaultGroup() {
        RestResponse<String, GenericMessage> linkResp = redirectClient.getSourceURL(Randomization.randomString());
        assertTrue(linkResp.isClientError());
    }

    @Test
    public void invalidLinkWithRandomGroup() {
        RestResponse<String, GenericMessage> linkResp = redirectClient.getSourceURL(Randomization.randomString(), Randomization.randomString());
        assertTrue(linkResp.isClientError());
    }

    @Test
    public void validGroupWithInvalidLink(){
        AddInEntry entry = Randomization.randomDataEntry();

        RestResponse<GenericMessage, AddOutEntry> resp = dataClient.add(entry);
        assertTrue(resp.isOk());

        RestResponse<String, GenericMessage> linkResp = redirectClient.getSourceURL(Randomization.randomString(), entry.getGroupName());
        assertTrue(linkResp.isClientError());
    }
}