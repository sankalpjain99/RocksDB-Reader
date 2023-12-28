package com.sankalp.rocksdbreader.server.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/rocksdb/reader/v1/healthcheck")
public class HealthcheckController {

    @GetMapping()
    public String checkHealth() {
        return "Server is Up!";
    }

}
