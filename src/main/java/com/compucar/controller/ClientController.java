package com.compucar.controller;

import com.compucar.service.exceptions.EntityNullException;
import com.compucar.service.exceptions.IdNullException;
import com.compucar.model.Client;
import com.compucar.service.ClientService;
import com.compucar.service.exceptions.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/clients")
public class ClientController {

    @Autowired
    private ClientService clientService;

    @RequestMapping(method = RequestMethod.GET)
    @ResponseBody
    @ResponseStatus(value = HttpStatus.OK)
    public List<Client> get() {
        return this.clientService.getAllClients();
    }

    @RequestMapping(value = "/{clientId}", method = RequestMethod.GET)
    @ResponseBody
    @ResponseStatus(value = HttpStatus.OK)
    public Client get(@PathVariable("clientId")Long clientId) throws IdNullException, NotFoundException {
        return this.clientService.getClient(clientId);
    }

    @RequestMapping(method = RequestMethod.POST)
    @ResponseBody
    @ResponseStatus(value = HttpStatus.CREATED)
    public Client post(@RequestBody Client client) throws EntityNullException {
        return this.clientService.addClient(client);
    }

    @RequestMapping(value = "/{clientId}", method = RequestMethod.PUT)
    @ResponseBody
    @ResponseStatus(value = HttpStatus.OK)
    public Client put(@PathVariable("clientId")Long clientId, @RequestBody Client client) throws IdNullException, NotFoundException, EntityNullException {
        return this.clientService.updateClient(clientId, client);
    }

    @RequestMapping(value = "/{clientId}", method = RequestMethod.DELETE)
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void delete(@PathVariable("clientId")Long clientId) throws IdNullException {
        this.clientService.deleteClient(clientId);
    }
}
