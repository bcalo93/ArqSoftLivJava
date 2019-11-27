package com.compucar.controller;

import com.compucar.aop.AspectExecution;
import com.compucar.dto.CarServiceDto;
import com.compucar.dto.EventDto;
import com.compucar.model.*;
import com.compucar.service.CarServiceService;
import com.compucar.service.EventService;
import com.compucar.service.exceptions.DuplicateElementException;
import com.compucar.service.exceptions.InvalidFieldValueException;
import com.compucar.service.exceptions.NotFoundException;
import com.compucar.service.exceptions.RequiredFieldMissingException;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;

@Slf4j
@RestController
@RequestMapping(value = "/services")
public class CarServiceController {

    @Autowired
    private CarServiceService carServiceService;

    @Autowired
    private EventService eventService;

    @Autowired
    private ModelMapper modelMapper;

    @GetMapping
    @AspectExecution
    public List<CarService> listServices() {
        log.info("list all services");
        return carServiceService.listServices();
    }

    @GetMapping(value = "/{serviceId}")
    @AspectExecution
    public CarService getService(@PathVariable("serviceId") Long id) throws NotFoundException {
        log.info("getService {}", id);
        CarService service = carServiceService.getService(id);
        log.info("service {} ", service);

        return service;
    }

    @PostMapping
    @AspectExecution
    public CarService saveService(@RequestBody CarServiceDto serviceDto) throws
            RequiredFieldMissingException,
            DuplicateElementException,
            NotFoundException,
            InvalidFieldValueException {
        log.info("received  {}", serviceDto);
        CarService service = convertToEntity(serviceDto);
        CarService serviceAdded = carServiceService.addService(service);
        List<EventDto> serviceEvents = serviceDto.getEvents();
        if(serviceEvents != null) {
            for (EventDto event : serviceEvents) {
                event.setServiceCode(service.getCode());
                eventService.postEvent(event);
            }
        }

        return serviceAdded;
    }

    @PostMapping(path = "/{serviceCode}/diagnoses")
    public CarService addDiagnoseToService(@PathVariable("serviceCode") String serviceCode, @RequestBody Diagnose diagnose)
            throws NotFoundException, RequiredFieldMissingException {
        return carServiceService.addDiagnose(serviceCode, diagnose);
    }

    private CarService convertToEntity(CarServiceDto serviceDto) {
        LocalDateTime serviceDate = serviceDto.getDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
        Client serviceClient = new Client(serviceDto.getClientCode());
        Mechanic serviceMechanic = new Mechanic(serviceDto.getMechanicCode());
        Reader serviceReader = new Reader(serviceDto.getReaderCode());
        Workshop serviceWorkshop = new Workshop(serviceDto.getWorkshopCode());

        CarService service = modelMapper.map(serviceDto, CarService.class);
        service.setDate(serviceDate);
        service.setClient(serviceClient);
        service.setMechanic(serviceMechanic);
        service.setReader(serviceReader);
        service.setWorkshop(serviceWorkshop);

        return service;
    }
}
