package com.pmoradi.system;

import com.pmoradi.entities.dao.ClickDao;
import com.pmoradi.entities.dao.GroupDao;
import com.pmoradi.entities.dao.URLDao;
import org.glassfish.hk2.utilities.binding.AbstractBinder;
import org.glassfish.jersey.jackson.JacksonFeature;
import org.glassfish.jersey.server.ResourceConfig;

import javax.inject.Singleton;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.ws.rs.ApplicationPath;
import java.util.HashSet;
import java.util.Set;

@ApplicationPath("/service")
public class SystemSetup extends ResourceConfig {

    public SystemSetup() {
        packages("com.pmoradi.rest");
        packages("org.glassfish.jersey.jackson");
        register(JacksonFeature.class);

        final LockManager manager = getLockManager();
        final SessionFactory sessionFactory = getSessionFactory();
        register(new AbstractBinder() {
            @Override
            protected void configure() {
                bindFactory(InjectFactory.getFacadeFactory(manager)).in(Singleton.class);
                bindFactory(InjectFactory.getClickDaoFactory(sessionFactory)).in(Singleton.class);
                bindFactory(InjectFactory.getGroupDaoFactory(sessionFactory)).in(Singleton.class);
                bindFactory(InjectFactory.getURLDaoFactory(sessionFactory)).in(Singleton.class);
            }
        });
    }

    private LockManager getLockManager() {
        return new LockManager() {

            private Set<Key> keys = new HashSet<>();

            @Override
            public synchronized Key lock(String keyName) {
                final Key key = new EntityKey(keyName);

                while (keys.contains(key)) {
                    try {
                        wait();
                    } catch (InterruptedException e) {}
                }
                keys.add(key);
                return key;
            }

            @Override
            public synchronized void unlock(Key key) {
                keys.remove(key);
                notifyAll();
            }
        };
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
