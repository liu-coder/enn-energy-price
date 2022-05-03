package com.enn.energy.price.biz.service.convertMapper;

import com.enn.energy.price.biz.service.bo.proxyprice.ElectricityTimeSectionCreateBO;
import com.enn.energy.price.dal.po.mbg.ElectricityTimeSection;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * 分时区间请求VO转BO
 *
 * @author sunjidong
 * @date 2022/5/1
 **/
@Mapper
public interface ElectricityTimeSectionCreateBOMapper {

    ElectricityTimeSectionCreateBOMapper INSTANCE = Mappers.getMapper(ElectricityTimeSectionCreateBOMapper.class);

    ElectricityTimeSection timeSectionCreateBOToPO(ElectricityTimeSectionCreateBO timeSectionCreateBO);

}
