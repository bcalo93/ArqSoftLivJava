package com.compucar.builder;

import com.compucar.model.OperationLog;

import java.time.LocalDateTime;

public class OperationLogBuilder {
    private OperationLog operationLog;

    public OperationLogBuilder() {
        this.operationLog = new OperationLog();
    }

    public OperationLogBuilder id(Long id) {
        this.operationLog.setId(id);
        return this;
    }

    public OperationLogBuilder username(String username) {
        this.operationLog.setUsername(username);
        return this;
    }

    public OperationLogBuilder serviceName(String serviceName) {
        this.operationLog.setServiceName(serviceName);
        return this;
    }

    public OperationLogBuilder registerDate(LocalDateTime registerDate) {
        this.operationLog.setRegisterDate(registerDate);
        return this;
    }

    public OperationLog build() {
        return this.operationLog;
    }
}
