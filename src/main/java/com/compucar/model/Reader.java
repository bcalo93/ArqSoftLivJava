package com.compucar.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@EqualsAndHashCode(callSuper = true)
@Data
@Entity
public class Reader extends MaintenanceItem {
    private String code;
    private String brand;
    private int batteryLife;
    private int actualTimeUse;

    @ManyToOne
    @JoinColumn
    private Workshop workshop;
}
