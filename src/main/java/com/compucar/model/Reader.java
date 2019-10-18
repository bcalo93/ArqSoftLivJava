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
    private String brand;
    private int actualTimeUse;
    private int batteryLife;

    @ManyToOne
    @JoinColumn
    private Workshop workshop;

    public Reader() {
        actualTimeUse = 0;
    }

    public Reader(String code) {
        this();
        this.setCode(code);
    }
}
