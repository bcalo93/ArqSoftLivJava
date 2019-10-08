package com.compucar.service;

import com.compucar.model.Reader;

import java.util.List;

public interface ReaderService {
    void addReader(Reader reader);

    void update(Reader reader);

    void removeReader(Reader reader);

    Reader getReader(Long id);

    List<Reader> listReaders();
}
