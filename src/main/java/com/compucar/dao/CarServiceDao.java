package com.compucar.dao;

import com.compucar.model.CarService;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CarServiceDao extends JpaRepository<CarService, Long> {
    Optional<CarService> findById(Long id);

    boolean existsByCode(String code);
}
