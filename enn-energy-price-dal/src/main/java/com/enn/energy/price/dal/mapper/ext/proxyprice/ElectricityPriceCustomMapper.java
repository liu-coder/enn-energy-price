package com.enn.energy.price.dal.mapper.ext.proxyprice;

import com.enn.energy.price.dal.po.ext.ElectricityPriceDetailPO;
import com.enn.energy.price.dal.po.mbg.ElectricityPrice;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@Mapper
public interface ElectricityPriceCustomMapper {

    /**
     * 根据价格ids删除价格
     * @param priceIds  价格ids
     */
    void batchDeletePriceByPriceIds(@Param( "priceIds" ) String priceIds);

    /**
     * 根据规则ids批量删除价格
     * @param map
     */
    void batchDeletePriceByRuleIds(Map<String,Object> map);


    /**
     * 获取电价明细列表
     * @param structureId
     * @return
     */
    List<ElectricityPriceDetailPO> getPriceDetailListByStructureId(@Param( "structureId" ) String structureId);

    ElectricityPriceDetailPO getPriceDetailByRuleId(@Param( "ruleId" ) String ruleId);


    /**
     * 获取电价
     * @param map
     * @return
     */
    List<ElectricityPrice> getPriceByCondition(Map<String,Object> map);


    /**
     * 更新价格根据条件
     * @param electricityPrice
     * @return
     */
    int updatePriceByConditions(ElectricityPrice electricityPrice);
}
