package com.compucar.dao;

import com.compucar.model.Reader;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ReaderDao extends JpaRepository<Reader, Long> {
    Optional<Reader> findById(Long id);

    Optional<Reader> findByCode(String code);

    List<Reader> findByWorkshopId(Long workshopId);

    List<Reader> findAll();

    boolean existsByCode(String code);
}
