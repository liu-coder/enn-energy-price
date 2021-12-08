package com.enn.energy.price.facade.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import top.rdfa.framework.biz.ro.RdfaResult;

public class BlockHandler {

    static Logger logger= LoggerFactory.getLogger(BlockHandler.class);

    public static RdfaResult blockHandler(){
        logger.warn("blockHandler happened!!!");
        return RdfaResult.fail("-201", "server is blocked!");
    }
}