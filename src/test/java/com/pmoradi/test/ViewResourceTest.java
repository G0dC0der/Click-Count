package com.pmoradi.test;

import com.pmoradi.essentials.Loop;
import com.pmoradi.rest.entries.*;
import com.pmoradi.test.util.*;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

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
    public void totalDataTest() {
        DataEntry entry = Randomization.randomDataEntry();
        assertTrue(dataClient.add(entry).isOk());
        assertTrue(redirectClient.getLink(entry.getUrlName(), entry.getGroupName()).isRedirection());

        RestResponse<TotalEntry> resp = viewClient.getTotalData();
        assertTrue(resp.isOk());
        assertTrue(resp.entity.getTotalUrls() > 0);
        assertTrue(resp.entity.getTotalClicks() > 0);
    }

    @Test
    public void viewAllNoGroup() {
        ViewEntry entry = Randomization.randomViewEntry();
        entry.setGroupName(null);

        RestResponse<Object> resp = viewClient.viewAll(entry);
        assertTrue(resp.isClientError());
        assertFalse(((DataOutEntry)resp.entity).getGroupError().isEmpty());
    }

    @Test
    public void viewAllDefaultGroup() {
        ViewEntry entry = Randomization.randomViewEntry();
        entry.setGroupName("default");

        RestResponse<Object> resp = viewClient.viewAll(entry);
        assertTrue(resp.isClientError());
        assertFalse(((DataOutEntry)resp.entity).getGroupError().isEmpty());
    }

    @Test
    public void viewAllInvalidGroup() {
        ViewEntry entry = Randomization.randomViewEntry();

        RestResponse<Object> resp = viewClient.viewAll(entry);
        assertTrue(resp.isClientError());
        assertFalse(((DataOutEntry)resp.entity).getGroupError().isEmpty());
    }

    @Test
    public void viewAllInvalidPassword() {
        DataEntry dataEntry = Randomization.randomDataEntry();
        RestResponse<DataOutEntry> addResp = dataClient.add(dataEntry);
        assertTrue(addResp.isOk());

        ViewEntry entry = new ViewEntry();
        entry.setGroupName(dataEntry.getGroupName());
        entry.setPassword(Randomization.randomString());

        RestResponse<Object> resp = viewClient.viewAll(entry);
        assertTrue(resp.isClientError());
        assertFalse(((DataOutEntry)resp.entity).getGroupError().isEmpty());
    }

    @Test
    public void viewAllTest() {
        final int SIZE = 10;
        final Calendar TODAY = Calendar.getInstance();
        TODAY.setTime(new Date());
        List<String> urls = Loop.create(SIZE, Randomization::randomString);
        List<String> links = Loop.create(SIZE, Randomization::randomLink);
        DataEntry dataEntry = Randomization.randomDataEntry();

        for(int i = 0; i < SIZE; i++) {
            String url = urls.get(i);
            String link = links.get(i);

            dataEntry.setUrlName(url);
            dataEntry.setLink(link);
            assertTrue(dataClient.add(dataEntry).isOk());

            for(int j = 0; j < i; j++) {
                RestResponse<String> redirectResp = redirectClient.getLink(url, dataEntry.getGroupName());
                assertTrue(redirectResp.isRedirection());
                assertEquals(link, redirectResp.entity);
            }
        }

        ViewEntry viewEntry = new ViewEntry();
        viewEntry.setGroupName(dataEntry.getGroupName());
        viewEntry.setPassword(dataEntry.getPassword());

        RestResponse<Object> viewResp = viewClient.viewAll(viewEntry);
        assertTrue(viewResp.isOk());
        GroupEntry groupEntry = (GroupEntry) viewResp.entity;
        assertEquals(dataEntry.getGroupName(), groupEntry.getGroupName());
        for(int i = 0; i < SIZE; i++) {
            UrlEntry urlEntry = groupEntry.getUrls()[i];

            assertEquals(urls.get(i), urlEntry.getUrlName());
            assertEquals(links.get(i), urlEntry.getLink());
            assertEquals(i, urlEntry.getClicks().length);

            Calendar createDate = Calendar.getInstance();
            createDate.setTime(new Date(urlEntry.getAddDate()));
            assertEquals(TODAY.get(Calendar.DAY_OF_YEAR), createDate.get(Calendar.DAY_OF_YEAR));
            assertEquals(TODAY.get(Calendar.MONTH), createDate.get(Calendar.MONTH));
            assertEquals(TODAY.get(Calendar.YEAR), createDate.get(Calendar.YEAR));
        }
    }
}
