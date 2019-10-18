package com.compucar.dto;

import lombok.Data;

import java.util.Date;

@Data
public class MechanicDto {
    private Long id;
    private Integer code;
    private String name;
    private String phone;
    private Date startDate;
}
