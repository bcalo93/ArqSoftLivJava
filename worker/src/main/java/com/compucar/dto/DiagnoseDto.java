package com.compucar.dto;

import lombok.Data;

@Data
public class DiagnoseDto {
    private String eventName;
    private String result;

    public DiagnoseDto() {

    }

    public DiagnoseDto(String eventName, String result) {
        this.eventName = eventName;
        this.result = result;
    }

}
