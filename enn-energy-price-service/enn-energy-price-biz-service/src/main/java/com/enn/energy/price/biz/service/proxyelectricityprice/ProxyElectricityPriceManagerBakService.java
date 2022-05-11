package com.enn.energy.price.biz.service.proxyelectricityprice;

import cn.hutool.poi.excel.ExcelReader;
import cn.hutool.poi.excel.ExcelWriter;
import com.enn.energy.price.biz.service.bo.proxyprice.*;
import io.swagger.annotations.ApiParam;
import org.springframework.web.bind.annotation.PathVariable;
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

    /**
     * @describtion 校验季节以及分时
     * @author sunjidong
     * @date 2022/5/9 10:18
     * @param
     * @return
     */
    ElectricityPriceStructureAndRuleValidateRespBO validateSeasonTime(List<ElectricitySeasonCreateBO> seasonCreateBOList);

    /**
     * @describtion 校验待删除的电价是否已绑定设备
     * @author sunjidong
     * @date 2022/5/9 10:18
     * @param
     * @return
     */
    Boolean validateDeletePriceRule(String ruleId);

    /**
     * @describtion 取消区域时，校验是否绑定了设备
     * @author sunjidong
     * @date 2022/5/9 10:18
     * @param
     * @return
     */
    ElectricityPriceStructureCreateBO validateDeleteArea(String id, String structureId, List<String> districtCodeList);

    /**
     * @describtion 根据省编码查找版本以及版本下的所有体系详细内容
     * @author sunjidong
     * @date 2022/5/9 15:53
     * @param
     * @return
     */
    List<ElectricityPriceStructureDetailBO> getLastVersionStructures(String provinceCode);

    /**
     * 获取默认的体系详细内容
     * @author sunjidong
     * @date 2022/5/11 14:57
     */
    ElectricityPriceDefaultStructureAndRuleBO getDefaultStructureDetail(String type, String provinceCode);
}
