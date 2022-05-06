package com.enn.energy.price.dal.mapper.ext.proxyprice;

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

    /**
     * @param ids 批量查询体系列表根据体系ids
     * @return
     */
    List<ElectricityPriceStructure> batchQueryStructureListByIds(@Param( "ids" ) List<String> ids);

    /**
     * @param record 根据id更新体系
     * @return
     */
    int updateByPrimaryKey(ElectricityPriceStructure record);
}
