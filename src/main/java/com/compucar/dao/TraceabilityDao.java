package com.compucar.dao;

import com.compucar.model.Traceability;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TraceabilityDao extends JpaRepository<Traceability, Long> {
}
