package com.compucar.dao;

import com.compucar.model.Reader;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReaderDao extends JpaRepository<Reader, Long> {
    Reader findById(Long id);

    List<Reader> findAll();
}
