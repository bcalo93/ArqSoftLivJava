package com.compucar.builder;

import com.compucar.model.ServiceExecution;

import java.time.LocalDateTime;

public class ServiceExecutionBuilder {
    private ServiceExecution serviceExecution;

    public ServiceExecutionBuilder() {
        this.serviceExecution = new ServiceExecution();
    }

    public ServiceExecutionBuilder id(Long id) {
        this.serviceExecution.setId(id);
        return this;
    }

    public ServiceExecutionBuilder serviceName(String serviceName) {
        this.serviceExecution.setServiceName(serviceName);
        return this;
    }

    public ServiceExecutionBuilder executionTime(Long executionTime) {
        this.serviceExecution.setExecutionTime(executionTime);
        return this;
    }

    public ServiceExecutionBuilder registerDate(LocalDateTime registerDate) {
        this.serviceExecution.setRegisterDate(registerDate);
        return this;
    }

    public ServiceExecution build() {
        return this.serviceExecution;
    }
}
