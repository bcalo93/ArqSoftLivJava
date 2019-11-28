package com.compucar.worker;

import com.compucar.dto.EventIdDto;
import com.compucar.service.EventService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.MessageChannel;
import org.springframework.stereotype.Component;

import java.util.List;


@Slf4j
@Component
public class MessageHandler {
    @Autowired
    private EventService eventService;

    public void processService(Message<String> message) {
        String serviceCode = message.getPayload();
        log.info("get message from processService {}", serviceCode);
        this.eventService.processServiceCode(serviceCode);
    }

    public void processTask(Message<EventIdDto> message) {
        EventIdDto eventIdDto = message.getPayload();
        log.info("get EventIdDto from processTask {}", eventIdDto);
        this.eventService.processSingleEvent(eventIdDto);
    }

    public void reprocessSubmit(Message<List<String>> message) {
        List<String> serviceCodes = message.getPayload();
        log.info("reprocessSubmit with message {}", serviceCodes);
        this.eventService.processServiceCodeList(serviceCodes);
    }

    public void reprocessService(Message<EventIdDto[]> message) {
        EventIdDto[] eventIdDtos = message.getPayload();
        log.info("reprocessService with message {}", eventIdDtos);
        this.eventService.reprocessEvents(eventIdDtos);
    }
}
