package com.enn.energy.price.processor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import top.rdfa.framework.mq.RdfaMqListener;
import top.rdfa.framework.rocketmq.annotation.RocketMQMessageListener;

/**
 * 消费cim端价格变动mq
 */
@Slf4j
@Service
@RocketMQMessageListener(topic = "cim-cim-data-service-datasync-topic", consumerGroup = "enn-energy-price-consumer",selectorExpression = "cim")
public class CIMDeviceChangeConsumer implements RdfaMqListener<String> {

    @Override
    public void onMessage(String message) {
        log.info("价格中心接收消息：{}", message);

    }
}
