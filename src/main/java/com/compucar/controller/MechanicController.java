package com.compucar.controller;

import com.compucar.model.Mechanic;
import com.compucar.service.MechanicService;
import com.compucar.service.exceptions.DuplicateElementException;
import com.compucar.service.exceptions.EntityNullException;
import com.compucar.service.exceptions.IdNullException;
import com.compucar.service.exceptions.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/mechanics")
public class MechanicController {

    @Autowired
    private MechanicService mechanicService;

    @RequestMapping(method = RequestMethod.GET)
    @ResponseBody
    @ResponseStatus(value = HttpStatus.OK)
    public List<Mechanic> get() {
        return this.mechanicService.getAllMechanic();
    }

    @RequestMapping(value = "/{mechanicId}", method = RequestMethod.GET)
    @ResponseBody
    @ResponseStatus(value = HttpStatus.OK)
    public Mechanic get(@PathVariable("mechanicId")Long mechanicId) throws IdNullException, NotFoundException {
        return this.mechanicService.getMechanic(mechanicId);
    }

    @RequestMapping(method = RequestMethod.POST)
    @ResponseBody
    @ResponseStatus(value = HttpStatus.CREATED)
    public Mechanic post(@RequestBody Mechanic mechanic) throws EntityNullException, DuplicateElementException {
        return this.mechanicService.addMechanic(mechanic);
    }

    @RequestMapping(value = "/{mechanicId}", method = RequestMethod.PUT)
    @ResponseBody
    @ResponseStatus(value = HttpStatus.OK)
    public Mechanic put(@PathVariable("mechanicId")Long mechanicId, @RequestBody Mechanic mechanic) throws IdNullException, NotFoundException, EntityNullException {
        return this.mechanicService.updateMechanic(mechanicId, mechanic);
    }

    @RequestMapping(value = "/{mechanicId}", method = RequestMethod.DELETE)
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void delete(@PathVariable("mechanicId")Long mechanicId) throws IdNullException {
        this.mechanicService.deleteMechanic(mechanicId);
    }
}
