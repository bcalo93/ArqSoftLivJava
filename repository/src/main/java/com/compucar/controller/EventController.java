package com.compucar.controller;

import com.compucar.model.Event;
import com.compucar.service.EventService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping(value = "/events")
public class EventController {

    @Autowired
    private EventService eventService;

    @GetMapping(params = "code")
    public List<String> getNamesByServiceCode(@RequestParam(value = "code") String serviceCode) {
        return eventService.getEventNamesByServiceCode(serviceCode);
    }

    @GetMapping(params = {"code", "name"})
    public List<Event> getEventsByServiceCodeAndName(@RequestParam(value = "code") String serviceCode, @RequestParam(value = "name") String eventName) {
        return eventService.getEventsByServiceCodeAndName(serviceCode, eventName);
    }

    @PostMapping
    public void addEvent(@RequestBody Event event) {
        eventService.addEvent(event);
    }
}
