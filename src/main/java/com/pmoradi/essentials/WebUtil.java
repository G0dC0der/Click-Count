package com.pmoradi.essentials;

import javax.ws.rs.core.Response.Status;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
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

    public static String addHttp(String str){
        if(str.startsWith("http://") || str.startsWith("https://"))
            return str;
        return "http://" + str;
    }

    public static boolean isReserved(String token){
        switch (token){
            case "add":
            case "default":
            case "view":
            case "all":
            case "service":
                return true;
            default:
                return false;
        }
    }

    public static boolean isLocalAddress(String addr) {
        return addr.equals("127.0.0.1") || addr.equals("localhost") || addr.equals("0:0:0:0:0:0:0:1");
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
