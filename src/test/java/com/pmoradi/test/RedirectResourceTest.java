package com.pmoradi.test;

import com.pmoradi.rest.entries.DataEntry;
import com.pmoradi.rest.entries.DataOutEntry;
import com.pmoradi.test.util.*;
import org.junit.Assert;
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
        DataEntry entry = new DataEntry();
        entry.setLink(Randomization.randomLink());

        RestResponse<DataOutEntry> resp = dataClient.add(entry);
        assertTrue(resp.isOk());

        RestResponse<String> linkResp = redirectClient.getLink(resp.entity.getUrlName());
        assertTrue(linkResp.isRedirection());
        assertNotNull(linkResp.entity);
    }

    @Test
    public void getValidLinkWithRandomGroup() {
        DataEntry entry = Randomization.randomDataEntry();

        RestResponse<DataOutEntry> resp = dataClient.add(entry);
        assertTrue(resp.isOk());

        RestResponse<String> linkResp = redirectClient.getLink(resp.entity.getUrlName(), resp.entity.getGroupName());
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
