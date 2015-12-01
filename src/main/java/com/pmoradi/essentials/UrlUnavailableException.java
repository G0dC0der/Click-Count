package com.pmoradi.essentials;

public class UrlUnavailableException extends Exception{

    public UrlUnavailableException(){
        super();
    }

    public UrlUnavailableException(String message){
        super(message);
    }

    public UrlUnavailableException(Throwable cause){
        super(cause);
    }

    public UrlUnavailableException(String message, Throwable cause){
        super(message, cause);
    }
}
