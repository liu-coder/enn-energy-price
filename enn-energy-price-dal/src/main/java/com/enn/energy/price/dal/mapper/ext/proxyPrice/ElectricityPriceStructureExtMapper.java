package com.enn.energy.price.dal.mapper.ext.proxyPrice;

import com.enn.energy.price.dal.po.mbg.ElectricityPriceStructure;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ElectricityPriceStructureExtMapper {
    /**
     * 根据版本id查询体系列表
     * @param versionId
     * @return
     */
    List<ElectricityPriceStructure> queryListByVersionId(@Param( "versionId" ) String versionId);

    List<ElectricityPriceStructure> batchQueryStructureListByIds(@Param( "ids" ) List<String> ids);
}
