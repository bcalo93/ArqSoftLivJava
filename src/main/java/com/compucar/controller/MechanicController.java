package com.compucar.controller;

import com.compucar.dto.MechanicDto;
import com.compucar.model.Mechanic;
import com.compucar.service.MechanicService;
import com.compucar.service.exceptions.DuplicateElementException;
import com.compucar.service.exceptions.EntityNullException;
import com.compucar.service.exceptions.IdNullException;
import com.compucar.service.exceptions.NotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/mechanics")
public class MechanicController {

    @Autowired
    private MechanicService mechanicService;

    @Autowired
    private ModelMapper modelMapper;

    @GetMapping
    @ResponseStatus(value = HttpStatus.OK)
    public List<MechanicDto> get() {
        List<Mechanic> mechanics = this.mechanicService.getAllMechanic();
        return mechanics.stream()
                .map(mechanic -> convertToDto(mechanic))
                .collect(Collectors.toList());
    }

    @GetMapping(value = "/{mechanicId}")
    @ResponseStatus(value = HttpStatus.OK)
    public MechanicDto get(@PathVariable("mechanicId")Long mechanicId) throws IdNullException, NotFoundException {
        Mechanic mechanic = this.mechanicService.getMechanic(mechanicId);
        return convertToDto(mechanic);
    }

    @PostMapping
    @ResponseStatus(value = HttpStatus.CREATED)
    public Mechanic post(@RequestBody MechanicDto mechanicDto) throws EntityNullException, DuplicateElementException {
        Mechanic mechanic = convertToEntity(mechanicDto);
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

    private MechanicDto convertToDto(Mechanic mechanic) {
        MechanicDto mechanicDto = modelMapper.map(mechanic, MechanicDto.class);
        Date startDate = Date.from(mechanic.getStartDate().atZone(ZoneId.systemDefault()).toInstant());
        mechanicDto.setStartDate(startDate);
        return mechanicDto;
    }

    private Mechanic convertToEntity(MechanicDto mechanicDto) {
        Mechanic mechanic = modelMapper.map(mechanicDto, Mechanic.class);
        LocalDateTime startDate =  mechanicDto.getStartDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
        mechanic.setStartDate(startDate);
        return mechanic;
    }
}
