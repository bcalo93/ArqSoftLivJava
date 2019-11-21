package com.compucar.service;

import com.compucar.model.Event;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface EventService {
    List<String> getEventNamesByServiceCode(String serviceCode);

    List<Event> getEventsByServiceCodeAndName(String serviceCode, String eventName);

    void addEvent(Event event);
}