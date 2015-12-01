package com.pmoradi.essentials;

public class GroupUnavailableException extends Exception{

    public GroupUnavailableException(){
        super();
    }

    public GroupUnavailableException(String message){
        super(message);
    }

    public GroupUnavailableException(Throwable cause){
        super(cause);
    }

    public GroupUnavailableException(String message, Throwable cause){
        super(message, cause);
    }
}
