package com.compucar.controller;

import com.compucar.dto.EntityDtoConverter;
import com.compucar.dto.MechanicDto;
import com.compucar.dto.OperationLogDto;
import com.compucar.model.CarService;
import com.compucar.model.Mechanic;
import com.compucar.model.OperationLog;
import com.compucar.service.*;
import com.compucar.service.exceptions.InvalidFieldValueException;
import com.compucar.service.exceptions.RequiredFieldMissingException;
import com.itextpdf.text.html.simpleparser.CellWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

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
    private ServiceExecutionService executionService;

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

    @GetMapping(value = "/usagereport")
    public String getUsageReport(
            @RequestParam(value = "date", required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
                    LocalDate date,
            Model model) throws RequiredFieldMissingException {
        if(date == null) {
            date = LocalDate.now();
        }
        model.addAttribute("usageReport", executionService.getUsageReport(date));
        return "report/usagereport";
    }

    @GetMapping(value = "/services-monthly")
    public String getServicesByMonth(@RequestParam(value = "month", required = false) Integer month, Model model) throws InvalidFieldValueException {
        if(month != null) {
            model.addAttribute("services", carServiceService.getServicesForGivenMonth(month));
        }
        return "report/services_monthly";
    }

    @GetMapping(value = "/services-dates-reader")
    public String getServicesByReaderBetweenDates(
            @RequestParam(value = "from", required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
                    LocalDate from,
            @RequestParam(value = "to", required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
                    LocalDate to,
            @RequestParam(value = "reader", required = false) String readerCode,
            Model model) throws InvalidFieldValueException {

        model.addAttribute("readers", readerService.listReaders());
        if(from != null && to != null && readerCode != null) {
            List<CarService> servicesWithReader = carServiceService.getServicesWithReader(readerCode);
            List<CarService> servicesWithReaderBetweenDates = servicesWithReader
                    .stream()
                    .filter(s -> s.getDate().isAfter(from.atStartOfDay()) && s.getDate().isBefore(to.atTime(23, 59)))
                    .collect(Collectors.toList());
            Double totalCost = servicesWithReader.stream().mapToDouble(s -> s.getCost()).sum();
            Integer totalUsageTime = servicesWithReader.stream().mapToInt(s -> s.getServiceTime()).sum();
            Integer totalMaintenancesWithReader = servicesWithReader.size();

            model.addAttribute("services", servicesWithReaderBetweenDates);
            model.addAttribute("totalCost", totalCost);
            model.addAttribute("totalUsageTime", totalUsageTime);
            model.addAttribute("totalMaintenances", totalMaintenancesWithReader);
        }

        return "report/services_dates_reader";
    }
}