package com.enn.energy.price.processor;

import org.springframework.stereotype.Service;
import top.rdfa.framework.mq.RdfaMqListener;
import top.rdfa.framework.rocketmq.annotation.RocketMQMessageListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


@Service
@RocketMQMessageListener(topic = "${rocketmq.topic}", consumerGroup = "${rocketmq.consumer.group}", selectorExpression = "*")
public class RdfaStringConsumerDemo implements RdfaMqListener<String> {

    private final static Logger logger= LoggerFactory.getLogger(RdfaStringConsumerDemo.class);

    @Override
    public void onMessage(String message) {
        logger.info("------- StringConsumer received: {}\n", message);
    }
}
