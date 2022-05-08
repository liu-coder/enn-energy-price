package com.enn.energy.price.web.convertMapper;

import com.enn.energy.price.biz.service.bo.ElectricityPriceDictionaryBO;
import com.enn.energy.price.biz.service.bo.proxyprice.*;
import com.enn.energy.price.web.vo.requestvo.*;
import com.enn.energy.price.web.vo.responsevo.ElectricityPriceDictionaryVO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * @author liujin
 * @version 1.0.0
 * @Date 2022/5/1 17:23
 */
@Mapper
public interface ElectricityPriceVersionUpdateConverMapper {
    ElectricityPriceVersionUpdateConverMapper INSTANCE = Mappers.getMapper( ElectricityPriceVersionUpdateConverMapper.class );

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
    ElectricityPriceStructureRuleUpdateBO ElectricityPriceSeasonRuleUpdateReqVOTOBO(ElectricityPriceSeasonRuleUpdateReqVO electricityPriceSeasonRuleUpdateReqVO);

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


    /**
     * @param seasonDateVO
     * @return
     */
    SeansonDateBO seasonDateVOTOBO(SeasonDateVO seasonDateVO);

    /**
     * @param electricityPriceVersionUpdateReqVO
     * @return bo
     */
    ElectricityPriceVersionDeleteBO  electricityPriceVersionDeleteReqVOTOBO(ElectricityPriceVersionDeleteReqVO electricityPriceVersionUpdateReqVO);


    /**
     * @param electricityPriceDictionaryBO
     * @return
     */
    ElectricityPriceDictionaryVO ElectricityPriceDictionaryBOToVO(ElectricityPriceDictionaryBO electricityPriceDictionaryBO);


    /**
     * @param electricityPriceDictionaryBOs
     * @return
     */
    List<ElectricityPriceDictionaryVO> ElectricityPriceDictionaryBOListToVOList(List<ElectricityPriceDictionaryBO> electricityPriceDictionaryBOs);

}
