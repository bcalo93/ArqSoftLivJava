package com.compucar.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.util.Calendar;

@EqualsAndHashCode(callSuper = true)
@Data
@Entity
public class Service extends  MaintenanceItem {
    private String code;
    private Calendar date;
    private int serviceTime;
    private double cost;

    @ManyToOne
    @JoinColumn
    private Client client;

    @ManyToOne
    @JoinColumn
    private Reader reader;

    @ManyToOne
    @JoinColumn
    private Workshop workshop;
}
