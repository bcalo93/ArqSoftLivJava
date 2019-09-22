package com.compucar.model;

import lombok.Data;

import java.util.Calendar;

@Data
public class Service extends DataEntity {
    private String code;
    private Calendar date;
    private int serviceTime;
    private double cost;
    private Client client;
    private Reader reader;
    private Workshop workshop;
}
