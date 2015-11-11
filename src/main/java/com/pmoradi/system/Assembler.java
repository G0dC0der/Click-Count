package com.pmoradi.system;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class Assembler {

    @Target(ElementType.METHOD)
    @Retention(RetentionPolicy.RUNTIME)
    public @interface Exec {}

    public static final void execAll() throws InvocationTargetException, IllegalAccessException {
        for(Method method : Assembler.class.getMethods()){
            if(method.isAnnotationPresent(Exec.class)){
                method.setAccessible(true);
                method.invoke(null);
            }
        }
    }

    @Exec
    static void compileSoy(){

    }
}
