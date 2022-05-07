package com.enn.energy.price.biz.service.convertMapper;

import com.enn.energy.price.biz.service.bo.proxyprice.ElectricityPriceCreateBO;
import com.enn.energy.price.dal.po.mbg.ElectricityPrice;
import com.enn.energy.price.dal.po.mbg.ElectricityPriceEquipment;
import com.enn.energy.price.dal.po.view.ElectricityPriceEquipmentView;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * xxxxx此处为类的描述信息
 *
 * @author sunjidong
 * @date 2022/5/5
 **/
@Mapper
public interface ElectricityPriceVersionViewConvertMapper {

    ElectricityPriceVersionViewConvertMapper INSTANCE = Mappers.getMapper(ElectricityPriceVersionViewConvertMapper.class);

    ElectricityPriceEquipment priceVersionViewToPo(ElectricityPriceEquipmentView priceEquipmentView);

}
