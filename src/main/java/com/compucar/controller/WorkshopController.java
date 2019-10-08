package com.compucar.controller;

import com.compucar.model.Workshop;
import com.compucar.service.WorkshopService;
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
        log.info("list");
        return workshopService.listWorkshops();
    }

    //@ResponseBody
    @GetMapping(value = "/{workshopId}")
    public Workshop getWorkshop(@PathVariable("workshopId") Long id) {
        log.info("getWorkshop {}", id);
        Workshop workshop = workshopService.getWorkshop(id);

        log.info("workshop {} ", workshop);

        return workshop;
    }

    @PostMapping
    public void saveWorkshop(@RequestBody Workshop workshop) {
        log.info("received  {}", workshop);
        workshopService.addWorkshop(workshop);
    }
}
