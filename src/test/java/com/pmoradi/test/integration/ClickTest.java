package com.pmoradi.test.integration;

import com.pmoradi.rest.entries.*;
import com.pmoradi.test.integration.clients.DataResourceClient;
import com.pmoradi.test.integration.clients.RedirectResourceClient;
import com.pmoradi.test.integration.clients.ViewResourceClient;
import com.pmoradi.test.integration.rest.RestResponse;
import com.pmoradi.test.util.Parallelism;
import com.pmoradi.test.util.Randomization;
import org.junit.Before;
import org.junit.Test;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;

import static org.junit.Assert.*;

public class ClickTest {

    private DataResourceClient dataClient;
    private RedirectResourceClient redirectClient;
    private ViewResourceClient viewClient;

    @Before
    public void setup() {
        dataClient = new DataResourceClient();
        redirectClient = new RedirectResourceClient();
        viewClient = new ViewResourceClient();
    }

    @Test
    public void synchronousClicks() {
        final long expectedClicks = Randomization.randomInt(200) + 200;
        final AddInEntry entry = Randomization.randomDataEntry();

        RestResponse<String, AddOutEntry> addResp = dataClient.add(entry);
        assertTrue(addResp.isOk());
        assertFalse(addResp.successEntity.isEmpty());

        for (int i = 0; i < expectedClicks; i++) {
            RestResponse<String, GenericMessage> reResp = redirectClient.getLink(entry.getUrlName(), entry.getGroupName());
            assertTrue(reResp.isRedirection());
            assertFalse(reResp.successEntity.isEmpty());
        }

        GroupView groupView = new GroupView();
        groupView.setGroupName(entry.getGroupName());
        groupView.setPassword(entry.getPassword());

        RestResponse<GroupEntry, GenericMessage> viewResp = viewClient.viewAll(groupView);
        assertTrue(viewResp.isOk());
        assertEquals(expectedClicks, viewResp.successEntity.getUrls()[0].getClicks().longValue());
    }

    @Test
    public void asynchronousClicks() throws Throwable {
        final long expectedClicks = Randomization.randomInt(200) + 200;
        final AddInEntry entry = Randomization.randomDataEntry();

        RestResponse<String, AddOutEntry> addResp = dataClient.add(entry);
        assertTrue(addResp.isOk());
        assertFalse(addResp.successEntity.isEmpty());

        Parallelism.concurrentTasks((int)expectedClicks, ()-> {
            RestResponse<String, GenericMessage> reResp = redirectClient.getLink(entry.getUrlName(), entry.getGroupName());
            assertTrue(reResp.isRedirection());
            assertFalse(reResp.successEntity.isEmpty());
        });

        GroupView groupView = new GroupView();
        groupView.setGroupName(entry.getGroupName());
        groupView.setPassword(entry.getPassword());

        RestResponse<GroupEntry, GenericMessage> viewResp = viewClient.viewAll(groupView);
        assertTrue(viewResp.isOk());
        assertEquals(expectedClicks, viewResp.successEntity.getUrls()[0].getClicks().longValue());
    }
}
