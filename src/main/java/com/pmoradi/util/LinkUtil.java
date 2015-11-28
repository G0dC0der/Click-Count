package com.pmoradi.util;

public class LinkUtil {

    public static String addHttp(String str){
        if(str.startsWith("http://") || str.startsWith("https://"))
            return str;
        return "http://" + str;
    }
}
