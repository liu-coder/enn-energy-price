package com.enn.energy.price.client.service;

import com.enn.energy.price.client.dto.HelloDubboDto;
import top.rdfa.framework.biz.ro.RdfaResult;

/**
* 如果为分页结果，请使用PagedRdfaResult
*/

public interface SpringDubboDemoService {

    RdfaResult<HelloDubboDto> helloDubbo(HelloDubboDto helloDubboDto);

    RdfaResult<String> helloSentinel();
}
