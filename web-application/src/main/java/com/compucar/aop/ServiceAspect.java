package com.compucar.aop;

import com.compucar.builder.OperationLogBuilder;
import com.compucar.builder.ServiceExecutionBuilder;
import com.compucar.model.OperationLog;
import com.compucar.model.ServiceExecution;
import com.compucar.service.OperationLogService;
import com.compucar.service.ServiceExecutionService;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;

@Aspect
@Component
@Slf4j
public class ServiceAspect {

    @Autowired
    private HttpServletRequest request;

    @Autowired
    private OperationLogService operationLogService;

    @Autowired
    private ServiceExecutionService serviceExecutionService;

    @Around("@annotation(AspectExecution)")
    public Object aroundService(ProceedingJoinPoint joinPoint) throws Throwable {
        Object result;
        long initTime = System.currentTimeMillis();
        String methodName = joinPoint.getSignature().getName();
        this.saveServiceCall(methodName);
        try {
            result = joinPoint.proceed();
        } finally {
            this.saveServiceExecution(methodName, System.currentTimeMillis() - initTime);
        }
        return result;
    }

    private void saveServiceCall(String serviceName) {
        OperationLog operationLog = new OperationLogBuilder()
                .serviceName(serviceName)
                .username(request.getHeader("username"))
                .registerDate(LocalDateTime.now())
                .build();
        this.operationLogService.addOperationLog(operationLog);
    }

    private void saveServiceExecution(String serviceName, long executionTime) {
        ServiceExecution serviceExecution = new ServiceExecutionBuilder()
                .serviceName(serviceName)
                .executionTime(executionTime)
                .registerDate(LocalDateTime.now())
                .build();
        this.serviceExecutionService.addServiceExecution(serviceExecution);
    }
}
