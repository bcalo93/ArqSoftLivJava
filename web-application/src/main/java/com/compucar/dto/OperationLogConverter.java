package com.compucar.dto;

import com.compucar.model.OperationLog;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

@Component
public class OperationLogConverter extends EntityDtoConverterBase<OperationLog, OperationLogDto> {

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public OperationLogDto convertToDto(OperationLog operationLog) {
        OperationLogDto operationLogDto = modelMapper.map(operationLog, OperationLogDto.class);
        Date registerDate = Date.from(operationLog.getRegisterDate().atZone((ZoneId.systemDefault())).toInstant());
        operationLogDto.setRegisterDate(registerDate);
        return operationLogDto;
    }

    @Override
    public OperationLog convertToEntity(OperationLogDto operationLogDto) {
        OperationLog operationLog = modelMapper.map(operationLogDto, OperationLog.class);
        LocalDateTime registerDate = operationLogDto.getRegisterDate().toInstant()
                .atZone(ZoneId.systemDefault()).toLocalDateTime();
        operationLog.setRegisterDate(registerDate);
        return operationLog;
    }
}
