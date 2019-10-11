package com.compucar.service.exceptions;

import java.io.Serializable;

public class DuplicateElementException extends Exception implements Serializable {
    private static final long serialVersionUID = 1L;

    public DuplicateElementException(String element) {
        super(element + " already exists.");
    }
}
