package com.compucar.service;

import com.compucar.model.OperationLog;

import java.util.List;

public interface OperationLogService {
    void addOperationLog(OperationLog operationLog);

    List<OperationLog> getAllOperationLogs();
}