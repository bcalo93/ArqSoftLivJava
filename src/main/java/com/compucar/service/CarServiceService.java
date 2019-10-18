package com.compucar.service;

import com.compucar.model.CarService;
import com.compucar.service.exceptions.DuplicateElementException;
import com.compucar.service.exceptions.InvalidFieldValueException;
import com.compucar.service.exceptions.NotFoundException;
import com.compucar.service.exceptions.RequiredFieldMissingException;

import java.util.List;

public interface CarServiceService {
    List<CarService> listServices();

    CarService getService(Long id) throws NotFoundException;

    void addService(CarService service) throws RequiredFieldMissingException, DuplicateElementException, NotFoundException, InvalidFieldValueException;
}
