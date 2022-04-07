package com.shareit.exception;

public class InvalidParameterException extends RuntimeException {
    private String paramName;

    public InvalidParameterException(String paramName) {
        super(String.format("Invalid param: $1", paramName));
        this.paramName = paramName;
    }

    public String getParamName() {
        return paramName;
    }
}
