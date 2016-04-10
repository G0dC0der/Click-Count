package com.pmoradi.test.unit.mocks;

import com.pmoradi.entities.dao.URLDao;
import com.pmoradi.essentials.Loop;
import com.pmoradi.test.util.Randomization;
import org.mockito.Mockito;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

public class DaoMocks {

    public static URLDao getURLDao() {
        URLDao dao = Mockito.mock(URLDao.class);
        when(dao.findByGroupAndUrl(any(), any())).thenReturn(EntityGenerator.generateURL());
        when(dao.findByLink(any())).then(mock -> Loop.create(Randomization.randomInt(10), EntityGenerator::generateURL));

        return dao;
    }
}
