package com.enn.energy.price.web.convertMapper;

import com.enn.energy.price.biz.service.bo.proxyprice.ElectricityPriceVersionCreateBO;
import com.enn.energy.price.web.vo.requestvo.ElectricityPriceVersionCreateReqVO;
import org.apache.ibatis.annotations.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * 版本的创建(仅版本部分)请求VO转换器
 *
 * @author sunjidong
 * @date 2022/5/1
 **/
@Mapper
public interface ElectricityPriceVersionCreateReqVOMapper {

    ElectricityPriceVersionCreateReqVOMapper INSTANCE = Mappers.getMapper(ElectricityPriceVersionCreateReqVOMapper.class);

    ElectricityPriceVersionCreateBO priceVersionCreateReqVOToBO(ElectricityPriceVersionCreateReqVO priceVersionStructuresReqVO);

}
