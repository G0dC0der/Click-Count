package com.pmoradi.system;

import com.pmoradi.dao.ClickDao;
import com.pmoradi.dao.EntityDao;
import com.pmoradi.dao.GroupDao;
import com.pmoradi.dao.URLDao;
import com.pmoradi.essentials.Engineering;
import org.glassfish.hk2.utilities.binding.AbstractBinder;
import org.glassfish.jersey.jackson.JacksonFeature;
import org.glassfish.jersey.server.ResourceConfig;

import javax.inject.Singleton;
import javax.ws.rs.ApplicationPath;

@ApplicationPath("/rest")
public class RestSetup extends ResourceConfig {

    public RestSetup(){
        packages("com.pmoradi.rest");
        packages("org.glassfish.jersey.jackson");
        register(JacksonFeature.class);
        register(new AbstractBinder(){
            @Override
            protected void configure() {
                bind(Engineering.class).to(Engineering.class).in(Singleton.class);
                bind(ClickDao.class).to(ClickDao.class).in(Singleton.class);
                bind(GroupDao.class).to(GroupDao.class).in(Singleton.class);
                bind(URLDao.class).to(URLDao.class).in(Singleton.class);
                bind(EntityDao.class).to(EntityDao.class).in(Singleton.class);
            }
        });
    }
}
