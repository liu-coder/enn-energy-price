package com.enn.energy.price.biz.service;

import cn.hutool.core.collection.CollectionUtil;
import com.enn.energy.price.integration.cimzuul.client.CimBaseService;
import com.enn.energy.price.integration.cimzuul.dto.CimResponse;
import com.enn.energy.price.integration.cimzuul.dto.EntDTO;
import com.enn.energy.price.integration.participant.client.ParticipantApiClient;
import com.enn.energy.price.integration.participant.dto.ParticipantResponse;
import com.enn.energy.price.common.constants.CommonConstant;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import top.rdfa.framework.cache.api.CacheClient;

import java.util.List;

/**
 * @author Zhiqiang Li
 * @date 2021/12/1
 * @description: 价格中心调用外部服务
 **/
@Service
@Slf4j
public class ElectricityPriceOutSideService {
    @Autowired
    private CimBaseService cimBaseService;
    @Autowired
    private ParticipantApiClient participantApiClient;

    @Autowired
    public CacheClient cacheClient;

    /**
     *通过租户ID获取cimCode
     * @param tenantId
     * @return
     */
    public String getCimCodeByTenantId(String tenantId){
        String cimCode = "";
        if(StringUtils.isBlank(tenantId)){
            return cimCode;
        }
        //通过缓存获取，cimCode
        cimCode = cacheClient.vGet(CommonConstant.REDIS_TENANT_ID_TO_CIM_CODE_KEY, tenantId);

        if(StringUtils.isNotBlank(cimCode)){
            return cimCode;
        }
        //在参与者中心，获取企业id
        ParticipantResponse<List<String>> participantResponse = participantApiClient.tenantIdToEntId(tenantId, CommonConstant.SOURCE_UAC);

        if(!participantResponse.success()||CollectionUtil.isEmpty(participantResponse.getData())){
            log.warn("通过租户拿到uac企业的id异常,没有获取到数据tenantId:{}",tenantId);
            return cimCode;
        }
        //UAC ID
        String entId = participantResponse.getData().get(0);
        //通过企业id获取cimCode
        CimResponse<List<EntDTO>> cimResponse = cimBaseService.querySystemInfoListByEntId(entId);
        if(!cimResponse.success()||CollectionUtil.isEmpty(cimResponse.getData())){
            log.warn("通过企业id获取cim系统的code异常,没有获取到数据tenantId{}",tenantId);
            return cimCode;
        }

        cimCode = cimResponse.getData().get(0).getCode();
        //保存缓存到缓存中
        cacheClient.vSet(CommonConstant.REDIS_TENANT_ID_TO_CIM_CODE_KEY,tenantId,cimCode);

        return cimCode;
    }
}
