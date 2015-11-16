package com.pmoradi.security;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class SecureStrings {

    private static MessageDigest MD5, SHA1;

    static {
        try {
            MD5 = MessageDigest.getInstance("MD5");
            SHA1 = MessageDigest.getInstance("SHA-1");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }

    public static String md5(String str){
        try {
            return new String(MD5.digest(str.getBytes("UTF-8")));
        } catch (UnsupportedEncodingException e) {
            return null;
        }
    }

    public static String sha1(String str){
        try {
            return new String(SHA1.digest(str.getBytes("UTF-8")));
        } catch (UnsupportedEncodingException e) {
            return null;
        }
    }

    public static String getSalt(){
        return "CLICKERZ";
    }
}