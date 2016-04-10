package com.pmoradi.security;

import com.pmoradi.entities.User;
import com.pmoradi.entities.dao.UserDao;
import com.pmoradi.rest.entries.GenericMessage;

import javax.inject.Inject;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.ResourceInfo;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.List;

public class AuthenticationFilter implements ContainerRequestFilter{

    @Context
    private ResourceInfo resourceInfo;
    @Inject
    private UserDao userDao;

    @Override
    public void filter(ContainerRequestContext requestContext) throws IOException {
        final Role requiredRole = findRole();

        if(requiredRole != null) {
            String username = getHeader(requestContext, "username");
            String password = getHeader(requestContext, "password");

            if (username == null || password == null) {
                requestContext.abortWith(Response.status(Status.FORBIDDEN).entity(new GenericMessage("Incomplete request. Credentials not found in header.")).build());
                return;
            }

            User user = userDao.findByName(username);
            if (user == null || !user.getPassword().equals(SecureStrings.md5(password + SecureStrings.getSalt()))) {
                requestContext.abortWith(Response.status(Status.UNAUTHORIZED).entity(new GenericMessage("Access denied! Wrong username and password combination.")).build());
                return;
            }
            if (requiredRole.isAbove(user.getRole())) {
                requestContext.abortWith(Response.status(Status.UNAUTHORIZED).entity(new GenericMessage("The request was aborted. Role not sufficient enough.")).build());
                return;
            }
        }
    }

    private String getHeader(ContainerRequestContext requestContext, String key) {
        List<String> list = requestContext.getHeaders().get(key);
        if(list == null || list.isEmpty())
            return null;
        return list.get(0);
    }

    private Role findRole() {
        Method method = resourceInfo.getResourceMethod();
        if(method.isAnnotationPresent(Guarded.class)) {
            return method.getDeclaredAnnotation(Guarded.class).value();
        }

        Class clazz = resourceInfo.getResourceClass();
        if(clazz.isAnnotationPresent(Guarded.class)) {
            return ((Guarded)clazz.getDeclaredAnnotation(Guarded.class)).value();
        }
        return null;
    }
}
