package com.pmoradi.security;

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
        return hash(MD5, str);
    }

    public static String sha1(String str){
        return hash(SHA1, str);
    }

    private static String hash(MessageDigest md, String str){
        byte[] array = md.digest(str.getBytes());
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < array.length; ++i) {
            sb.append(Integer.toHexString((array[i] & 0xFF) | 0x100).substring(1,3));
        }
        return sb.toString();
    }

    public static final String getSalt(){
        return "CLICKERZ";
    }
}