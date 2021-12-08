package com.enn.energy.price.core.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import top.rdfa.framework.mq.RdfaMqClient;
import top.rdfa.framework.mq.message.RdfaMqResult;

import javax.annotation.Resource;


@Service
public class RdfaProducerDemo  {

    private final static Logger logger= LoggerFactory.getLogger(RdfaProducerDemo.class);

    @Resource
    private RdfaMqClient rdfaMqClient;

    //配置中心读取topic ，自行设置
    @Value("${rocketmq.topic}")
    private String topic;

    public void testSend()  {
        //指定主题、消息体发送，返回SendResult
        RdfaMqResult sendResult = rdfaMqClient.send(topic,"test rdfa mq convertAndSend 1");
        logger.info(" test rdfaMqClient convertAndSend sendResult 1 {} ",sendResult.toString());
    }

}

