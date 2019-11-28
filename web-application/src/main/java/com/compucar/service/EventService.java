package com.compucar.service;

import com.compucar.dto.EventDto;
import com.compucar.service.exceptions.InvalidFieldValueException;

import java.util.List;

public interface EventService {
    void postEvent(EventDto serviceEvent) throws InvalidFieldValueException;
    void processEvents(String serviceCode, List<EventDto> serviceEvents) throws InvalidFieldValueException;
}
