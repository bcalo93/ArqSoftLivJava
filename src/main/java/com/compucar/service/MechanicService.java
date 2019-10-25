package com.compucar.service;

import com.compucar.model.Mechanic;
import com.compucar.service.exceptions.*;

import java.util.List;

public interface MechanicService {
    Mechanic addMechanic(Mechanic mechanic) throws DuplicateElementException, EntityNullException, RequiredFieldMissingException;

    Mechanic updateMechanic(Long id, Mechanic mechanic) throws NotFoundException, IdNullException, EntityNullException;

    void deleteMechanic(Long id) throws IdNullException;

    Mechanic getMechanic(Long id) throws NotFoundException, IdNullException;

    List<Mechanic> getAllMechanic();

}
