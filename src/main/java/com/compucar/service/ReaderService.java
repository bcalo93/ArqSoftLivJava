package com.compucar.service;

import com.compucar.model.Reader;
import com.compucar.service.exceptions.DuplicateElementException;
import com.compucar.service.exceptions.NotFoundException;
import com.compucar.service.exceptions.RequiredFieldMissingException;

import java.util.List;

public interface ReaderService {
    List<Reader> listReaders();

    Reader getReader(Long id) throws NotFoundException;

    Reader addReader(Reader reader) throws RequiredFieldMissingException, DuplicateElementException, NotFoundException;

    void updateReader(Reader reader) throws RequiredFieldMissingException, NotFoundException, DuplicateElementException;

    void removeReader(Long id) throws NotFoundException;
}
