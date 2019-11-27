package com.compucar.service;

import com.compucar.model.CarService;
import com.compucar.model.Diagnose;
import com.compucar.service.exceptions.DuplicateElementException;
import com.compucar.service.exceptions.InvalidFieldValueException;
import com.compucar.service.exceptions.NotFoundException;
import com.compucar.service.exceptions.RequiredFieldMissingException;

import java.time.LocalDateTime;
import java.util.List;

public interface CarServiceService {
    List<CarService> listServices();

    CarService getService(Long id) throws NotFoundException;

    List<CarService> getServicesBetweenDates(LocalDateTime from, LocalDateTime to);

    CarService addService(CarService service) throws RequiredFieldMissingException, DuplicateElementException, NotFoundException, InvalidFieldValueException;

    CarService addDiagnose(String serviceCode, Diagnose diagnose) throws NotFoundException, RequiredFieldMissingException;
}
