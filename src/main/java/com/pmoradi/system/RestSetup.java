package com.pmoradi.system;

import org.glassfish.jersey.server.ResourceConfig;

import javax.ws.rs.ApplicationPath;

@ApplicationPath("/")
public class RestSetup extends ResourceConfig {

    public RestSetup(){
        packages("com.pmoradi.rest");
    }
}
