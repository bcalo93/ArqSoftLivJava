package com.compucar.dao;

import com.compucar.model.OperationLog;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OperationLogDao extends JpaRepository<OperationLog, Long> {
    List<OperationLog> findByOrderByRegisterDateDesc();
}
