package com.enn.energy.price.biz.service.proxyelectricityprice;

import cn.hutool.poi.excel.ExcelReader;
import cn.hutool.poi.excel.ExcelWriter;
import com.enn.energy.price.biz.service.bo.proxyprice.*;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import top.rdfa.framework.biz.ro.RdfaResult;

import java.util.List;

/**
 * 代购电价service
 *
 * @author sunjidong
 * @date 2022/4/30
 **/
public interface ProxyElectricityPriceManagerBakService {

    /**
     * @describtion  新建版本以及版本下的所有体系
     * @author sunjidong
     * @date 2022/5/1 9:57
     * @param priceVersionStructuresCreateBO
     * @return Boolean
     */
    Boolean createPriceVersionStructures(ElectricityPriceVersionStructuresCreateBO priceVersionStructuresCreateBO);

    /**
     * 校验电价体系以及电价规则
     * @param structureAndRuleValidateBO
     * @return ElectricityPriceStructureAndRuleValidateRespBO
     */
    ElectricityPriceStructureAndRuleValidateRespBO validateStructureAndRule(ElectricityPriceStructureAndRuleValidateBO structureAndRuleValidateBO);

    ExcelWriter downLoadTemplate();

    /**
     * @describtion 上传模板
     * @author sunjidong
     * @date 2022/5/8 8:11
     * @param
     * @return
     */
    List<ElectricityPriceRuleCreateBO> uploadTemplate(ExcelReader reader, List<ElectricityPriceRuleCreateBO> priceRuleCreateBOList);

    ElectricityPriceStructureAndRuleValidateRespBO validateSeasonTime(List<ElectricitySeasonCreateBO> seasonCreateBOList);
}
