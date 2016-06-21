package com.pmoradi.essentials;

import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Random;

public class WebUtil {

    private static final char[] URL_CHARS = "abcdefghijklmnopqrstuvwxyz0123456789".toCharArray();
    private static final Random r = new Random();

    public static String randomUrl(){
        final int size = 8;
        StringBuilder bu = new StringBuilder(size);
        for(int i = 0; i < size; i++){
            bu.append(URL_CHARS[r.nextInt(URL_CHARS.length)]);
        }

        return bu.toString();
    }

    public static boolean isReserved(String token){
        switch (token){
            case "add":
            case "default":
            case "view":
            case "all":
            case "service":
            case "admin":
                return true;
            default:
                return false;
        }
    }

    public static boolean isProtocolBased(String link) {
        return  link.startsWith("http://") || link.startsWith("https://") ||
                link.startsWith("ftp://")  || link.startsWith("sftp://");
    }

    public static boolean exists(String link) {
        if (link.startsWith("http://") || link.startsWith("https://"))
            return httpExists(link);
        else if (link.startsWith("ftp://") || link.startsWith("sftp://"))
            return urlExists(link);
        else
            throw new IllegalArgumentException("Unknown protocol for " + link);
    }

    private static boolean httpExists(String link) {
        HttpURLConnection con = null;
        try {
            URL url = new URL(link);
            con =  (HttpURLConnection)  url.openConnection ();
            con.setRequestMethod ("HEAD");
            con.connect();
            int code = con.getResponseCode() ;
            return code >= 200 && code <= 299;
        } catch (IOException e) {
            return false;
        } finally {
            IOUtils.close(con);
        }
    }

    private static boolean urlExists(String link) {
        try {
            new URL(link).openStream().close();
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    public static boolean validUrl(String url){
        for(char c : url.toCharArray()){
            switch (c){
                case 'a':
                case 'b':
                case 'c':
                case 'd':
                case 'e':
                case 'f':
                case 'g':
                case 'h':
                case 'i':
                case 'j':
                case 'k':
                case 'l':
                case 'm':
                case 'n':
                case 'o':
                case 'p':
                case 'q':
                case 'r':
                case 's':
                case 't':
                case 'u':
                case 'v':
                case 'w':
                case 'x':
                case 'y':
                case 'z':
                case 'A':
                case 'B':
                case 'C':
                case 'D':
                case 'E':
                case 'F':
                case 'G':
                case 'H':
                case 'I':
                case 'J':
                case 'K':
                case 'L':
                case 'M':
                case 'N':
                case 'O':
                case 'P':
                case 'Q':
                case 'R':
                case 'S':
                case 'T':
                case 'U':
                case 'V':
                case 'W':
                case 'X':
                case 'Y':
                case 'Z':
                case '0':
                case '1':
                case '2':
                case '3':
                case '4':
                case '5':
                case '6':
                case '7':
                case '8':
                case '9':
                case '.':
                case '-':
                case '_':
                case '~':
                    break;
                default:
                    return false;
            }
        }
        return true;
    }
}
