package com.enn.energy.price.web.convertMapper;

import com.enn.energy.price.biz.service.bo.proxyprice.ElectricityPriceDefaultStructureAndRuleBO;
import com.enn.energy.price.biz.service.bo.proxyprice.ElectricityPriceStructureBO;
import com.enn.energy.price.biz.service.bo.proxyprice.ElectricityPriceStructureDeleteValidateBO;
import com.enn.energy.price.biz.service.bo.proxyprice.ElectricityPriceStructureDetailBO;
import com.enn.energy.price.web.vo.responsevo.*;
import com.enn.energy.price.web.vo.requestvo.ElectricityPriceStructureDeleteValidateReqVO;
import com.enn.energy.price.web.vo.responsevo.ElectricityPriceStructureDetailRespVO;
import com.enn.energy.price.web.vo.responsevo.ElectricityPriceStructureRespVO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(uses = { ElectricityPriceStructureRuleConverMapper.class })
public interface ElectricityPriceStrutureConverMapper {
    ElectricityPriceStrutureConverMapper INSTANCE = Mappers.getMapper(ElectricityPriceStrutureConverMapper.class);

    List<ElectricityPriceStructureDetailRespVO> ElectricityPriceStructureDetailBOListToVOList(List<ElectricityPriceStructureDetailBO> priceStructureDetailBOList);

    /**
     * @param priceStructureDetailBO
     * @return
     */
    @Mappings( {
            @Mapping( source = "priceStructureDetailBO.electricityPriceDetailBOList",target = "priceDetailRespVOList"),
            @Mapping( source = "priceStructureDetailBO.electricityPriceStructureRuleDetailBOS",target = "structureRuleDetailRespVOList")
    } )
    ElectricityPriceStructureDetailRespVO ElectricityPriceStructureDetailBOToVO(ElectricityPriceStructureDetailBO priceStructureDetailBO);
















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
     * 电价体系删除转换
     * @param vo
     * @return
     */
    ElectricityPriceStructureDeleteValidateBO ElectricityPriceStructureDeleteValidateVOToBO(ElectricityPriceStructureDeleteValidateReqVO vo);
    ElectricityPriceStrategyForCreateRespVO ElectricityPriceStructureForCreaateBOToVO(ElectricityPriceStructureBO priceStructureBO);

    ElectricityPriceStructureDetailForCreateRespVO ElectricityPriceStructureDetailForCreaateBOToVO(ElectricityPriceStructureDetailBO priceStructureDetailBO);

    List<ElectricityPriceStructureDetailForCreateRespVO> ElectricityPriceStructureDetailForCreaateBOToVOList(List<ElectricityPriceStructureDetailBO> priceStructureDetailBO);

    /**
     * @param priceDefaultStructureAndRuleBO
     * @return
     */
    @Mappings({
            @Mapping(source = "priceDefaultStructureAndRuleBO.priceStructureRuleDetailBO", target = "structureRuleDetailForCreateRespVO"),
            @Mapping(source = "priceDefaultStructureAndRuleBO.priceDetailBOList", target = "priceDetailForCreateRespVOList"),
    })
    ElectricityPriceDefaultStructureAndRuleRespVO electricityPriceStructureAndRuleBOToVO(ElectricityPriceDefaultStructureAndRuleBO priceDefaultStructureAndRuleBO);


}
