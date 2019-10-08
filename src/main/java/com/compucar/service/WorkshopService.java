package com.compucar.service;

import com.compucar.model.Workshop;

import java.util.List;

public interface WorkshopService {
    void addWorkshop(Workshop workshop);

    void update(Workshop workshop);

    void removeWorkshop(Workshop workshop);

    Workshop getWorkshop(Long id);

    List<Workshop> listWorkshops();
}
