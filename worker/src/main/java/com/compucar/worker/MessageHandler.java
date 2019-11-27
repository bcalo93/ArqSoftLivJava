package com.compucar.worker;

import com.compucar.service.EventService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class MessageHandler {
    @Autowired
    private EventService eventService;

    public void processService(Message<String> message) {
        String serviceCode = message.getPayload();
        log.info("get message {}", serviceCode);
        this.eventService.processServiceCode(serviceCode);
    }

}
