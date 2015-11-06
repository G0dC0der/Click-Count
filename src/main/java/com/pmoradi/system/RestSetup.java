package com.pmoradi.system;

import org.glassfish.hk2.utilities.binding.AbstractBinder;
import org.glassfish.jersey.server.ResourceConfig;

import javax.ws.rs.ApplicationPath;

@ApplicationPath("/rest")
public class RestSetup extends ResourceConfig {

    public RestSetup(){
        packages("com.pmoradi.rest");
        register(new AbstractBinder(){
            @Override
            protected void configure() {
                bind(new Engineering()).to(Engineering.class);
            }
        });
    }
}
