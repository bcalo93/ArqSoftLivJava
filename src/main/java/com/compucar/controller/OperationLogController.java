package com.compucar.controller;

import com.compucar.aop.AspectExecution;
import com.compucar.dto.EntityDtoConverter;
import com.compucar.dto.OperationLogDto;
import com.compucar.model.OperationLog;
import com.compucar.service.OperationLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/operationlogs")
public class OperationLogController {

    @Autowired
    private OperationLogService operationLogService;

    @Autowired
    private EntityDtoConverter<OperationLog, OperationLogDto> entityDtoConverter;

    @GetMapping
    @ResponseStatus(value = HttpStatus.OK)
    @AspectExecution
    public List<OperationLogDto> getAllOperations() {
        return entityDtoConverter.convertToDtos(operationLogService.getAllOperationLogs());
    }

}
