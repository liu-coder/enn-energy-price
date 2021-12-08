package com.enn.energy.price.facade.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import top.rdfa.framework.biz.ro.RdfaResult;
import com.alibaba.csp.sentinel.slots.block.flow.FlowException;

public class FallBackHandler {

    static Logger logger= LoggerFactory.getLogger(FallBackHandler.class);

    public static RdfaResult fallback(Throwable ex){
        logger.warn("fallback happened, ex: {}", ex.toString());
        if(FlowException.isBlockException(ex)){
            return RdfaResult.fail("-200", "sentinel intercepted!");
        }
        return RdfaResult.fail("-200", "server has error!");
    }
}