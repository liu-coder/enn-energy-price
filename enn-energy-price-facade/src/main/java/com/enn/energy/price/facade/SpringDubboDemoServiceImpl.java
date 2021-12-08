package com.enn.energy.price.facade;

import com.enn.energy.price.client.service.SpringDubboDemoService;
import com.enn.energy.price.client.dto.HelloDubboDto;
import com.enn.energy.price.facade.handler.BlockHandler;
import com.enn.energy.price.facade.handler.FallBackHandler;
import org.apache.dubbo.config.annotation.DubboService;
import com.alibaba.csp.sentinel.annotation.SentinelResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import top.rdfa.framework.biz.ro.RdfaResult;

@DubboService(version = "1.0.0", protocol = { "dubbo" })
public class SpringDubboDemoServiceImpl implements SpringDubboDemoService {

    Logger logger= LoggerFactory.getLogger(SpringDubboDemoServiceImpl.class);

    /**
     * Dubbo 服务提供者示例，使用了 Sentinel 流控，需要配置 具体流控规则。 Apollo 配置；
     * SentinelResource 为限流项，非必选；
     * 详细使用及配置方法请参阅 https://confluence.enncloud.cn/pages/viewpage.action?pageId=516555235
     */
    @Override
    @SentinelResource(value = "sentinelTest", blockHandler = "blockHandler",
            defaultFallback = "fallback")
    public RdfaResult<HelloDubboDto> helloDubbo(HelloDubboDto helloDubboDto) {
        logger.info("hello dubbo, data: {}", helloDubboDto);
        helloDubboDto.setResData(
                helloDubboDto.getSrcData());

        return RdfaResult.success(helloDubboDto);
    }

    /**
     * Dubbo 服务提供者示例，使用了 Sentinel 流控，需要配置 具体流控规则。 Apollo 配置；
     * 详细使用及配置方法请参阅 https://confluence.enncloud.cn/pages/viewpage.action?pageId=516555235
     */
    @Override
    @SentinelResource(value = "sentinelTest", blockHandlerClass= BlockHandler.class, blockHandler = "blockHandler",
            fallbackClass= FallBackHandler.class, defaultFallback = "fallback")
    public RdfaResult<String> helloSentinel() {
        return RdfaResult.success("Yeah, you get me.");
    }


    public RdfaResult<HelloDubboDto> blockHandler(HelloDubboDto helloDubboDto){
        logger.warn("blockHandler happened!!!");
        return RdfaResult.fail("-201", "server is blocked!");
    }

    public RdfaResult<HelloDubboDto> fallback(HelloDubboDto helloDubboDto){
        logger.warn("blockHandler happened!!!");
        return RdfaResult.fail("-200", "server has error!");
    }
}
