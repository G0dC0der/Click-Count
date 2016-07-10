package com.pmoradi.test.integration;

import com.pmoradi.rest.entries.AddInEntry;
import com.pmoradi.rest.entries.GenericMessage;
import com.pmoradi.rest.entries.GroupEntry;
import com.pmoradi.test.integration.clients.AdminResourceClient;
import com.pmoradi.test.integration.clients.DataResourceClient;
import com.pmoradi.test.integration.clients.RedirectResourceClient;
import com.pmoradi.test.integration.rest.RestResponse;
import com.pmoradi.test.util.Randomization;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class AdminResourceTest {

    private AdminResourceClient adminClient;
    private DataResourceClient dataClient;
    private RedirectResourceClient redirectClient;

    @Before
    public void setup() {
        dataClient = new DataResourceClient();
        redirectClient = new RedirectResourceClient();
        adminClient = new AdminResourceClient();
        adminClient.setUsername("admin");
        adminClient.setPassword("1234");
    }

    //Role tests

    //Test the rest

    @Test
    public void denyOnWrongPassword() {
        AddInEntry entry = Randomization.randomDataEntry();
        assertTrue(dataClient.add(entry).isOk());

        adminClient.setPassword(Randomization.randomString());
        assertTrue(adminClient.viewGroup(entry.getGroupName()).isClientError());
    }

    @Test
    public void denyOnWrongUsername() {
        AddInEntry entry = Randomization.randomDataEntry();
        assertTrue(dataClient.add(entry).isOk());

        adminClient.setUsername(Randomization.randomString());
        assertTrue(adminClient.viewGroup(entry.getGroupName()).isClientError());
    }

    @Test
    public void viewGroup() {
        AddInEntry firstEntry = Randomization.randomDataEntry();
        AddInEntry secondEntry = new AddInEntry();
        secondEntry.setGroupName(firstEntry.getGroupName());
        secondEntry.setPassword(firstEntry.getPassword());
        secondEntry.setLink(firstEntry.getLink());
        secondEntry.setUrlName(Randomization.randomString());

        dataClient.add(firstEntry);
        dataClient.add(secondEntry);
        redirectClient.getLink(secondEntry.getUrlName(), secondEntry.getGroupName());

        RestResponse<GroupEntry, GenericMessage> resp = adminClient.viewGroup(firstEntry.getGroupName());
        assertTrue(resp.isOk());

        GroupEntry result = resp.successEntity;

        assertEquals(firstEntry.getGroupName(), result.getGroupName());
        assertEquals(2, result.getUrls().length);
        assertEquals(firstEntry.getUrlName(), result.getUrls()[0].getUrlName());
        assertEquals(secondEntry.getUrlName(), result.getUrls()[1].getUrlName());
        assertEquals(0L, result.getUrls()[0].getClicks().longValue());
        assertEquals(1L, result.getUrls()[1].getClicks().longValue());

    }
}
