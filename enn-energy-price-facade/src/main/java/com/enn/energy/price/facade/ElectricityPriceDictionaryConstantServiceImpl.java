package com.enn.energy.price.facade;


import com.enn.energy.price.client.dto.request.ElectricityPriceDictionaryDTO;
import com.enn.energy.price.client.service.ElectricityPriceDictionaryConstantService;

import com.enn.energy.price.biz.service.ElectricityPriceDictionaryService;
import com.enn.energy.price.biz.service.bo.ElectricityPriceDictionaryBO;
import com.enn.energy.price.client.dto.response.*;
import com.enn.energy.price.client.dto.request.*;
import com.enn.energy.price.common.utils.BeanUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import top.rdfa.framework.biz.ro.RdfaResult;

import java.util.List;

/**
 * @author ：chenchangtong
 * @date ：Created 2021/11/22 16:23
 * @description：快乐工作每一天
 */
@Slf4j
@Validated
@RestController
@RequestMapping("/price")
public class ElectricityPriceDictionaryConstantServiceImpl implements ElectricityPriceDictionaryConstantService {
    @Autowired
    private ElectricityPriceDictionaryService electricityPriceDictionaryService;

    @PostMapping("/saveDictionary")
    @Override
    public RdfaResult insertDictionary(@Validated @RequestBody ElectricityPriceDictionaryDTO priceDictionaryDTO) {
        log.info("saveDictionary 创建常量");
        ElectricityPriceDictionaryBO priceDictionaryBo = ElectricityPriceDictionaryBO.builder().
                code(priceDictionaryDTO.getKey()).
                name(priceDictionaryDTO.getValue())
                .type(priceDictionaryDTO.getType()).typeDesc(priceDictionaryDTO.getTypeDesc()).build();
        int result = electricityPriceDictionaryService.insertDictionary(priceDictionaryBo);
        if (result == -99){
            return RdfaResult.fail("E2002","字典数据存在重复添加情况，请检查常量code、name和type");
        }
        return result > 0 ? RdfaResult.success("") : RdfaResult.fail("E2001","数据创建失败");
    }

    @PostMapping("/findDictionary")
    @Override
    public RdfaResult<ElectricityPriceDictionarySelectRespDTO> findDictionary(@RequestBody ElectricityPriceDictionarySelectDTO priceDictionaryDTO) {
        ElectricityPriceDictionaryBO bo = BeanUtil.map(priceDictionaryDTO,ElectricityPriceDictionaryBO.class);
        List<ElectricityPriceDictionaryBO> priceDictionaryBOS = electricityPriceDictionaryService.selectDictionary(bo);
        List<ElectricityPriceDictionarySelectRespDTO.ElectricityPriceDictionarySelectItemResp> dictionaries = BeanUtil.mapList(priceDictionaryBOS,
                ElectricityPriceDictionarySelectRespDTO.ElectricityPriceDictionarySelectItemResp.class);
        ElectricityPriceDictionarySelectRespDTO respDTO = new ElectricityPriceDictionarySelectRespDTO();
        respDTO.setItems(dictionaries);
        return RdfaResult.success(respDTO);
    }

    @PostMapping("/editDictionary")
    @Override
    public RdfaResult editDictionary(@RequestBody ElectricityPriceDictionarySelectDTO priceDictionaryDTO) {
        ElectricityPriceDictionaryBO bo = BeanUtil.map(priceDictionaryDTO,ElectricityPriceDictionaryBO.class);
        int result = electricityPriceDictionaryService.editDictionary(bo);
        return result > 0 ? RdfaResult.success(""):RdfaResult.fail("E2001","更新失败");
    }


}
