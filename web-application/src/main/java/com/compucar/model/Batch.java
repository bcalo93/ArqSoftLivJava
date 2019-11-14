package com.compucar.model;

import com.compucar.dto.CarServiceDto;
import com.compucar.dto.MechanicDto;
import com.compucar.dto.ReaderDto;
import lombok.Data;

import java.util.List;

@Data
public class Batch {

    private List<Client> clients;
    private List<MechanicDto> mechanics;
    private List<Workshop> workshops;
    private List<ReaderDto> readers;
    private List<CarServiceDto> services;
}
