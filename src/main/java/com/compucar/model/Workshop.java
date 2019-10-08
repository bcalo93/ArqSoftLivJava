package com.compucar.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Entity;

@EqualsAndHashCode(callSuper = true)
@Data
@Entity
public class Workshop extends MaintenanceItem {
    private String code;
    private String name;
    private String address;
    private String city;
}
