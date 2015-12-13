package com.pmoradi.test.dao;

import com.pmoradi.entities.dao.ClickDao;
import com.pmoradi.entities.dao.URLDao;
import com.pmoradi.entities.Click;
import com.pmoradi.entities.URL;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.sql.Timestamp;
import java.util.List;
import java.util.UUID;

public class DaoTest {

    private URLDao urlDao;
    private ClickDao clickDao;

    @Before
    public void setup(){
        urlDao = new URLDao();
        clickDao = new ClickDao();
    }

    @Test
    public void clickUrlCascade(){
        URL url = new URL();
        url.setUrl(UUID.randomUUID().toString());
        url.setLink(UUID.randomUUID().toString());
        urlDao.save(url);

        final int CLICKS = 10;
        for(int i = 0; i < CLICKS; i++){
            Click click = new Click();
            click.setTime(new Timestamp(System.currentTimeMillis()));
            click.setUrl(url);
            clickDao.save(click);
        }
        List<Click> clicks = urlDao.findById(url.getId()).getClicks();

        Assert.assertEquals(CLICKS, clicks.size());
        Click click = clicks.get(0);

        Assert.assertEquals(url.getId(), click.getUrl().getId());
    }
}
