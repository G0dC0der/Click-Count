package com.pmoradi.system;

import com.pmoradi.entities.dao.ClickDao;
import com.pmoradi.entities.dao.GroupDao;
import com.pmoradi.entities.dao.URLDao;
import org.glassfish.hk2.api.Factory;
import org.glassfish.hk2.api.ServiceLocator;
import org.glassfish.hk2.utilities.binding.AbstractBinder;
import org.glassfish.jersey.jackson.JacksonFeature;
import org.glassfish.jersey.server.ResourceConfig;

import javax.inject.Singleton;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Context;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

@ApplicationPath("/service")
public class SystemSetup extends ResourceConfig {

    public SystemSetup(@Context ServiceLocator locator) throws IOException {
        packages("com.pmoradi.rest");
        packages("org.glassfish.jersey.jackson");
        register(JacksonFeature.class);

        final SessionFactory sessionFactory = getSessionFactory();

        register(new AbstractBinder() {
            @Override
            protected void configure() {
                bindFactory(InjectFactory.getClickDaoFactory(sessionFactory)).to(ClickDao.class).in(Singleton.class);
                bindFactory(InjectFactory.getGroupDaoFactory(sessionFactory)).to(GroupDao.class).in(Singleton.class);
                bindFactory(InjectFactory.getURLDaoFactory(sessionFactory)).to(URLDao.class).in(Singleton.class);
                bindFactory(InjectFactory.getApplicationSettingsFactory("localhost:9090", "META-INF/credentials.json")).to(ApplicationSettings.class).in(Singleton.class);

                Factory<Facade> facadeFactory = InjectFactory.getFacadeFactory(new LockManager());
                Factory<AdminFacade> adminFacadeFactory = InjectFactory.getAdminFacadeFactory();

                locator.inject(facadeFactory);
                locator.inject(adminFacadeFactory);

                bindFactory(facadeFactory).to(Facade.class).in(Singleton.class);
                bindFactory(adminFacadeFactory).to(AdminFacade.class).in(Singleton.class);
            }
        });
    }

    private SessionFactory getSessionFactory() {
        return new SessionFactory() {

            EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("hibernate-engine");

            @Override
            public EntityManager newSession() {
                return entityManagerFactory.createEntityManager();
            }
        };
    }
}