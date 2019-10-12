package com.compucar.service;

import com.compucar.model.Workshop;
import com.compucar.service.exceptions.DuplicateElementException;
import com.compucar.service.exceptions.NotFoundException;
import com.compucar.service.exceptions.RequiredFieldMissingException;

import java.util.List;

public interface WorkshopService {
    List<Workshop> listWorkshops();

    Workshop getWorkshop(Long id) throws NotFoundException;

    void addWorkshop(Workshop workshop) throws DuplicateElementException, RequiredFieldMissingException;

    void updateWorkshop(Workshop workshop) throws NotFoundException, RequiredFieldMissingException, DuplicateElementException;

    void removeWorkshop(Long id) throws NotFoundException;
}
