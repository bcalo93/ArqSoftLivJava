package com.compucar.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Entity;
import java.time.LocalDateTime;

@EqualsAndHashCode(callSuper = true)
@Data
@Entity
public class OperationLog extends DataEntity {
    private String username;
    private String serviceName;
    private LocalDateTime registerDate;
}
