package com.compucar.builder;

import com.compucar.model.Traceability;

import java.time.LocalDateTime;

public class TraceabilityBuilder {
    private Traceability traceability;

    public TraceabilityBuilder() {
        this.traceability = new Traceability();
    }

    public TraceabilityBuilder username(String username) {
        this.traceability.setUsername(username);
        return this;
    }

    public TraceabilityBuilder serviceName(String serviceName) {
        this.traceability.setServiceName(serviceName);
        return this;
    }

    public TraceabilityBuilder registerDate(LocalDateTime registerDate) {
        this.traceability.setRegisterDate(registerDate);
        return this;
    }

    public Traceability build() {
        return this.traceability;
    }
}
