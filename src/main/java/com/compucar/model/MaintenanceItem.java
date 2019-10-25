package com.compucar.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;

@EqualsAndHashCode(callSuper = true)
@Data
@MappedSuperclass
public abstract class MaintenanceItem extends DataEntity {

    @Column(unique = true)
    private String code;
}
