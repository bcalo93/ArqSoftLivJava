package com.compucar.dao;

import com.compucar.model.Workshop;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface WorkshopDao extends JpaRepository<Workshop, Long> {
    Optional<Workshop> findById(Long id);
    Optional<Workshop> findByCode(String code);

    List<Workshop> findAll();

    boolean existsByCode(String code);
}
