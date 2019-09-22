package com.compucar.model;

import lombok.Data;

@Data
public class Workshop extends DataEntity {
    private String code;
    private String name;
    private String address;
    private String city;

}
