package com.otuz.model;

/**
 * A POJO for holding the error code and the error message of the server operation.
 * Created by AhmetOguzhanBasar on 20.02.2016.
 */
public class ErrorModel {

    private int errorCode;
    private String errorMessage;

    public int getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
}
