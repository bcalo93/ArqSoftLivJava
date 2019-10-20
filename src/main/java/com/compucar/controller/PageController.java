package com.compucar.controller;

import com.compucar.dto.EntityDtoConverter;
import com.compucar.dto.MechanicDto;
import com.compucar.model.Mechanic;
import com.compucar.service.ClientService;
import com.compucar.service.MechanicService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/page")
public class PageController {
    @Autowired
    private ClientService clientService;

    @Autowired
    private MechanicService mechanicService;

    @Autowired
    private EntityDtoConverter<Mechanic, MechanicDto> entityDtoConverter;
    
    @RequestMapping(value = "/clients", method = RequestMethod.GET)
    public String clientsList(Model model) {
        model.addAttribute("clients", this.clientService.getAllClients());
        return "client/list";
    }

    @RequestMapping(value = "/mechanics", method = RequestMethod.GET)
    public String mechanicsList(Model model) {
        model.addAttribute("mechanics", entityDtoConverter.convertToDtos(mechanicService.getAllMechanic()));
        return "mechanic/list";
    }
}
