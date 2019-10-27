package com.compucar.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;

@EqualsAndHashCode(callSuper = true)
@Data
@MappedSuperclass
public abstract class Contact extends DataEntity {

    @Column(unique = true)
    private Integer number;

    private String name;
    private String phone;
}
