package com.compucar.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.time.LocalDateTime;

@EqualsAndHashCode(callSuper = true)
@Data
@Entity
public class CarService extends MaintenanceItem {
    private LocalDateTime date;
    private int serviceTime;
    private double cost;

    @ManyToOne
    @JoinColumn
    private Client client;

    @ManyToOne
    @JoinColumn
    private Mechanic mechanic;

    @ManyToOne
    @JoinColumn
    private Reader reader;

    @ManyToOne
    @JoinColumn
    private Workshop workshop;

    public void applyDiscount(int percentage) {
        this.cost *= ((double) (100 - percentage) / 100);
    }
}
