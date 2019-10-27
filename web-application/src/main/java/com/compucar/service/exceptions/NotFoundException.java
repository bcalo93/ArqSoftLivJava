package com.compucar.service.exceptions;

import java.io.Serializable;

public class NotFoundException extends Exception implements Serializable {
    private static final long serialVersionUID = 1L;

    public NotFoundException(String element) {
        super(element + " was not found.");
    }
}
