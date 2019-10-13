package com.compucar.service;

import com.compucar.model.Mechanic;
import com.compucar.service.exceptions.DuplicateElementException;
import com.compucar.service.exceptions.EntityNullException;
import com.compucar.service.exceptions.IdNullException;
import com.compucar.service.exceptions.NotFoundException;

import java.util.List;

public interface MechanicService {
    Mechanic addMechanic(Mechanic mechanic) throws DuplicateElementException, EntityNullException;

    Mechanic updateMechanic(Long id, Mechanic mechanic) throws NotFoundException, IdNullException, EntityNullException;

    void deleteMechanic(Long id) throws IdNullException;

    Mechanic getMechanic(Long id) throws NotFoundException, IdNullException;

    List<Mechanic> getAllMechanic();

}
