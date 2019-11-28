package com.compucar.service;

import com.compucar.dto.EventDto;
import com.compucar.service.exceptions.InvalidFieldValueException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Slf4j
@Service
public class EventServiceImpl implements EventService {

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private MessageChannel notifications;

    @Value("${service.repository.url}")
    private String url;

    @Override
    public void postEvent(EventDto serviceEvent) throws InvalidFieldValueException {
        HttpEntity<EventDto> requestEntity = new HttpEntity<>(serviceEvent);
        try {
            ResponseEntity<Void> responseEntity = restTemplate.postForEntity(url, requestEntity, Void.class);
            if(responseEntity.getStatusCode() != HttpStatus.OK) {
                log.info("Error trying to post events to repository system. Response code: " + responseEntity.getStatusCode());
                throw new InvalidFieldValueException("Error trying to post events to repository system");
            }
        }
        catch (RestClientException e) {
            log.info("Error trying to connect to repository system" + e.getMessage());
            throw new InvalidFieldValueException("Error trying to post events to repository system");
        }
    }

    @Override
    public void processEvents(String serviceCode, List<EventDto> serviceEvents) throws InvalidFieldValueException {
        for (EventDto event : serviceEvents) {
            event.setServiceCode(serviceCode);
            this.postEvent(event);
        }
        enqueueServiceName(serviceCode);
    }

    private void enqueueServiceName(String serviceCode) {
        log.info("sending service code {}", serviceCode);
        Message<String> codeMessage = MessageBuilder.withPayload(serviceCode).build();
        notifications.send(codeMessage);
    }
}
