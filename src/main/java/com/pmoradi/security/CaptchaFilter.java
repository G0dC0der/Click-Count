package com.pmoradi.security;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.ResourceInfo;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.List;

public class CaptchaFilter implements ContainerRequestFilter{

    @Context
    private ResourceInfo resourceInfo;
    @Context
    private HttpServletRequest request;

    @Override
    public void filter(ContainerRequestContext requestContext) throws IOException {
        if(isRobotProtected()) {
            String input = getHeader(requestContext, "captcha");
            Captcha captcha = (Captcha) request.getSession().getAttribute("captcha");
            request.getSession().removeAttribute("captcha");

            if(captcha == null){
                requestContext.abortWith(Response.status(Status.FORBIDDEN).entity("Captcha was never requested.").build());
            } else if(captcha.hasExpired()) {
                requestContext.abortWith(Response.status(Status.FORBIDDEN).entity("Captcha has expired.").build());
            } else if(!captcha.isCorrect(input)) {
                requestContext.abortWith(Response.status(Status.FORBIDDEN).entity("Captcha is incorrect.").build());
            }
        }
    }

    private String getHeader(ContainerRequestContext requestContext, String key) {
        List<String> list = requestContext.getHeaders().get(key);
        if(list == null || list.isEmpty())
            return null;
        return list.get(0);
    }

    private boolean isRobotProtected() {
        Method method = resourceInfo.getResourceMethod();
        Class clazz = resourceInfo.getResourceClass();

        return method.getDeclaredAnnotation(RobotSecure.class) != null || clazz.getDeclaredAnnotation(RobotSecure.class) != null;
    }
}
