package com.pmoradi.test.unit;


import com.pmoradi.entities.Namespace;
import com.pmoradi.entities.URL;
import com.pmoradi.test.util.Randomization;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class URLDaoTest extends AbstractDaoTest{

    @Test
    public void clickTest(){
        Namespace namespace = new Namespace();
        namespace.setName(Randomization.randomString());
        namespace.setPassword(Randomization.randomString());
        namespaceDao.save(namespace);

        URL url = new URL();
        url.setNamespace(namespace);
        url.setAlias(Randomization.randomString());
        url.setAdded(System.currentTimeMillis());
        url.setSource("google.se");
        urlDao.save(url);

        urlDao.click(namespace.getName(), url.getAlias());

        URL fetched = urlDao.findById(namespace.getName(), url.getAlias());
        assertEquals(1, fetched.getClicks().longValue());
    }
}