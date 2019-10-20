package com.compucar.service;

import com.compucar.dao.OperationLogDao;
import com.compucar.model.OperationLog;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Slf4j
public class OperationLogServiceImp implements OperationLogService {

    @Autowired
    private OperationLogDao operationLogDao;

    @Override
    public void addOperationLog(OperationLog operationLog) {
        if (isValidOperationLog(operationLog)) {
            operationLogDao.save(operationLog);
        } else {
            log.warn("Invalid operationLog object {}", operationLog);
        }
    }

    @Override
    public List<OperationLog> getAllOperationLogs() {
        return operationLogDao.findByOrderByRegisterDateDesc();
    }

    private boolean isValidOperationLog(OperationLog operationLog) {
        return operationLog != null && operationLog.getUsername() != null && operationLog.getRegisterDate() != null &&
                operationLog.getServiceName() != null;
    }
}