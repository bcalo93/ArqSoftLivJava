package com.compucar.service;

import com.compucar.model.Workshop;
import com.compucar.service.exceptions.DuplicateElementException;
import com.compucar.service.exceptions.InvalidFieldValueException;
import com.compucar.service.exceptions.NotFoundException;
import com.compucar.service.exceptions.RequiredFieldMissingException;

import java.util.List;

public interface WorkshopService {
    List<Workshop> listWorkshops();

    Workshop getWorkshop(Long id) throws NotFoundException;

    Workshop addWorkshop(Workshop workshop) throws DuplicateElementException, RequiredFieldMissingException;

    Workshop updateWorkshop(Workshop workshop) throws NotFoundException, RequiredFieldMissingException, InvalidFieldValueException;

    void removeWorkshop(Long id) throws NotFoundException;
}
