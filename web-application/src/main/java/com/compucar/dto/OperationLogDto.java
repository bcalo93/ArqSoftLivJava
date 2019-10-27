package com.compucar.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

@Data
public class OperationLogDto {
    private Long id;
    private String username;
    private String serviceName;

    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Date registerDate;
}
