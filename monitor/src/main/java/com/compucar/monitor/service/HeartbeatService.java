package com.compucar.monitor.service;

import com.compucar.monitor.model.Heartbeat;

import java.util.List;

public interface HeartbeatService {
    void addHeartbeat(Heartbeat heartbeat);

    List<Heartbeat> getHeartbeats();
}
