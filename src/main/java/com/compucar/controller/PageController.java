package com.compucar.controller;

import com.compucar.dto.EntityDtoConverter;
import com.compucar.dto.MechanicDto;
import com.compucar.dto.OperationLogDto;
import com.compucar.model.Mechanic;
import com.compucar.model.OperationLog;
import com.compucar.service.ClientService;
import com.compucar.service.MechanicService;
import com.compucar.service.OperationLogService;
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
    private EntityDtoConverter<Mechanic, MechanicDto> mechanicDtoConverter;

    @Autowired
    private OperationLogService operationLogService;

    @Autowired
    private EntityDtoConverter<OperationLog, OperationLogDto> operationLogDtoConverter;
    
    @RequestMapping(value = "/clients", method = RequestMethod.GET)
    public String clientsList(Model model) {
        model.addAttribute("clients", this.clientService.getAllClients());
        return "client/list";
    }

    @RequestMapping(value = "/mechanics", method = RequestMethod.GET)
    public String mechanicsList(Model model) {
        model.addAttribute("mechanics", mechanicDtoConverter.convertToDtos(mechanicService.getAllMechanic()));
        return "mechanic/list";
    }

    @RequestMapping(value = "/operationlogs", method = RequestMethod.GET)
    public String operationList(Model model) {
        model.addAttribute("operationLogs", operationLogDtoConverter.convertToDtos(operationLogService
                .getAllOperationLogs())
        );
        return "operationlog/list";
    }
}
