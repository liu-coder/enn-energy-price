package com.enn.energy.price.web.convertMapper;

import com.enn.energy.price.biz.service.bo.proxyprice.ElectricityPriceStructureBO;
import com.enn.energy.price.biz.service.bo.proxyprice.ElectricityPriceStructureDetailBO;
import com.enn.energy.price.web.vo.responsevo.ElectricityPriceStrategyForCreateRespVO;
import com.enn.energy.price.web.vo.responsevo.ElectricityPriceStructureDetailForCreateRespVO;
import com.enn.energy.price.web.vo.responsevo.ElectricityPriceStructureDetailRespVO;
import com.enn.energy.price.web.vo.responsevo.ElectricityPriceStructureRespVO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface ElectricityPriceStrutureConverMapper {
    ElectricityPriceStrutureConverMapper INSTANCE = Mappers.getMapper(ElectricityPriceStrutureConverMapper.class);

    /**
     * @param priceStructureBO
     * @return
     */
    ElectricityPriceStructureRespVO ElectricityPriceStructureRespBOToVO(ElectricityPriceStructureBO priceStructureBO);

    /**
     * @param priceStructureBOList
     * @return
     */
    List<ElectricityPriceStructureRespVO> ElectricityPriceStructureRespBOListToVOList(List<ElectricityPriceStructureBO> priceStructureBOList);


    /**
     * @param priceStructureDetailBO
     * @return
     */
    ElectricityPriceStructureDetailRespVO ElectricityPriceStructureDetailBOToVO(ElectricityPriceStructureDetailBO priceStructureDetailBO);

    List<ElectricityPriceStructureDetailRespVO> ElectricityPriceStructureDetailBOListToVOList(List<ElectricityPriceStructureDetailBO> priceStructureDetailBOList);

    ElectricityPriceStrategyForCreateRespVO ElectricityPriceStructureForCreaateBOToVO(ElectricityPriceStructureBO priceStructureBO);

    ElectricityPriceStructureDetailForCreateRespVO ElectricityPriceStructureDetailForCreaateBOToVO(ElectricityPriceStructureDetailBO priceStructureDetailBO);

    List<ElectricityPriceStructureDetailForCreateRespVO> ElectricityPriceStructureDetailForCreaateBOToVOList(List<ElectricityPriceStructureDetailBO> priceStructureDetailBO);



}
