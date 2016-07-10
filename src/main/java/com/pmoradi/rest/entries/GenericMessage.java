package com.pmoradi.rest.entries;

public class GenericMessage {

    private String message;

    public GenericMessage(){}

    public GenericMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return message;
    }
}
