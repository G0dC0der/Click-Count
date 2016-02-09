package com.pmoradi.system;

import com.pmoradi.entities.dao.ClickDao;
import com.pmoradi.entities.dao.GroupDao;
import com.pmoradi.entities.dao.URLDao;
import com.pmoradi.entities.dao.UserDao;
import org.glassfish.hk2.api.Factory;
import org.glassfish.hk2.api.ServiceLocator;

import javax.persistence.EntityManagerFactory;
import javax.ws.rs.core.Context;
import java.net.InetAddress;
import java.net.UnknownHostException;

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

    static Factory<UserDao> getUserDaoFactory(SessionFactory sessionFactory){
        return new Factory<UserDao>() {
            @Override
            public UserDao provide() {
                return new UserDao(sessionFactory);
            }

            @Override public void dispose(UserDao userDao) {}
        };
    }

    static Factory<Facade> getFacadeFactory(LockManager manager) {
        return new Factory<Facade>() {

            @Context
            ServiceLocator locator;

            @Override
            public Facade provide() {
                Facade facade = new Facade(manager);
                locator.inject(facade);
                return facade;
            }

            @Override
            public void dispose(Facade facade) {}
        };
    }

    static Factory<AdminFacade> getAdminFacadeFactory() {
        return new Factory<AdminFacade>() {

            @Context
            ServiceLocator locator;

            @Override
            public AdminFacade provide() {
                AdminFacade facade = new AdminFacade();
                locator.inject(facade);
                return facade;
            }

            @Override
            public void dispose(AdminFacade facade) {}
        };
    }

    static Factory<?> getShutdownCleaner(EntityManagerFactory entityManagerFactory) {
        return new Factory<Object>() {
            @Override
            public Object provide() {
                return null;
            }

            @Override
            public void dispose(Object o) {
                entityManagerFactory.close();
            }
        };
    }
}
