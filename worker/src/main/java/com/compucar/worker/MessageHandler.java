package com.compucar.worker;

import com.compucar.dto.EventIdDto;
import com.compucar.service.EventService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Slf4j
@Component
public class MessageHandler {
    @Autowired
    private EventService eventService;

    @PostConstruct
    public void postConstruct() {
        //eventService.processServiceCode("SRV017");
    }

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



}
