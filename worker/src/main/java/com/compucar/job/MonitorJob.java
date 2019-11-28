package com.compucar.job;

import com.compucar.dto.HeartbeatDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Component
public class MonitorJob {
    @Autowired
    private RestTemplate restTemplate;

    @Value("${service.monitor.url}")
    private String url;

    @Autowired
    private Environment env;

    @Scheduled(fixedRateString = "${job.monitor.fixedRate}")
    public void sendHeartbeat() {
        log.info("sending heartbeat to monitor...");
        String applicationName = env.getProperty("server.applicationName");
        int port = Integer.valueOf(env.getProperty("server.port"));
        HeartbeatDto heartbeat = new HeartbeatDto(applicationName, port);
        HttpEntity<HeartbeatDto> requestEntity = new HttpEntity<>(heartbeat);
        try {
            ResponseEntity<Void> responseEntity = restTemplate.postForEntity(url, requestEntity, Void.class);
            if(responseEntity.getStatusCode() == HttpStatus.OK) {
                log.info("Heartbeat succeeded");
            }
            else {
                log.warn("Error sending heartbeat to monitor. Response code: {}", responseEntity.getStatusCode());
            }
        }
        catch (RestClientException e) {
            log.warn("Error sending heartbeat to monitor: {}", e.getMessage());
        }
    }
}
