package com.compucar.service;

import com.compucar.dto.EventDto;
import com.compucar.service.exceptions.InvalidFieldValueException;

public interface EventService {
    void postEvent(EventDto serviceEvent) throws InvalidFieldValueException;
}
