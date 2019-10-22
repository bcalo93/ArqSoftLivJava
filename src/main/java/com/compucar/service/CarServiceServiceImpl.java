package com.compucar.service;

import com.compucar.dao.*;
import com.compucar.model.*;
import com.compucar.service.exceptions.DuplicateElementException;
import com.compucar.service.exceptions.InvalidFieldValueException;
import com.compucar.service.exceptions.NotFoundException;
import com.compucar.service.exceptions.RequiredFieldMissingException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.Period;
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

    @Autowired
    private Environment env;

    private final int DISCOUNT_PERCENTAGE;

    public CarServiceServiceImpl(CarServiceDao carServiceDao, ClientDao clientDao, MechanicDao mechanicDao,
                                 ReaderDao readerDao, WorkshopDao workshopDao, Environment env) {
        this.carServiceDao = carServiceDao;
        this.clientDao = clientDao;
        this.mechanicDao = mechanicDao;
        this.readerDao = readerDao;
        this.workshopDao = workshopDao;
        this.env = env;

        DISCOUNT_PERCENTAGE = 20;
    }

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
        attachServiceAttributes(service);
        validateServiceAdd(service);
        checkForDiscount(service);
        updateReaderUsageTime(service);

        return carServiceDao.save(service);
    }

    private void attachServiceAttributes(CarService service) throws NotFoundException {
        if (service.getClient() != null) {
            Integer clientNumber = service.getClient().getNumber();
            Client client = clientDao.findByNumber(clientNumber).orElseThrow(() -> new NotFoundException("Client with number " + clientNumber));
            service.setClient(client);
        }
        if (service.getWorkshop() != null) {
            String workshopCode = service.getWorkshop().getCode();
            Workshop workshop = workshopDao.findByCode(workshopCode).orElseThrow(() -> new NotFoundException("Workshop with code " + workshopCode));
            service.setWorkshop(workshop);
        }
        if (service.getMechanic() != null) {
            Integer mechanicNumber = service.getMechanic().getNumber();
            Mechanic mechanic = mechanicDao.findByNumber(mechanicNumber).orElseThrow(() -> new NotFoundException("Mechanic with number " + mechanicNumber));
            service.setMechanic(mechanic);
        }
        if (service.getReader() != null) {
            String readerCode = service.getReader().getCode();
            Reader reader = readerDao.findByCode(readerCode).orElseThrow(() -> new NotFoundException("Reader with code " + readerCode));
            service.setReader(reader);
        }
    }

    private void updateReaderUsageTime(CarService service) {
        Reader serviceReader = service.getReader();
        serviceReader.addUseTime(service.getServiceTime());
        readerDao.save(serviceReader);
    }

    private void checkForDiscount(CarService service) {
        LocalDateTime serviceDate = service.getDate();
        LocalDateTime previousMonthDate = serviceDate.minus(Period.ofMonths(1));
        LocalDateTime from = LocalDateTime.of(previousMonthDate.getYear(), previousMonthDate.getMonth(), 1, 0, 0);
        LocalDateTime to = LocalDateTime.of(previousMonthDate.getYear(), previousMonthDate.getMonth(), 30, 23, 59);
        if (service.getClient().isACompany() && carServiceDao.countByClientAndDateBetween(service.getClient(), from, to) > 5) {
            log.info("client is a company and has more than 5 services last month, applying {} percent discount", DISCOUNT_PERCENTAGE);
            service.applyDiscount(DISCOUNT_PERCENTAGE);
        }
    }

    private void validateServiceAdd(CarService service) throws DuplicateElementException, RequiredFieldMissingException, InvalidFieldValueException {
        log.info("validating service {} ", service);
        validateNotDuplicate(service);
        validateRequiredFields(service);
        validateFieldValues(service);
    }

    private void validateNotDuplicate(CarService service) throws DuplicateElementException {
        if (service.getId() != null && carServiceDao.exists(service.getId())) {
            log.info("duplicate service id {} ", service.getId());
            throw new DuplicateElementException("Service with id " + service.getId());
        }
        if (service.getCode() != null && carServiceDao.existsByCode(service.getCode())) {
            log.info("duplicate service code {} ", service.getCode());
            throw new DuplicateElementException("Service with code " + service.getCode());
        }
    }

    private void validateRequiredFields(CarService service) throws RequiredFieldMissingException {
        if (service.getCode() == null || service.getCode().trim().isEmpty()) {
            log.info("service code missing");
            throw new RequiredFieldMissingException("Code");
        }
        if (service.getDate() == null) {
            log.info("service sate missing");
            throw new RequiredFieldMissingException("Date");
        }
        if (service.getClient() == null) {
            log.info("service client missing");
            throw new RequiredFieldMissingException("Client");
        }
        if (service.getReader() == null) {
            log.info("service reader missing");
            throw new RequiredFieldMissingException("Reader");
        }
        if (service.getWorkshop() == null) {
            log.info("service workshop missing");
            throw new RequiredFieldMissingException("Workshop");
        }
        if (service.getMechanic() == null) {
            log.info("service mechanic missing");
            throw new RequiredFieldMissingException("Mechanic");
        }
    }

    private void validateFieldValues(CarService service) throws InvalidFieldValueException {
        int minBatteryLife = Integer.valueOf(env.getProperty("minBatteryLife"));
        LocalDateTime serviceDate = service.getDate();
        Reader serviceReader = service.getReader();
        Workshop serviceWorkshop = service.getWorkshop();
        Mechanic serviceMechanic = service.getMechanic();
        Client serviceClient = service.getClient();

        LocalDateTime startOfDay = LocalDateTime.of(serviceDate.getYear(), serviceDate.getMonth(), serviceDate.getDayOfMonth(), 0, 0);
        LocalDateTime endOfDay = LocalDateTime.of(serviceDate.getYear(), serviceDate.getMonth(), serviceDate.getDayOfMonth(), 23, 59);

        if (serviceDate.isAfter(LocalDateTime.now())) {
            log.info("service date after today");
            throw new InvalidFieldValueException("Date after today");
        }
        if (service.getServiceTime() < 0) {
            log.info("service duration is negative");
            throw new InvalidFieldValueException("Negative duration value");
        }
        if (service.getCost() < 0) {
            log.info("service cost is negative");
            throw new InvalidFieldValueException("Negative cost value");
        }
        if (!serviceReader.isAvailable(service.getServiceTime(), minBatteryLife)) {
            log.info("service reader has insufficient battery");
            throw new InvalidFieldValueException("Reader with insufficient battery");
        }
        if (!serviceReader.getWorkshop().equals(serviceWorkshop)) {
            log.info("service reader not in selected workshop");
            throw new InvalidFieldValueException("Selected reader not in selected workshop");
        }
        if (carServiceDao.countByMechanicAndDateBetween(serviceMechanic, startOfDay, endOfDay) >= 5) {
            log.info("service mechanic already did 5 services today");
            throw new InvalidFieldValueException("Mechanic has 5 or more services done today");
        }
        if (carServiceDao.existsByMechanicAndDateBetweenAndWorkshopNot(serviceMechanic, startOfDay, endOfDay, serviceWorkshop)) {
            log.info("service mechanic already did a service on a different workshop today");
            throw new InvalidFieldValueException("Mechanic already did a service on a different workshop today");
        }
        if (serviceClient.isAPerson() && carServiceDao.existsByClientAndDateBetween(serviceClient, startOfDay, endOfDay)) {
            log.info("service client is a person and already did a service on this date");
            throw new InvalidFieldValueException("Client is a person and already did a service on this date");
        }
    }
}
