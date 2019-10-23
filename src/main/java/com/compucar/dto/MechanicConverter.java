package com.compucar.dto;

import com.compucar.model.Mechanic;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

@Component
public class MechanicConverter extends EntityDtoConverterBase<Mechanic, MechanicDto> {

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public MechanicDto convertToDto(Mechanic mechanic) {
        MechanicDto mechanicDto = modelMapper.map(mechanic, MechanicDto.class);
        Date startDate = Date.from(mechanic.getStartDate().atZone(ZoneId.systemDefault()).toInstant());
        mechanicDto.setStartDate(startDate);
        return mechanicDto;
    }

    @Override
    public Mechanic convertToEntity(MechanicDto mechanicDto) {
        Mechanic mechanic = modelMapper.map(mechanicDto, Mechanic.class);
        LocalDateTime startDate = mechanicDto.getStartDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
        mechanic.setStartDate(startDate);
        return mechanic;
    }
}
