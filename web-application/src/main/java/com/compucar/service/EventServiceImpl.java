package com.compucar.service;

import com.compucar.dto.EventDto;
import com.compucar.service.exceptions.InvalidFieldValueException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Service
public class EventServiceImpl implements EventService {

    @Autowired
    private RestTemplate restTemplate;

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
}
