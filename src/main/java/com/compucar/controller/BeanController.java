package com.compucar.controller;

import com.compucar.aop.AspectExecution;
import com.compucar.model.BeanCreation;
import com.compucar.service.BeanCreationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequestMapping(value = "/beans")
public class BeanController {

    @Autowired
    private BeanCreationService beanService;

    @GetMapping
    @AspectExecution
    public List<BeanCreation> listBeansCreated() {
        log.info("list all beans created");
        return beanService.listBeans();
    }
}
