package com.compucar.service;

import com.compucar.model.Event;
import com.compucar.repository.EventRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EventServiceImpl implements EventService {

    @Autowired
    private EventRepository eventRepository;

    @Override
    public List<String> getEventNamesByServiceCode(String serviceCode) {
        return eventRepository.getNamesByServiceCode(serviceCode);
    }

    @Override
    public List<Event> getEventsByServiceCodeAndName(String serviceCode, String eventName) {
        return eventRepository.getEventsByServiceCodeAndName(serviceCode, eventName);
    }

    @Override
    public void addEvent(Event event) {
        eventRepository.save(event);
    }
}
