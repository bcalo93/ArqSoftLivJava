package com.compucar.service;

import com.compucar.dto.EventIdDto;

import java.util.List;

public interface EventService {
    void processServiceCode(String serviceCode);

    void processSingleEvent(EventIdDto eventIdDto);

    void processServiceCodeList(List<String> serviceCode);

    void reprocessEvents(EventIdDto[] eventIdDtos);
}
