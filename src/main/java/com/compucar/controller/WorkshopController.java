package com.compucar.controller;

import com.compucar.model.Workshop;
import com.compucar.service.WorkshopService;
import com.compucar.service.exceptions.DuplicateElementException;
import com.compucar.service.exceptions.NotFoundException;
import com.compucar.service.exceptions.RequiredFieldMissingException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping(value = "/workshops")
public class WorkshopController {

    @Autowired
    private WorkshopService workshopService;

    @GetMapping
    public List<Workshop> listWorkshops() {
        log.info("list all workshops");
        return workshopService.listWorkshops();
    }

    @GetMapping(value = "/{workshopId}")
    public Workshop getWorkshop(@PathVariable("workshopId") Long id) throws NotFoundException {
        log.info("getWorkshop {}", id);
        Workshop workshop = workshopService.getWorkshop(id);
        log.info("workshop {} ", workshop);

        return workshop;
    }

    @PostMapping
    public Workshop saveWorkshop(@RequestBody Workshop workshop) throws DuplicateElementException, RequiredFieldMissingException {
        log.info("received  {}", workshop);
        return workshopService.addWorkshop(workshop);
    }

    @PutMapping
    public void updateWorkshop(@RequestBody Workshop workshop) throws NotFoundException, RequiredFieldMissingException, DuplicateElementException {
        log.info("received  {}", workshop);
        workshopService.updateWorkshop(workshop);
    }

    @DeleteMapping(value = "/{workshopId}")
    public void removeWorkshop(@PathVariable("workshopId") Long id) throws NotFoundException  {
        log.info("delete workshop  {}", id);
        workshopService.removeWorkshop(id);
    }
}
