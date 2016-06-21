package com.pmoradi.system;

import com.pmoradi.entities.dao.GroupDao;
import com.pmoradi.entities.dao.URLDao;
import com.pmoradi.entities.dao.UserDao;
import org.glassfish.hk2.api.Factory;
import org.glassfish.hk2.api.ServiceLocator;

import javax.ws.rs.core.Context;
import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.util.Properties;
import java.util.concurrent.Executors;

class InjectFactory {

    static Factory<Application> getApplicationFactory(InputStream properties) {
        try {
            Properties props = new Properties();
            props.load(properties);

            return new Factory<Application>() {
                @Override
                public Application provide() {
                    return new Application() {
                        @Override
                        public String getRestPath() {
                            return props.getProperty("restUrl");
                        }

                        @Override
                        public String getDomain() {
                            return props.getProperty("domain");
                        }
                    };
                }

                @Override
                public void dispose(Application application) {}
            };
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
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
                Facade facade = new Facade(manager, Executors.newFixedThreadPool(500));
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
}
