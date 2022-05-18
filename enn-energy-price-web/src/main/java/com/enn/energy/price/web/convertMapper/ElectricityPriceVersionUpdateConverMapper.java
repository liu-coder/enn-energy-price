package com.enn.energy.price.web.convertMapper;

import com.enn.energy.price.biz.service.bo.ElectricityPriceDictionaryBO;
import com.enn.energy.price.biz.service.bo.proxyprice.*;
import com.enn.energy.price.web.vo.requestvo.*;
import com.enn.energy.price.web.vo.responsevo.ElectricityPriceDictionaryRespVO;
import com.enn.energy.price.web.vo.responsevo.ElectricityPriceVersionRespVO;
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

    /**
     * @param electricityPriceUpdateReqVO VO
     * @return BO
     */


    /**
     * @param electricityPriceSeasonRuleUpdateReqVO VO
     * @return BO
     */




    /**
     * @param electricityPriceStrategyReqVOTOVO VO
     * @return BO
     */

    /**
     * @param electricityTimeSectionUpdateReqVO VO
     * @return BO
     */



    /**
     * @param seasonDateVO
     * @return
     */

    /**
     * @param electricityPriceVersionUpdateReqVO
     * @return bo
     */



    /**
     * @param electricityPriceDictionaryBO
     * @return
     */


    /**
     * @param electricityPriceDictionaryBOs
     * @return
     */


    /**
     * @param electricityPriceVersionBOS
     * @return
     */

    /**
     * @param electricityPriceVersion
     * @return
     */


}
