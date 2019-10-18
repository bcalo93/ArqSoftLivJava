package com.compucar.controller;

import com.compucar.model.CarService;
import com.compucar.service.CarServiceService;
import com.compucar.service.exceptions.DuplicateElementException;
import com.compucar.service.exceptions.InvalidFieldValueException;
import com.compucar.service.exceptions.NotFoundException;
import com.compucar.service.exceptions.RequiredFieldMissingException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping(value = "/services")
public class CarServiceController {

    @Autowired
    private CarServiceService carServiceService;

    @GetMapping
    public List<CarService> listServices() {
        log.info("list all services");
        return carServiceService.listServices();
    }

    @GetMapping(value = "/{serviceId}")
    public CarService getService(@PathVariable("serviceId") Long id) throws NotFoundException {
        log.info("getService {}", id);
        CarService service = carServiceService.getService(id);
        log.info("service {} ", service);

        return service;
    }

    @PostMapping
    public void saveService(@RequestBody CarService service) throws
            RequiredFieldMissingException,
            DuplicateElementException,
            NotFoundException,
            InvalidFieldValueException {
        log.info("received  {}", service);
        carServiceService.addService(service);
    }
}
