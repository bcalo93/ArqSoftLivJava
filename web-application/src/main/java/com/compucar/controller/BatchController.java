package com.compucar.controller;

import com.compucar.aop.AspectExecution;
import com.compucar.dto.*;
import com.compucar.model.*;
import com.compucar.service.*;
import com.compucar.service.exceptions.*;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;

@Slf4j
@RestController
@RequestMapping(value = "/batch")
public class BatchController {

    @Autowired
    private EntityDtoConverter<Mechanic, MechanicDto> entityDtoConverter;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private ClientService clientService;

    @Autowired
    private MechanicService mechanicService;

    @Autowired
    private WorkshopService workshopService;

    @Autowired
    private ReaderService readerService;

    @Autowired
    private CarServiceService carServiceService;

    @Autowired
    private EventService eventService;

    @PostMapping
    @AspectExecution
    public void create(@RequestBody Batch batch) throws EntityNullException, RequiredFieldMissingException, DuplicateElementException, NotFoundException, InvalidFieldValueException {
        log.info("insert clients from batch");
        if(batch.getClients() != null) {
            for (Client c : batch.getClients()) {
                clientService.addClient(c);
            }
        }

        log.info("insert mechanics from batch");
        if(batch.getMechanics() != null) {
            List<Mechanic> mechanics = entityDtoConverter.convertToEntities(batch.getMechanics());
            for (Mechanic m : mechanics) {
                mechanicService.addMechanic(m);
            }
        }

        log.info("insert workshops from batch");
        if(batch.getWorkshops() != null) {
            for (Workshop w : batch.getWorkshops()) {
                workshopService.addWorkshop(w);
            }
        }

        log.info("insert readers from batch");
        if(batch.getReaders() != null) {
            for (ReaderDto readerDto : batch.getReaders()) {
                Reader reader = modelMapper.map(readerDto, Reader.class);
                readerService.addReader(reader);
            }
        }

        log.info("insert services from batch");
        if(batch.getServices() != null) {
            for (CarServiceDto carServiceDto : batch.getServices()) {
                CarService service = this.convertCarServiceToEntity(carServiceDto);
                carServiceService.addService(service);

                List<EventDto> serviceEvents = carServiceDto.getEvents();
                if (serviceEvents != null) {
                    for (EventDto event : serviceEvents) {
                        event.setServiceCode(service.getCode());
                        eventService.postEvent(event);
                    }
                }
            }
        }
    }

    private CarService convertCarServiceToEntity(CarServiceDto serviceDto) {
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
