package com.compucar.controller;

import com.compucar.model.Client;
import com.compucar.logic.ClientLogic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.util.List;

@Controller
@RequestMapping("/clients")
public class ClientController {

    @Autowired
    private ClientLogic clientLogic;

    @RequestMapping(method = RequestMethod.GET)
    @ResponseBody
    @ResponseStatus(value = HttpStatus.OK)
    public List<Client> get() {
        throw new NotImplementedException();
    }

    @RequestMapping(value = "/{clientId}", method = RequestMethod.GET)
    @ResponseBody
    @ResponseStatus(value = HttpStatus.OK)
    public Client get(@PathVariable("clientId")Long clientId) {
        throw new NotImplementedException();
    }

    @RequestMapping(method = RequestMethod.POST)
    @ResponseBody
    @ResponseStatus(value = HttpStatus.CREATED)
    public Client post(@RequestBody Client client) {
        throw new NotImplementedException();
    }

    @RequestMapping(value = "/{clientId}", method = RequestMethod.PUT)
    @ResponseBody
    @ResponseStatus(value = HttpStatus.OK)
    public Client put(@PathVariable("clientId")Long clientId, @RequestBody Client client) {
        throw new NotImplementedException();
    }

    @RequestMapping(value = "/{clientId}", method = RequestMethod.DELETE)
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void delete(@PathVariable("clientId")Long clientId) {
        throw new NotImplementedException();
    }
}
