package com.compucar.aop;

import com.compucar.builder.OperationLogBuilder;
import com.compucar.model.OperationLog;
import com.compucar.service.OperationLogService;
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

    @Around("@annotation(AspectExecution)")
    public Object aroundService(ProceedingJoinPoint joinPoint) throws Throwable {
        this.saveServiceCall(joinPoint.getSignature().getName());
        return joinPoint.proceed();
    }

    private void saveServiceCall(String serviceName) {
        OperationLog operationLog = new OperationLogBuilder()
                .serviceName(serviceName)
                .username(request.getHeader("username"))
                .registerDate(LocalDateTime.now())
                .build();
        this.operationLogService.addOperationLog(operationLog);
    }
}
