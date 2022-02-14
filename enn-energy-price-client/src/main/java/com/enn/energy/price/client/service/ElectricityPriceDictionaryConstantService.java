package com.enn.energy.price.client.service;


import com.enn.energy.price.client.dto.response.*;
import com.enn.energy.price.client.dto.request.*;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PostMapping;
import top.rdfa.framework.biz.ro.RdfaResult;

/**
 * @author huhul
 * 所属行业、电压等级、定价策略等
 */
public interface ElectricityPriceDictionaryConstantService {

    /**
     * 创建
     * @param priceDictionaryDTO
     * @return
     */
    @ApiOperation("创建字典项")
    @PostMapping("/price/saveDictionary")
    public RdfaResult insertDictionary(ElectricityPriceDictionaryDTO priceDictionaryDTO);
    /**
     * 查询
     * @param priceDictionaryDTO
     * @return
     *
     */
    public RdfaResult<ElectricityPriceDictionarySelectRespDTO> findDictionary(ElectricityPriceDictionarySelectDTO priceDictionaryDTO);

    /**
     * 编辑常量字典
     * @param priceDictionaryDTO
     * @return
     */
    public RdfaResult editDictionary(ElectricityPriceDictionarySelectDTO priceDictionaryDTO);
}
