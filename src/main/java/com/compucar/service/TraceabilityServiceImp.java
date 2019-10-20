package com.compucar.service;

import com.compucar.dao.TraceabilityDao;
import com.compucar.model.Traceability;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class TraceabilityServiceImp implements TraceabilityService {

    @Autowired
    private TraceabilityDao traceabilityDao;

    @Override
    public void addTraceability(Traceability traceability) {
        if(isValidTraceability(traceability)) {
            traceabilityDao.save(traceability);

        } else {
            log.warn("Invalid traceability object {}", traceability);
        }
    }

    private boolean isValidTraceability(Traceability traceability) {
        return traceability != null && traceability.getUsername() != null && traceability.getRegisterDate() != null &&
                traceability.getServiceName() != null;
    }
}
