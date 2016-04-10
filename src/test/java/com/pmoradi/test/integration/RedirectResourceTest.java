package com.pmoradi.test.integration;

import com.pmoradi.rest.entries.AddInEntry;
import com.pmoradi.rest.entries.AddOutEntry;
import com.pmoradi.test.integration.clients.DataResourceClient;
import com.pmoradi.test.integration.clients.RedirectResourceClient;
import com.pmoradi.test.integration.rest.*;
import com.pmoradi.test.util.Randomization;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class RedirectResourceTest {

    private RedirectResourceClient redirectClient;
    private DataResourceClient dataClient;

    @Before
    public void setup() {
        dataClient = new DataResourceClient(RestInfo.LOCAL_REST_URL);
        redirectClient = new RedirectResourceClient(RestInfo.LOCAL_REST_URL);
    }

    @Test
    public void getValidLinkWithDefaultGroup() {
        AddInEntry entry = Randomization.randomDataEntry();

        RestResponse<AddOutEntry> resp = dataClient.add(entry);
        assertTrue(resp.isOk());

        RestResponse<String> linkResp = redirectClient.getLink(entry.getUrlName());
        assertTrue(linkResp.isRedirection());
        assertNotNull(linkResp.entity);
    }

    @Test
    public void getValidLinkWithRandomGroup() {
        AddInEntry entry = Randomization.randomDataEntry();

        RestResponse<AddOutEntry> resp = dataClient.add(entry);
        assertTrue(resp.isOk());

        RestResponse<String> linkResp = redirectClient.getLink(entry.getUrlName(), entry.getGroupName());
        assertTrue(linkResp.isRedirection());
        assertNotNull(linkResp.entity);
    }

    @Test
    public void getInvalidLinkWithDefaultGroup() {
        RestResponse<String> linkResp = redirectClient.getLink(Randomization.randomString());
        assertTrue(linkResp.isClientError());
        assertNull(linkResp.entity);
    }

    @Test
    public void getInvalidLinkWithRandomGroup() {
        RestResponse<String> linkResp = redirectClient.getLink(Randomization.randomString(), Randomization.randomString());
        assertTrue(linkResp.isClientError());
        assertNull(linkResp.entity);
    }
}
