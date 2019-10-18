package com.compucar.service;

import com.compucar.dao.*;
import com.compucar.model.*;
import com.compucar.service.exceptions.DuplicateElementException;
import com.compucar.service.exceptions.InvalidFieldValueException;
import com.compucar.service.exceptions.NotFoundException;
import com.compucar.service.exceptions.RequiredFieldMissingException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
public class CarServiceServiceImpl implements CarServiceService {

    @Autowired
    private CarServiceDao carServiceDao;

    @Autowired
    private ClientDao clientDao;

    @Autowired
    private MechanicDao mechanicDao;

    @Autowired
    private ReaderDao readerDao;

    @Autowired
    private WorkshopDao workshopDao;

    @Override
    public List<CarService> listServices() {
        log.info("listing services ");
        return carServiceDao.findAll();
    }

    @Override
    public CarService getService(Long id) throws NotFoundException {
        log.info("getting service: {}", id);
        return carServiceDao.findById(id).orElseThrow(() -> new NotFoundException("Service with id " + id));
    }

    @Override
    public CarService addService(CarService service) throws RequiredFieldMissingException, DuplicateElementException, NotFoundException, InvalidFieldValueException {
        log.info("adding service {} ", service);
        validateServiceAdd(service);

        if(service.getClient() != null) {
            Integer clientNumber = service.getClient().getNumber();
            Client client = clientDao.findByNumber(clientNumber).orElseThrow(() -> new NotFoundException("Client with number " + clientNumber));
            service.setClient(client);
        }
        if(service.getReader() != null) {
            String readerCode = service.getReader().getCode();
            Reader reader = readerDao.findByCode(readerCode).orElseThrow(() -> new NotFoundException("Reader with code " + readerCode));
            service.setReader(reader);
        }
        if(service.getWorkshop() != null) {
            String workshopCode = service.getWorkshop().getCode();
            Workshop workshop = workshopDao.findByCode(workshopCode).orElseThrow(() -> new NotFoundException("Workshop with code " + workshopCode));
            service.setWorkshop(workshop);
        }
        if(service.getMechanic() != null) {
            Integer mechanicNumber = service.getMechanic().getNumber();
            Mechanic mechanic = mechanicDao.findByNumber(mechanicNumber).orElseThrow(() -> new NotFoundException("Mechanic with number " + mechanicNumber));
            service.setMechanic(mechanic);
        }
        return carServiceDao.save(service);
    }

    private void validateServiceAdd(CarService service) throws DuplicateElementException, RequiredFieldMissingException, InvalidFieldValueException {
        log.info("validating service {} ", service);

        if(service.getId() != null && carServiceDao.exists(service.getId())) {
            log.info("duplicate service id {} ", service.getId());
            throw new DuplicateElementException("Service with id " + service.getId());
        }
        if(service.getCode() != null && carServiceDao.existsByCode(service.getCode())) {
            log.info("duplicate service code {} ", service.getCode());
            throw new DuplicateElementException("Service with code " + service.getCode());
        }
        if(service.getCode() == null || service.getCode().trim().isEmpty()) {
            log.info("service code missing");
            throw new RequiredFieldMissingException("Code");
        }
        if(service.getDate() == null || service.getDate().isAfter(LocalDateTime.now())) {
            log.info("service date after today");
            throw new InvalidFieldValueException("Date after today");
        }
        if(service.getServiceTime() < 0) {
            log.info("service duration is negative");
            throw new InvalidFieldValueException("Negative duration value");
        }
        if(service.getCost() < 0) {
            log.info("service cost is negative");
            throw new InvalidFieldValueException("Negative cost value");
        }
        if(service.getClient() == null) {
            log.info("service client missing");
            throw new RequiredFieldMissingException("Client");
        }
        if(service.getReader() == null) {
            log.info("service reader missing");
            throw new RequiredFieldMissingException("Reader");
        }
        if(service.getWorkshop() == null) {
            log.info("service workshop missing");
            throw new RequiredFieldMissingException("Workshop");
        }
        if(service.getMechanic() == null) {
            log.info("service mechanic missing");
            throw new RequiredFieldMissingException("Mechanic");
        }
    }
}
