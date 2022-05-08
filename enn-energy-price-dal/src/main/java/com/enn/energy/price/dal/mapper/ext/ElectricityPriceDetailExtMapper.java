package com.enn.energy.price.dal.mapper.ext;

import com.enn.energy.price.dal.po.mbg.ElectricityPriceDetail;
import com.enn.energy.price.dal.po.mbg.ElectricityPriceRule;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;


/**
 * 电价明细Mapper.
 *
 * @author : wuchaon
 * @version : 1.0 2021/11/30 15:04
 * @since : 1.0
 **/
@Mapper
public interface ElectricityPriceDetailExtMapper {

    List<ElectricityPriceDetail> selectDetailByCondition(Map<String,Object> map);

    void batchAddElectricityPriceDetail(List<ElectricityPriceDetail> electricityPriceDetailList);

    //List<ElectricityPriceDetail> selectPriceDetailsByVersionId(String versionId);

    void batchUpdateByRuleIds(List<ElectricityPriceRule>  electricityPriceRuleBOs);

    int deleteDetailsByVersionId(String versionId);
}
