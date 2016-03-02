package com.pmoradi.essentials;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;

public class Interceptors {

    public static <T> T benchmark(T obj, Class<T> iface) {
        ClassLoader cl = obj.getClass().getClassLoader();
        Class[] classes = {iface};
        InvocationHandler h = (proxy, method, args) -> {
            long start = System.nanoTime();
            Object value =  method.invoke(obj, args);
            long end = System.nanoTime();
            System.out.println("Time " + method.getName() + ": " + (end - start));
            return value;
        };

        return (T) Proxy.newProxyInstance(cl, classes, h);
    }
}
