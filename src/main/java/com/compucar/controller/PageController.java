package com.compucar.controller;

import com.compucar.service.ClientService;
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
    
    @RequestMapping(value = "/clients", method = RequestMethod.GET)
    public String clientsList(Model model) {
        model.addAttribute("clients", this.clientService.getAllClients());
        return "client/list";
    }
}
