package com.pmoradi.test.dao;

import com.pmoradi.entities.Click;
import com.pmoradi.entities.dao.ClickDao;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class ClickDaoTest {

    private ClickDao clickDao;

    @Before
    public void setup(){
        clickDao = new ClickDao();
    }

    @Test
    public void saveAndFindTest(){
        Click click = new Click();
        clickDao.save(click);

        Click target = clickDao.findById(click.getId());
        Assert.assertNotNull(target);
        Assert.assertEquals(click.getId(), target.getId());
    }

    @Test
    public void deleteTest(){
        Click click = new Click();
        clickDao.save(click);
        Click beforeDelete = clickDao.findById(click.getId());

        Assert.assertNotNull(beforeDelete);
        Assert.assertEquals(click.getId(), beforeDelete.getId());

        clickDao.delete(click);
        Click afterDelete = clickDao.findById(click.getId());

        Assert.assertNull(afterDelete);
    }
}
