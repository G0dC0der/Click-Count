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
        entry.setLink("google.se");

        RestResponse<GenericMessage, AddOutEntry> resp = dataClient.add(entry);
        assertTrue(resp.isOk());

        RestResponse<String, GenericMessage> linkResp = redirectClient.getLink(entry.getUrlName());
        assertTrue(linkResp.isRedirection());
        assertNotNull(linkResp.successEntity);
    }

    @Test
    public void validLinkWithRandomGroup() {
        AddInEntry entry = Randomization.randomDataEntry();

        RestResponse<GenericMessage, AddOutEntry> resp = dataClient.add(entry);
        assertTrue(resp.isOk());

        RestResponse<String, GenericMessage> linkResp = redirectClient.getLink(entry.getUrlName(), entry.getGroupName());
        assertTrue(linkResp.isRedirection());
        assertNotNull(linkResp.successEntity);
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
}