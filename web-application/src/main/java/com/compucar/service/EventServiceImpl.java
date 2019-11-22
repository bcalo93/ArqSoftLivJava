package com.compucar.service;

import com.compucar.dto.EventDto;
import com.compucar.service.exceptions.InvalidFieldValueException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
public class EventServiceImpl implements EventService {

    @Autowired
    private RestTemplate restTemplate;

    @Override
    public void postEvent(EventDto serviceEvent) throws InvalidFieldValueException {
        String url = "http://localhost:8081/repository/events";

        HttpEntity<EventDto> requestEntity = new HttpEntity<>(serviceEvent);
        try {
            ResponseEntity<Void> responseEntity = restTemplate.postForEntity(url, requestEntity, Void.class);
            if(responseEntity.getStatusCode() != HttpStatus.OK) {
                throw new InvalidFieldValueException("Error trying to post events to repository system");
            }
        }
        catch (RestClientException e) {
            throw new InvalidFieldValueException("Error trying to post events to repository system");
        }
    }
}
