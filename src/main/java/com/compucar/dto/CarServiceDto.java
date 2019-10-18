package com.compucar.dto;

import lombok.Data;

import java.util.Date;

@Data
public class CarServiceDto {
    private Long id;
    private String code;
    private Date date;
    private int serviceTime;
    private Long cost;
    private Integer clientCode;
    private Integer mechanicCode;
    private String readerCode;
    private String workshopCode;
}
