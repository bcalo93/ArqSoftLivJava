package com.compucar.service.exceptions;

import java.io.Serializable;

public class RequiredFieldMissingException extends Exception implements Serializable {
    private static final long serialVersionUID = 1L;

    public RequiredFieldMissingException(String field) {
        super(field + " is required and can't be null or empty.");
    }
}
