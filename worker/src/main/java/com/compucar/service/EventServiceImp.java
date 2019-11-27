package com.compucar.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;

@Slf4j
@Component
public class EventServiceImp implements EventService {
    @Autowired
    private RestTemplate restTemplate;

    @Value("${service.repository.url}")
    private String url;

    @PostConstruct
    public void postConstruct() {
        processServiceCode("SRV017");
    }

    @Override
    public void processServiceCode(String serviceCode) {
        String requestUrl = url + "?code=" + serviceCode;
        ResponseEntity<String[]> response = restTemplate.getForEntity(requestUrl, String[].class);
        String[] eventNames = response.getBody();
        for (int i = 0; i < eventNames.length; i++) {
            log.info("response got with Event {}", eventNames[i]);
        }
    }
}
