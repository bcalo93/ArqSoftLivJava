package com.compucar.monitor.service;

import com.compucar.monitor.model.Heartbeat;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class HeartbeatServiceImpl implements HeartbeatService {

    private List<Heartbeat> heartbeats;

    @PostConstruct
    public void init() {
        log.info("initializing heartbeats service bean ");
        heartbeats = new ArrayList<>();
    }

    public List<Heartbeat> getHeartbeats() {
        return heartbeats;
    }

    public void addHeartbeat(Heartbeat heartbeat) {
        heartbeats.add(heartbeat);
    }
}
