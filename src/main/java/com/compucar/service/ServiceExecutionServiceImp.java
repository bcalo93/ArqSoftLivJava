package com.compucar.service;

import com.compucar.dao.ServiceExecutionDao;
import com.compucar.model.ServiceExecution;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class ServiceExecutionServiceImp implements ServiceExecutionService {

    @Autowired
    private ServiceExecutionDao serviceExecutionDao;

    @Override
    public void addServiceExecution(ServiceExecution serviceExecution) {
        if(isValidServiceExecution(serviceExecution)) {
            serviceExecutionDao.save(serviceExecution);

        } else {
            log.warn("Invalid operationLog object {}", serviceExecution);
        }
    }

    private boolean isValidServiceExecution(ServiceExecution serviceExecution) {
        return serviceExecution != null && serviceExecution.getExecutionTime() != null &&
                serviceExecution.getRegisterDate() != null && serviceExecution.getServiceName() != null;
    }
}
