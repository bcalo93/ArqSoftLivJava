package com.compucar.monitor.controller;

import com.compucar.monitor.model.Heartbeat;
import com.compucar.monitor.service.HeartbeatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/heartbeat")
public class HeartbeatController {

    @Autowired
    private HeartbeatService heartbeatService;

    @GetMapping
    public List<Heartbeat> getAll() {
        return heartbeatService.getHeartbeats();
    }

    @PostMapping
    public void receiveHeartbeat(@RequestBody Heartbeat heartbeat) {
        heartbeatService.addHeartbeat(heartbeat);
    }
}
