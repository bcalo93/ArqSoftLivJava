package com.compucar.service;

import com.compucar.model.Reader;
import com.compucar.service.exceptions.DuplicateElementException;
import com.compucar.service.exceptions.InvalidFieldValueException;
import com.compucar.service.exceptions.NotFoundException;
import com.compucar.service.exceptions.RequiredFieldMissingException;

import java.util.List;

public interface ReaderService {
    List<Reader> listReaders();

    List<Reader> listReadersWithBatteryLessThan(Integer delta) throws InvalidFieldValueException;

    Reader getReader(Long id) throws NotFoundException;

    Reader addReader(Reader reader) throws RequiredFieldMissingException, DuplicateElementException, NotFoundException;

    Reader updateReader(Reader reader) throws RequiredFieldMissingException, NotFoundException, InvalidFieldValueException;

    void removeReader(Long id) throws NotFoundException;
}
