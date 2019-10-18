package com.compucar.controller;

import com.compucar.model.Mechanic;
import com.compucar.service.MechanicService;
import com.compucar.service.exceptions.DuplicateElementException;
import com.compucar.service.exceptions.EntityNullException;
import com.compucar.service.exceptions.IdNullException;
import com.compucar.service.exceptions.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/mechanics")
public class MechanicController {

    @Autowired
    private MechanicService mechanicService;

    @GetMapping
    @ResponseStatus(value = HttpStatus.OK)
    public List<Mechanic> get() {
        return this.mechanicService.getAllMechanic();
    }

    @GetMapping(value = "/{mechanicId}")
    @ResponseStatus(value = HttpStatus.OK)
    public Mechanic get(@PathVariable("mechanicId")Long mechanicId) throws IdNullException, NotFoundException {
        return this.mechanicService.getMechanic(mechanicId);
    }

    @PostMapping
    @ResponseStatus(value = HttpStatus.CREATED)
    public Mechanic post(@RequestBody Mechanic mechanic) throws EntityNullException, DuplicateElementException {
        return this.mechanicService.addMechanic(mechanic);
    }

    @PutMapping(value = "/{mechanicId}")
    @ResponseStatus(value = HttpStatus.OK)
    public Mechanic put(@PathVariable("mechanicId")Long mechanicId, @RequestBody Mechanic mechanic) throws IdNullException, NotFoundException, EntityNullException {
        return this.mechanicService.updateMechanic(mechanicId, mechanic);
    }

    @DeleteMapping(value = "/{mechanicId}")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void delete(@PathVariable("mechanicId")Long mechanicId) throws IdNullException {
        this.mechanicService.deleteMechanic(mechanicId);
    }
}
