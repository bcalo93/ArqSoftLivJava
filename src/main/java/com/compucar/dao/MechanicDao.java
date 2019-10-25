package com.compucar.dao;

import com.compucar.model.Mechanic;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MechanicDao extends JpaRepository<Mechanic, Long> {
    Optional<Mechanic> findByNumber(Integer code);
}
