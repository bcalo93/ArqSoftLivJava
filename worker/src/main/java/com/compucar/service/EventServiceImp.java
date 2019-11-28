package com.compucar.service;

import com.compucar.dto.DiagnoseDto;
import com.compucar.dto.EventDto;
import com.compucar.dto.EventIdDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;


@Slf4j
@Component
public class EventServiceImp implements EventService {
    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private MessageChannel publishTasks;

    @Autowired
    private Environment env;

    @Value("${service.repository.url}")
    private String repositoryUrl;

    @Value("${service.webapp.url}")
    private String webAppUrl;

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
        if(eventDto != null) {
            log.info("The following EventDto is being processed: {}", eventDto);
            String port = env.getProperty("server.port");
            String applicationName = env.getProperty("server.applicationName");
            String result = new StringBuffer("service_").append(eventDto.getServiceCode())
                    .append("_event_").append(eventDto.getName()).append("_workerName_").append(applicationName)
                    .append("_port_").append(port).toString();
            log.info("Processing result: {}", result);
            postDiagnose(eventDto.getServiceCode(), new DiagnoseDto(eventDto.getName(), result));
        }
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
        try {
            ResponseEntity<EventDto[]> response = restTemplate.getForEntity(requestUrl, EventDto[].class);
            EventDto[] result = response.getBody();
            return result != null && result.length > 0 ? result[0] : null;

        } catch(RestClientException re) {
            return null;
        }

    }

    private void postDiagnose(String serviceName, DiagnoseDto diagnoseDto) {
        String endpointUrl = new StringBuffer(webAppUrl).append("/")
                .append(serviceName).append("/diagnoses").toString();
        HttpEntity<DiagnoseDto> requestEntity = new HttpEntity<>(diagnoseDto);
        try {
            ResponseEntity<Void> responseEntity = restTemplate.postForEntity(endpointUrl, requestEntity, Void.class);
            if(responseEntity.getStatusCode() != HttpStatus.OK) {
                log.info("status code is not OK: {}", responseEntity.getStatusCode());
            }

        } catch(RestClientException re) {
            log.info("call to this url {} failed", endpointUrl);
        }
    }
}
