package com.pmoradi.test.dao;

import com.pmoradi.entities.dao.EntityDao;
import com.pmoradi.entities.dao.URLDao;
import com.pmoradi.entities.Group;
import com.pmoradi.entities.URL;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public class UrlDaoTest {

    private URLDao urlDao;
    private EntityDao entityDao;

    @Before
    public void setup(){
        urlDao = new URLDao();
        entityDao = new EntityDao();
    }

    @Test
    public void testFindByName(){
        final String URL_NAME = UUID.randomUUID().toString();
        URL url = new URL();
        url.setUrl(URL_NAME);
        url.setLink("link");
        urlDao.save(url);

        Assert.assertNotNull(url.getId());

        URL target = urlDao.findByUrlName(URL_NAME);

        Assert.assertNotNull(target);
    }

    @Test
    public void testFindByLink(){
        final String LINK = UUID.randomUUID().toString();

        URL url1 = new URL();
        url1.setUrl(UUID.randomUUID().toString());
        url1.setLink(LINK);

        URL url2 = new URL();
        url2.setUrl(UUID.randomUUID().toString());
        url2.setLink(LINK);

        urlDao.save(url1);
        urlDao.save(url2);

        List<URL> urls = urlDao.findByLink(LINK);

        Assert.assertEquals(2, urls.size());
        Assert.assertEquals(url1.getId(), urls.get(0).getId());
        Assert.assertEquals(url2.getId(), urls.get(1).getId());
    }

    @Test
    public void testFindByGroupAndUrl(){
        final String GROUP = UUID.randomUUID().toString();
        final String URL = UUID.randomUUID().toString();

        URL url = new URL();
        Group group = new Group();
        group.setGroupName(GROUP);
        group.setPassword(UUID.randomUUID().toString());
        group.setUrls(Arrays.asList(url));

        url.setGroup(group);
        url.setUrl(URL);
        url.setLink(UUID.randomUUID().toString());
        url.setGroup(group);

        entityDao.save(group, url);

        Assert.assertNotNull(urlDao.findByGroupAndUrl(GROUP, URL));
        Assert.assertNull(urlDao.findByGroupAndUrl(GROUP, "foo"));
        Assert.assertNull(urlDao.findByGroupAndUrl("bar", URL));
    }
}
