package com.enn.energy.price.core.service.impl;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;


@Service
public class CheckLoggerLevelService {

        @Value("${logging.level.root:debug}")
    private String loggerLevel;

    public String getLoggerLevel()  {
        System.out.println("Logger Level: " + loggerLevel);
        return this.loggerLevel;
    }

}
