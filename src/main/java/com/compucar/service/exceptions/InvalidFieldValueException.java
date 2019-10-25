package com.compucar.service.exceptions;

import java.io.Serializable;

public class InvalidFieldValueException extends Exception implements Serializable {
    private static final long serialVersionUID = 1L;

    public InvalidFieldValueException(String reason) {
        super("Invalid field value: " + reason);
    }
}
