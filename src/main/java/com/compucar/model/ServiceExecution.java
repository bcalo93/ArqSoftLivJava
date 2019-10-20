package com.compucar.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Entity;
import java.time.LocalDateTime;

@EqualsAndHashCode(callSuper = true)
@Data
@Entity
public class ServiceExecution extends DataEntity {
    private String serviceName;
    private Long executionTime;
    private LocalDateTime registerDate;
}