package com.enn.energy.price.web.convertMapper;

import com.enn.energy.price.biz.service.bo.proxyprice.*;
import com.enn.energy.price.web.vo.requestvo.*;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * @author liujin
 * @version 1.0.0
 * @Date 2022/5/1 17:23
 */
@Mapper
public interface ElectricityPriceVersionUpdateMapper {
    ElectricityPriceVersionUpdateMapper INSTANCE = Mappers.getMapper( ElectricityPriceVersionUpdateMapper.class );

    /**
     * @param electricityPriceStructureUpdateReqVO vo
     * @return BO
     */
    ElectricityPriceStructureUpdateBO ElectricityPriceStructureUpdateReqVOTOBO(ElectricityPriceStructureUpdateReqVO electricityPriceStructureUpdateReqVO);

    /**
     * @param electricityPriceUpdateReqVO VO
     * @return BO
     */
    ElectricityPriceUpdateBO ElectricityPriceUpdateReqVOTOBO(ElectricityPriceUpdateReqVO electricityPriceUpdateReqVO);


    /**
     * @param electricityPriceSeasonRuleUpdateReqVO VO
     * @return BO
     */
    ElectricityPriceSeasonRuleUpdateBO ElectricityPriceSeasonRuleUpdateReqVOTOBO(ElectricityPriceSeasonRuleUpdateReqVO electricityPriceSeasonRuleUpdateReqVO);

    /**
     * @param electricityPriceSeasonUpdateReqVO VO
     * @return BO
     */
    ElectricityPriceSeasonUpdateBO ElectricityPriceSeasonUpdateReqVOTOBO(ElectricityPriceSeasonUpdateReqVO electricityPriceSeasonUpdateReqVO);


    /**
     * @param electricityPriceStrategyReqVOTOVO VO
     * @return BO
     */
    ElectricityPriceStrategyBO ElectricityPriceStrategyReqVOTOBO(ElectricityPriceStrategyReqVO electricityPriceStrategyReqVOTOVO);

    /**
     * @param electricityTimeSectionUpdateReqVO VO
     * @return BO
     */
    ElectricityTimeSectionUpdateBO ElectricityTimeSectionUpdateReqVOTOBO(ElectricityTimeSectionUpdateReqVO  electricityTimeSectionUpdateReqVO);


    /**
     * @param electricityPriceVersionUpdateReqVO VO
     * @return VO
     */
    ElectricityPriceVersionUpdateBO electricityPriceVersionUpdateReqVOTOBO(ElectricityPriceVersionUpdateReqVO electricityPriceVersionUpdateReqVO);


}
