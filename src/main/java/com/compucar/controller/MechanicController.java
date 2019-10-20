package com.compucar.controller;

import com.compucar.aop.AspectExecution;
import com.compucar.dto.EntityDtoConverter;
import com.compucar.dto.MechanicDto;
import com.compucar.model.Mechanic;
import com.compucar.service.MechanicService;
import com.compucar.service.exceptions.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/mechanics")
public class MechanicController {

    @Autowired
    private MechanicService mechanicService;

    @Autowired
    private EntityDtoConverter<Mechanic, MechanicDto> entityDtoConverter;

    @GetMapping
    @ResponseStatus(value = HttpStatus.OK)
    @AspectExecution
    public List<MechanicDto> getAllMechanic() {
        return entityDtoConverter.convertToDtos(mechanicService.getAllMechanic());
    }

    @GetMapping(value = "/{mechanicId}")
    @ResponseStatus(value = HttpStatus.OK)
    @AspectExecution
    public MechanicDto get(@PathVariable("mechanicId")Long mechanicId) throws IdNullException, NotFoundException {
        Mechanic mechanic = this.mechanicService.getMechanic(mechanicId);
        return entityDtoConverter.convertToDto(mechanic);
    }

    @PostMapping
    @ResponseStatus(value = HttpStatus.CREATED)
    @AspectExecution
    public MechanicDto addMechanic(@RequestBody MechanicDto mechanicDto) throws EntityNullException, DuplicateElementException, RequiredFieldMissingException {
        return entityDtoConverter.convertToDto(mechanicService.addMechanic(entityDtoConverter
                .convertToEntity(mechanicDto)));
    }

    @PutMapping(value = "/{mechanicId}")
    @ResponseStatus(value = HttpStatus.OK)
    @AspectExecution
    public MechanicDto updateMechanic(@PathVariable("mechanicId") Long mechanicId, @RequestBody MechanicDto mechanicDto) throws
            IdNullException, NotFoundException, EntityNullException {
        return entityDtoConverter.convertToDto(mechanicService.updateMechanic(mechanicId,
                entityDtoConverter.convertToEntity(mechanicDto)));
    }

    @DeleteMapping(value = "/{mechanicId}")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    @AspectExecution
    public void deleteMechanic(@PathVariable("mechanicId") Long mechanicId) throws IdNullException {
        mechanicService.deleteMechanic(mechanicId);
    }
}
