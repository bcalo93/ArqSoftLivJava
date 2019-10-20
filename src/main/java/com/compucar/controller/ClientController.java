package com.compucar.controller;

import com.compucar.aop.AspectExecution;
import com.compucar.model.Client;
import com.compucar.service.ClientService;
import com.compucar.service.exceptions.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/clients")
public class ClientController {

    @Autowired
    private ClientService clientService;

    @GetMapping
    @ResponseStatus(value = HttpStatus.OK)
    @AspectExecution
    public List<Client> getAllClients() {
        return this.clientService.getAllClients();
    }

    @GetMapping(value = "/{clientId}")
    @ResponseStatus(value = HttpStatus.OK)
    @AspectExecution
    public Client getClient(@PathVariable("clientId")Long clientId) throws IdNullException, NotFoundException {
        return this.clientService.getClient(clientId);
    }

    @PostMapping
    @ResponseStatus(value = HttpStatus.CREATED)
    @AspectExecution
    public Client addClient(@RequestBody Client client) throws EntityNullException, DuplicateElementException, RequiredFieldMissingException {
        return this.clientService.addClient(client);
    }

    @PutMapping(value = "/{clientId}")
    @ResponseStatus(value = HttpStatus.OK)
    @AspectExecution
    public Client updateClient(@PathVariable("clientId")Long clientId, @RequestBody Client client) throws IdNullException,
            NotFoundException, EntityNullException {
        return this.clientService.updateClient(clientId, client);
    }

    @DeleteMapping(value = "/{clientId}")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    @AspectExecution
    public void deleteClient(@PathVariable("clientId")Long clientId) throws IdNullException {
        this.clientService.deleteClient(clientId);
    }
}
