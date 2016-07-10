package com.pmoradi.test.integration;

import com.pmoradi.essentials.Loop;
import com.pmoradi.rest.entries.*;
import com.pmoradi.test.integration.clients.DataResourceClient;
import com.pmoradi.test.integration.clients.RedirectResourceClient;
import com.pmoradi.test.integration.clients.ViewResourceClient;
import com.pmoradi.test.integration.rest.RestInfo;
import com.pmoradi.test.integration.rest.RestResponse;
import com.pmoradi.test.util.Randomization;
import org.junit.Before;
import org.junit.Test;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.*;

public class ViewResourceTest {

    private ViewResourceClient viewClient;
    private DataResourceClient dataClient;
    private RedirectResourceClient redirectClient;

    @Before
    public void setup() {
        viewClient = new ViewResourceClient(RestInfo.LOCAL_REST_URL);
        dataClient = new DataResourceClient(RestInfo.LOCAL_REST_URL);
        redirectClient = new RedirectResourceClient(RestInfo.LOCAL_REST_URL);
    }

    @Test
    public void viewAllNoGroup() {
        GroupView entry = Randomization.randomViewEntry();
        entry.setGroupName(null);

        RestResponse<GroupEntry, GenericMessage> resp = viewClient.viewAll(entry);
        assertTrue(resp.isClientError());
    }

    @Test
    public void viewAllDefaultGroup() {
        GroupView entry = Randomization.randomViewEntry();
        entry.setGroupName("default");

        RestResponse<GroupEntry, GenericMessage> resp = viewClient.viewAll(entry);
        assertTrue(resp.isClientError());
    }

    @Test
    public void viewAllInvalidGroup() {
        GroupView entry = Randomization.randomViewEntry();

        RestResponse<GroupEntry, GenericMessage> resp = viewClient.viewAll(entry);
        assertTrue(resp.isClientError());
    }

    @Test
    public void viewAllInvalidPassword() {
        AddInEntry dataEntry = Randomization.randomDataEntry();
        RestResponse<GenericMessage, AddOutEntry> addResp = dataClient.add(dataEntry);
        assertTrue(addResp.isOk());

        GroupView entry = new GroupView();
        entry.setGroupName(dataEntry.getGroupName());
        entry.setPassword(Randomization.randomString());

        RestResponse<GroupEntry, GenericMessage> resp = viewClient.viewAll(entry);
        assertTrue(resp.isClientError());
    }

    @Test
    public void viewAllTest() {
        final Calendar TODAY = Calendar.getInstance();
        TODAY.setTime(new Date());
        List<String> urls = Loop.create(1, Randomization::randomString);
        List<String> links = Loop.create(1, ()-> "http://google.se");
        AddInEntry dataEntry = Randomization.randomDataEntry();

        String url = urls.get(0);
        String link = links.get(0);

        dataEntry.setUrlName(url);
        dataEntry.setLink(link);
        assertTrue(dataClient.add(dataEntry).isOk());

        RestResponse<String, GenericMessage> redirectResp = redirectClient.getLink(url, dataEntry.getGroupName());
        assertTrue(redirectResp.isRedirection());
        assertEquals(link, redirectResp.successEntity);

        GroupView viewEntry = new GroupView();
        viewEntry.setGroupName(dataEntry.getGroupName());
        viewEntry.setPassword(dataEntry.getPassword());

        RestResponse<GroupEntry, GenericMessage> viewResp = viewClient.viewAll(viewEntry);
        assertTrue(viewResp.isOk());
        GroupEntry resultGroupEntry = (GroupEntry) viewResp.successEntity;
        assertEquals(dataEntry.getGroupName(), resultGroupEntry.getGroupName());

        UrlEntry urlEntry = resultGroupEntry.getUrls()[0];

        assertEquals(urls.get(0), urlEntry.getUrlName());
        assertEquals(links.get(0), urlEntry.getLink());
        assertEquals(1, urlEntry.getClicks().longValue());

        Calendar createDate = Calendar.getInstance();
        createDate.setTime(new Date(urlEntry.getAddDate()));
        assertEquals(TODAY.get(Calendar.DAY_OF_YEAR), createDate.get(Calendar.DAY_OF_YEAR));
        assertEquals(TODAY.get(Calendar.MONTH), createDate.get(Calendar.MONTH));
        assertEquals(TODAY.get(Calendar.YEAR), createDate.get(Calendar.YEAR));
    }

    @Test
    public void viewSingleWithInvalidUrl() {
        RestResponse<GroupEntry, GenericMessage> resp = viewClient.viewSingle(Randomization.randomString());
        assertTrue(resp.isClientError());
    }

    @Test
    public void viewSingleWithInvalidGroup() {
        AddInEntry entry = Randomization.randomDataEntry();
        assertTrue(dataClient.add(entry).isOk());

        RestResponse<GroupEntry, GenericMessage> resp = viewClient.viewSingle(Randomization.randomString(), entry.getGroupName());
        assertTrue(resp.isClientError());
    }

    @Test
    public void viewSingleWithGroupHidesClicks() {
        AddInEntry entry = Randomization.randomDataEntry();
        assertTrue(dataClient.add(entry).isOk());

        final int size = Randomization.randomInt(10) + 10;
        for (int i = 0; i < size; i++) {
            RestResponse<String, GenericMessage> reResp = redirectClient.getLink(entry.getUrlName(), entry.getGroupName());
            assertTrue(reResp.isRedirection());
        }

        RestResponse<GroupEntry, GenericMessage> resp = viewClient.viewSingle(entry.getUrlName(), entry.getGroupName());
        UrlEntry urlEntry = resp.successEntity.getUrls()[0];
        assertNull(urlEntry.getClicks());
    }

    @Test
    public void viewSingleWithDefaultGroupShowClicks() {
        AddInEntry entry = new AddInEntry();
        entry.setUrlName(Randomization.randomString());
        entry.setLink("http://google.se");
        RestResponse<GenericMessage, AddOutEntry> resp = dataClient.add(entry);
        assertTrue(resp.isOk());

        RestResponse<String, GenericMessage> reResp = redirectClient.getLink(entry.getUrlName());
        assertTrue(reResp.isRedirection());

        RestResponse<GroupEntry, GenericMessage> viewResp = viewClient.viewSingle(entry.getUrlName());
        UrlEntry urlEntry = viewResp.successEntity.getUrls()[0];
        assertEquals(1, urlEntry.getClicks().longValue());
    }
}