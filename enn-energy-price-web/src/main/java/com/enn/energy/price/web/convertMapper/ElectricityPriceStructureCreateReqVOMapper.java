package com.enn.energy.price.web.convertMapper;

import com.enn.energy.price.biz.service.bo.proxyprice.ElectricityPriceStructureCreateBO;
import com.enn.energy.price.web.vo.requestvo.ElectricityPriceStructureCreateReqVO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * xxxxx此处为类的描述信息
 *
 * @author sunjidong
 * @date 2022/5/1
 **/
@Mapper
public interface ElectricityPriceStructureCreateReqVOMapper {

    ElectricityPriceStructureCreateReqVOMapper INSTANCE = Mappers.getMapper(ElectricityPriceStructureCreateReqVOMapper.class);

    ElectricityPriceStructureCreateBO priceStructureCreateReqVOToBO(ElectricityPriceStructureCreateReqVO priceStructureCreateReqVO);

}
