package com.pmoradi.system;

import com.pmoradi.entities.dao.ClickDao;
import com.pmoradi.entities.dao.GroupDao;
import com.pmoradi.entities.dao.URLDao;
import org.glassfish.hk2.api.Factory;

class InjectFactory {

    static Factory<ClickDao> getClickDaoFactory(SessionFactory sessionFactory){
        return new Factory<ClickDao>() {
            @Override
            public ClickDao provide() {
                return new ClickDao(sessionFactory);
            }

            @Override public void dispose(ClickDao clickDao) {}
        };
    }

    static Factory<GroupDao> getGroupDaoFactory(SessionFactory sessionFactory){
        return new Factory<GroupDao>() {
            @Override
            public GroupDao provide() {
                return new GroupDao(sessionFactory);
            }

            @Override public void dispose(GroupDao groupDao) {}
        };
    }

    static Factory<URLDao> getURLDaoFactory(SessionFactory sessionFactory){
        return new Factory<URLDao>() {
            @Override
            public URLDao provide() {
                return new URLDao(sessionFactory);
            }

            @Override public void dispose(URLDao urlDao) {}
        };
    }

    static Factory<Facade> getFacadeFactory(LockManager manager) {
        return new Factory<Facade>() {
            @Override
            public Facade provide() {
                return new Facade(manager);
            }

            @Override
            public void dispose(Facade facade) {}
        };
    }
}
