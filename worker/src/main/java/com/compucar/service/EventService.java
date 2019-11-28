package com.compucar.service;

import com.compucar.dto.EventIdDto;

public interface EventService {
    void processServiceCode(String serviceCode);

    void processSingleEvent(EventIdDto eventIdDto);
}
