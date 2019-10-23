package com.compucar.controller;

import com.compucar.dto.EntityDtoConverter;
import com.compucar.dto.MechanicDto;
import com.compucar.dto.OperationLogDto;
import com.compucar.model.Mechanic;
import com.compucar.model.OperationLog;
import com.compucar.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

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
    private WorkshopService workshopService;

    @Autowired
    private ReaderService readerService;

    @Autowired
    private CarServiceService carServiceService;

    @Autowired
    private OperationLogService operationLogService;

    @Autowired
    private EntityDtoConverter<OperationLog, OperationLogDto> operationLogDtoConverter;

    @GetMapping(value = "/main")
    public String mainMenu() {
        return "main";
    }

    @GetMapping(value = "/clients")
    public String clientsList(Model model) {
        model.addAttribute("clients", this.clientService.getAllClients());
        return "client/list";
    }

    @GetMapping(value = "/mechanics")
    public String mechanicsList(Model model) {
        model.addAttribute("mechanics", mechanicDtoConverter.convertToDtos(mechanicService.getAllMechanic()));
        return "mechanic/list";
    }

    @GetMapping(value = "/workshops")
    public String workshopsList(Model model) {
        model.addAttribute("workshops", this.workshopService.listWorkshops());
        return "workshop/list";
    }

    @GetMapping(value = "/readers")
    public String readersList(Model model) {
        model.addAttribute("readers", this.readerService.listReaders());
        return "reader/list";
    }

    @GetMapping(value = "/services")
    public String servicesList(Model model) {
        model.addAttribute("services", this.carServiceService.listServices());
        return "service/list";
    }

    @GetMapping(value = "/logs")
    public String operationList(Model model) {
        model.addAttribute("operationLogs", operationLogDtoConverter.convertToDtos(operationLogService
                .getAllOperationLogs())
        );
        return "operationlog/list";
    }
}