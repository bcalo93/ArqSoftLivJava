package com.compucar.service;

import com.compucar.model.CarService;
import com.compucar.model.Diagnose;
import com.compucar.service.exceptions.DuplicateElementException;
import com.compucar.service.exceptions.InvalidFieldValueException;
import com.compucar.service.exceptions.NotFoundException;
import com.compucar.service.exceptions.RequiredFieldMissingException;

import java.time.LocalDate;
import java.util.List;

public interface CarServiceService {
    List<CarService> listServices();

    CarService getService(Long id) throws NotFoundException;

    List<CarService> getServicesBetweenDates(LocalDate from, LocalDate to);

    List<CarService> getServicesBetweenDatesWithReader(LocalDate from, LocalDate to, String readerCode);

    List<CarService> getServicesWithReader(String readerCode);

    List<CarService> getServicesForGivenMonth(Integer month) throws InvalidFieldValueException;

    CarService addService(CarService service) throws RequiredFieldMissingException, DuplicateElementException, NotFoundException, InvalidFieldValueException;

    CarService addDiagnose(String serviceCode, Diagnose diagnose) throws NotFoundException, RequiredFieldMissingException;
}
