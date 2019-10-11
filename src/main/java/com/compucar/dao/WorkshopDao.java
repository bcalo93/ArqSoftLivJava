package com.compucar.dao;

import com.compucar.model.Workshop;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface WorkshopDao extends JpaRepository<Workshop, Long> {
    Optional<Workshop> findById(Long id);

    List<Workshop> findAll();
}
