package com.compucar.controller;

import com.compucar.aop.AspectExecution;
import com.compucar.dto.ServiceReportDto;
import com.compucar.service.ServiceExecutionService;
import com.compucar.service.exceptions.RequiredFieldMissingException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RequestParam;


import java.time.LocalDate;

@Slf4j
@RestController
@RequestMapping("usagereports")
public class ServiceExecutionController {

    @Autowired
    private ServiceExecutionService serviceExecutionService;

    @GetMapping
    @ResponseStatus(value = HttpStatus.OK)
    @AspectExecution
    public ServiceReportDto getServiceReport(@RequestParam(name = "date", required = false)
                                                 @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date)
            throws RequiredFieldMissingException {
        if(date == null) {
            date = LocalDate.now();
        }
        return this.serviceExecutionService.getUsageReport(date);
    }
}
