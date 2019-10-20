package com.compucar.controller;

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
    public List<MechanicDto> get() {
        List<Mechanic> mechanics = this.mechanicService.getAllMechanic();
        return entityDtoConverter.convertToDtos(mechanics);
    }

    @GetMapping(value = "/{mechanicId}")
    @ResponseStatus(value = HttpStatus.OK)
    public MechanicDto get(@PathVariable("mechanicId") Long mechanicId) throws IdNullException, NotFoundException {
        Mechanic mechanic = this.mechanicService.getMechanic(mechanicId);
        return entityDtoConverter.convertToDto(mechanic);
    }

    @PostMapping
    @ResponseStatus(value = HttpStatus.CREATED)
    public MechanicDto post(@RequestBody MechanicDto mechanicDto) throws EntityNullException, DuplicateElementException, RequiredFieldMissingException {
        return entityDtoConverter.convertToDto(this.mechanicService.addMechanic(entityDtoConverter
                .convertToEntity(mechanicDto)));
    }

    @PutMapping(value = "/{mechanicId}")
    @ResponseStatus(value = HttpStatus.OK)
    public MechanicDto put(@PathVariable("mechanicId") Long mechanicId, @RequestBody MechanicDto mechanicDto) throws
            IdNullException, NotFoundException, EntityNullException {
        return entityDtoConverter.convertToDto(this.mechanicService.updateMechanic(mechanicId,
                entityDtoConverter.convertToEntity(mechanicDto)));
    }

    @DeleteMapping(value = "/{mechanicId}")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void delete(@PathVariable("mechanicId") Long mechanicId) throws IdNullException {
        this.mechanicService.deleteMechanic(mechanicId);
    }
}
