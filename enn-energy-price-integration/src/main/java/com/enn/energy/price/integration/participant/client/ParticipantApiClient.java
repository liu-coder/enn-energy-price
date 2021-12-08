package com.enn.energy.price.integration.participant.client;

import com.enn.energy.price.integration.participant.dto.ParticipantResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * @author Zhiqiang Li
 * @date 2021/12/1
 * @description: 参与者中心调用
 **/
@FeignClient(name="ParticipantApiClient",contextId = "ParticipantApiClient",path="/rest", url = "${participant.api.server.url}")
@RequestMapping(headers = "X-GW-AccessKey=${participant.gateway.accessKey}")
public interface ParticipantApiClient {
    /**
     *通过租户拿到uac企业的id
     * @param tenantId
     * @param source
     * @return
     */
    @PostMapping("/user/tenantIdToEntId")
    ParticipantResponse<List<String>> tenantIdToEntId(@RequestParam("tenantId") String tenantId, @RequestParam("source") String source);
}
