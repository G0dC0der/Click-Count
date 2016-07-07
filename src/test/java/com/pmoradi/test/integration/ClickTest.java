package com.pmoradi.test.integration;

import com.pmoradi.essentials.Loop;
import com.pmoradi.rest.entries.AddInEntry;
import com.pmoradi.rest.entries.GenericMessage;
import com.pmoradi.rest.entries.GroupEntry;
import com.pmoradi.rest.entries.GroupView;
import com.pmoradi.test.integration.clients.DataResourceClient;
import com.pmoradi.test.integration.clients.RedirectResourceClient;
import com.pmoradi.test.integration.clients.ViewResourceClient;
import com.pmoradi.test.integration.rest.RestResponse;
import com.pmoradi.test.util.Randomization;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

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
        List<AddInEntry> entries = Loop.create(Randomization.randomInt(200) + 200, Randomization::randomDataEntry);
        entries.forEach(entry -> assertTrue(dataClient.add(entry).isOk()));
        entries.forEach(entry -> assertTrue(redirectClient.getLink(entry.getUrlName(), entry.getGroupName()).isRedirection()));

        entries.forEach(entry -> {
            GroupView groupView = new GroupView();
            groupView.setGroupName(entry.getGroupName());
            groupView.setPassword(entry.getPassword());

            RestResponse<GroupEntry, GenericMessage> viewResp = viewClient.viewAll(groupView);
            assertTrue(viewResp.isOk());
            assertEquals(1, viewResp.successEntity.getUrls()[0].getClicks().longValue());
        });
    }

    @Test
    public void asynchronousClicks() {
        List<AddInEntry> entries = Loop.create(Randomization.randomInt(200) + 200, Randomization::randomDataEntry);
        entries.parallelStream().forEach(entry -> assertTrue(dataClient.add(entry).isOk()));
        entries.parallelStream().forEach(entry -> assertTrue(redirectClient.getLink(entry.getUrlName(), entry.getGroupName()).isRedirection()));

        entries.parallelStream().forEach(entry -> {
            GroupView groupView = new GroupView();
            groupView.setGroupName(entry.getGroupName());
            groupView.setPassword(entry.getPassword());

            RestResponse<GroupEntry, GenericMessage> viewResp = viewClient.viewAll(groupView);
            assertTrue(viewResp.isOk());
            assertEquals(1, viewResp.successEntity.getUrls()[0].getClicks().longValue());
        });
    }
}
