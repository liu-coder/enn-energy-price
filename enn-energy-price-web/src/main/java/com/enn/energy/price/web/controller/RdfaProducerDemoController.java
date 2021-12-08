package com.enn.energy.price.web.controller;

import com.enn.energy.price.core.service.impl.RdfaProducerDemo;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import javax.annotation.Resource;

@RestController
@RequestMapping("/testMq")
public class RdfaProducerDemoController {

    @Resource
    RdfaProducerDemo rdfaProducer;

    @RequestMapping("/send")
    public String sendMessage(){

        rdfaProducer.testSend();
        return "send success";
    }
}
