package com.enn.energy.price.web.convertMapper;

import com.enn.energy.price.biz.service.bo.proxyprice.ElectricityPriceStructureBO;
import com.enn.energy.price.biz.service.bo.proxyprice.ElectricityPriceStructureDeleteValidateBO;
import com.enn.energy.price.biz.service.bo.proxyprice.ElectricityPriceStructureDetailBO;
import com.enn.energy.price.biz.service.bo.proxyprice.ElectricityPriceStructureUpdateBO;
import com.enn.energy.price.web.vo.requestvo.ElectricityPriceStructureUpdateReqVO;
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

    @Mappings({
            @Mapping(source = "electricityPriceUpdateReqVOList",target = "electricityPriceUpdateBOList"),
            @Mapping( source = "electricityPriceSeasonRuleUpdateReqVOList",target = "electricityPriceStructureRuleUpdateBOList")
    })
    ElectricityPriceStructureUpdateBO ElectricityPriceStructureUpdateReqVOTOBO(ElectricityPriceStructureUpdateReqVO electricityPriceStructureUpdateReqVO);















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



}
