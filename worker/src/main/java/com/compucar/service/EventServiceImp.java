package com.compucar.service;

import com.compucar.dto.EventDto;
import com.compucar.dto.EventIdDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;


@Slf4j
@Component
public class EventServiceImp implements EventService {
    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private MessageChannel publishTasks;

    @Value("${service.repository.url}")
    private String repositoryUrl;

    @Override
    public void processServiceCode(String serviceCode) {
        String requestUrl = new StringBuffer(repositoryUrl).append("?code=")
                .append(serviceCode)
                .toString();
        ResponseEntity<String[]> response = restTemplate.getForEntity(requestUrl, String[].class);
        String[] eventNames = response.getBody();
        if(eventNames != null) {
            for (String eventName : eventNames) {
                log.info("response got with Event {}", eventName);
                publishTask(new EventIdDto(serviceCode, eventName));
            }
        }
    }

    @Override
    public void processSingleEvent(EventIdDto eventIdDto) {
        EventDto eventDto = getSingleEvent(eventIdDto);
        log.info("The following EventDto was found: {}", eventDto);
//        if(eventDto != null) {
//        }
    }

    private void publishTask(EventIdDto event) {
        Message<EventIdDto> message = MessageBuilder.withPayload(event).build();
        boolean result = publishTasks.send(message);
        log.info("result from sending message with EventIdDto {} is {}", event, result);
    }

    private EventDto getSingleEvent(EventIdDto eventIdDto) {
        String requestUrl = new StringBuffer(repositoryUrl).append("?code=")
                .append(eventIdDto.getServiceCode()).append("&name=")
                .append(eventIdDto.getName())
                .toString();

        ResponseEntity<EventDto[]> response = restTemplate.getForEntity(requestUrl, EventDto[].class);
        EventDto[] result = response.getBody();
        return result != null && result.length > 0 ? result[0] : null;
    }
}
