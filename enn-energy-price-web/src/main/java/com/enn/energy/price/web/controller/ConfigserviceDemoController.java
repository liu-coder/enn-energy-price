package com.enn.energy.price.web.controller;

import com.enn.energy.price.core.service.impl.CheckLoggerLevelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/config")
public class ConfigserviceDemoController {

    @Autowired
    private CheckLoggerLevelService loggerService;

    @GetMapping(value = "/logger/level")
    public String getLoggerLevel() {
        return "logging.level.root = " + loggerService.getLoggerLevel();
    }

}
