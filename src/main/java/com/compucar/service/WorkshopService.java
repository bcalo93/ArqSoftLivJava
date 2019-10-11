package com.compucar.service;

import com.compucar.model.Workshop;
import com.compucar.service.exceptions.NotFoundException;

import java.util.List;

public interface WorkshopService {
    List<Workshop> listWorkshops();

    Workshop getWorkshop(Long id) throws NotFoundException;

    void addWorkshop(Workshop workshop);

    void updateWorkshop(Workshop workshop) throws NotFoundException;

    void removeWorkshop(Long id) throws NotFoundException;
}
