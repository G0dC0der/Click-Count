package com.pmoradi.test.integration.rest;

public class RestResponse<T> {

    public final int statusCode;
    public final T entity;

    public RestResponse(int statusCode, T entity) {
        this.statusCode = statusCode;
        this.entity = entity;
    }

    public boolean isOk() {
        return statusCode >= 200 &&  statusCode <= 299;
    }

    public boolean isRedirection() {
        return statusCode >= 300 &&  statusCode <= 399;
    }

    public boolean isClientError() {
        return statusCode >= 400 &&  statusCode <= 499;
    }

    public boolean isServerError() {
        return statusCode >= 500 &&  statusCode <= 599;
    }

    @Override
    public String toString() {
        return "statusCode=" + statusCode + "\nentity=" + entity;
    }
}
