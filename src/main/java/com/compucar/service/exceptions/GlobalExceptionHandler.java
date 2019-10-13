package com.compucar.service.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ResponseBody
    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String notFoundHandler(NotFoundException ex) {
        return ex.getMessage();
    }

    @ResponseBody
    @ExceptionHandler(DuplicateElementException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public String duplicateElementHandler(DuplicateElementException ex) {
        return ex.getMessage();
    }

    @ResponseBody
    @ExceptionHandler(RequiredFieldMissingException.class)
    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    public String requiredFieldMissingHandler(RequiredFieldMissingException ex) {
        return ex.getMessage();
    }

    @ResponseBody
    @ExceptionHandler({EntityNullException.class, IdNullException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String badRequestHandler(Exception ex) {
        return ex.getMessage();
    }
}
