package com.pmoradi.test.integration.rest;

import javax.ws.rs.core.Response;

public class RestResponse<SUCCESS, FAIL> {

    public final int statusCode;
    public final SUCCESS successEntity;
    public final FAIL failEntity;

    public RestResponse(int statusCode, SUCCESS successEntity, FAIL failEntity) {
        this.statusCode = statusCode;
        this.successEntity = successEntity;
        this.failEntity = failEntity;
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
        return "RestResponse{" +
                "statusCode=" + statusCode +
                ", successEntity=" + successEntity +
                ", failEntity=" + failEntity +
                '}';
    }

    public static <SUCCESS, FAIL> RestResponse<SUCCESS, FAIL> fromResponse(Response resp, Class<SUCCESS> successClass, Class<FAIL> failClass) {
        boolean isOk = resp.getStatus() >= 200 && resp.getStatus() <= 299;
        if(isOk) {
            return new RestResponse(resp.getStatus(), resp.readEntity(successClass), null);
        } else {
            return new RestResponse(resp.getStatus(), null, resp.readEntity(failClass));
        }
    }
}
