package com.enn.energy.price.dal.mapper.ext.proxyprice;

import com.enn.energy.price.dal.po.mbg.ElectricityPriceStructure;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@Mapper
public interface ElectricityPriceStructureCustomMapper {

    /**
     * 根据条件查询体系列表
     * @param map
     * @return
     */
    List<ElectricityPriceStructure> queryListByConditions(Map<String,Object> map);


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

    /**
     * 根据id查询体系
     * @param id
     * @return
     */
    ElectricityPriceStructure selectByPrimaryKey(Long id);
}
