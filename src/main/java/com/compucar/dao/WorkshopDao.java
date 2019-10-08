package com.compucar.dao;

import com.compucar.model.Workshop;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface WorkshopDao extends JpaRepository<Workshop, Long> {
    Workshop findById(Long id);

    List<Workshop> findAll();
}
