package com.pmoradi.security;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class SecureStrings {

    public static String md5(String str){
        return hash(getDigest("MD5"), str);
    }

    public static String sha1(String str){
        return hash(getDigest("SHA-1"), str);
    }

    private static MessageDigest getDigest(String alg){
        try {
            return MessageDigest.getInstance(alg);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    private static String hash(MessageDigest md, String str){
        byte[] array = md.digest(str.getBytes());
        StringBuilder sb = new StringBuilder();
        for (byte b : array) {
            sb.append(Integer.toHexString((b & 0xFF) | 0x100).substring(1, 3));
        }
        return sb.toString();
    }

    public static String getSalt(){
        return "kingmoradis";
    }
}