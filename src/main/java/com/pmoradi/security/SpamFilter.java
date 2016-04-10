package com.pmoradi.security;

import com.pmoradi.essentials.ExpiringSet;
import com.pmoradi.rest.entries.GenericMessage;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.ResourceInfo;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import java.io.IOException;
import java.lang.reflect.Method;

public class SpamFilter implements ContainerRequestFilter{

    private static final ExpiringSet<String> CACHE = new ExpiringSet();

    @Context
    private ResourceInfo resourceInfo;
    @Context
    private HttpServletRequest request;

    @Override
    public void filter(ContainerRequestContext requestContext) throws IOException {
        final long delay = getDelay();
        final String ip = request.getRemoteAddr();

        if(delay != -1) {
            if(CACHE.hasExpired(ip)) {
                CACHE.put(ip, delay);
            } else {
                requestContext.abortWith(Response.status(Status.FORBIDDEN).entity(
                        new GenericMessage("Spam protection activated! This services has a " + delay + " ms delay.")).build());
            }
        }
    }

    private long getDelay() {
        Method method = resourceInfo.getResourceMethod();
        if(method.isAnnotationPresent(RequestInterval.class)) {
            return method.getDeclaredAnnotation(RequestInterval.class).value();
        }

        Class clazz = resourceInfo.getResourceClass();
        if(clazz.isAnnotationPresent(Guarded.class)) {
            return ((RequestInterval)clazz.getDeclaredAnnotation(RequestInterval.class)).value();
        }
        return -1;
    }

}
