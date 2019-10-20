package com.compucar.dao;

import com.compucar.model.ServiceExecution;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ServiceExecutionDao extends JpaRepository<ServiceExecution, Long> {
}
