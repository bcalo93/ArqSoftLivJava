package com.compucar.aop;

import com.compucar.builder.TraceabilityBuilder;
import com.compucar.model.Traceability;
import com.compucar.service.TraceabilityService;
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
    private TraceabilityService traceabilityService;

    @Around("@annotation(AspectExecution)")
    public Object aroundService(ProceedingJoinPoint joinPoint) throws Throwable {
        this.saveServiceCall(joinPoint.getSignature().getName());
        return joinPoint.proceed();
    }

    private void saveServiceCall(String serviceName) {
        Traceability traceability = new TraceabilityBuilder()
                .serviceName(serviceName)
                .username(request.getHeader("username"))
                .registerDate(LocalDateTime.now())
                .build();
        this.traceabilityService.addTraceability(traceability);
    }
}
