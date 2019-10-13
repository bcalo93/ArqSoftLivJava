package com.compucar.dao;

import com.compucar.model.Mechanic;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MechanicDao extends JpaRepository<Mechanic, Long> {
    Mechanic findByNumber(int number);
}
