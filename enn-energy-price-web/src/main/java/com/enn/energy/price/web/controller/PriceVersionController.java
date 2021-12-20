package com.enn.energy.price.web.controller;

import com.enn.energy.price.biz.service.ElectricityPriceService;
import com.enn.energy.price.biz.service.bo.*;
import com.enn.energy.price.common.utils.PriceDateUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import top.rdfa.framework.utils.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * 价格版本数据导入,临时初始化数据用的.
 *
 * @author : wuchaon
 * @version : 1.0 2021/12/17 16:10
 * @since : 1.0
 **/
@RestController
@Slf4j
public class PriceVersionController {

    @Autowired
    private ElectricityPriceService electricityPriceService;

    @RequestMapping("/index")
    public ModelAndView index() {
        ModelAndView result = new ModelAndView("index");
        result.setViewName("index");
        return result;
    }

    /**
     * 导入价格版本
     * @param file
     * @param request
     * @return
     */
    @RequestMapping("/fileUpload")
    public String fileUpload(@RequestParam(value = "file", required = false) MultipartFile file,
                             HttpServletRequest request) {

        String path = request.getSession().getServletContext().getRealPath("upload");

        // 成功条数
        int successNum = 0;
        // 失败条数
        int failedNum = 0;
        String fileName = file.getOriginalFilename();
        File targetFile = new File(path, fileName);
        if (!targetFile.exists()) {
            targetFile.mkdirs();
        }

        // 获取文件后缀
        String pattern = fileName.substring(fileName.indexOf(".") + 1, fileName.length());
        if (pattern.equals("xls") || pattern.equals("xlsx")) {
            int line= 0;
            try {
                file.transferTo(targetFile);
                Workbook book;
                try {
                    InputStream is = new FileInputStream(targetFile);
                    book = new HSSFWorkbook(is);
                } catch (Exception e) {
                    InputStream is = new FileInputStream(targetFile);
                    book = new XSSFWorkbook(is);
                    log.error("解析电价版本文件异常:{}",e.getMessage());
                }
                Sheet sheet = book.getSheetAt(0);
                //总行数
                int rowNum = sheet.getLastRowNum();

                if (rowNum > 2000) {
                    targetFile.delete();
                    return "导入数据条数不能超过2000条";
                }

                Row row;
                String systemCode;
                String systemName;
                String equipmentId;
                String equipmentName;
                String price;

                for (int i = 1; i <= rowNum; i++) {
                    line = i;
                    row = sheet.getRow(i);
                    systemCode = getCellValue(row.getCell(0));
                    systemName = getCellValue(row.getCell(1));
                    equipmentId = getCellValue(row.getCell(2));
                    equipmentName = getCellValue(row.getCell(3));
                    price = getCellValue(row.getCell(4));

                    if (StringUtils.isEmpty(systemCode) || StringUtils.isEmpty(systemName)
                            || StringUtils.isEmpty(equipmentId) || StringUtils.isEmpty(equipmentName) || StringUtils.isEmpty(price)) {
                        failedNum += 1;
                    } else {

                        ElectricityPriceVersionBO electricityPriceVersionBO = new ElectricityPriceVersionBO();
                        electricityPriceVersionBO.setStartDate(PriceDateUtils.strToDayDate("2021-01-01"));
                        electricityPriceVersionBO.setPriceType("1");
                        electricityPriceVersionBO.setBindType("1");
                        electricityPriceVersionBO.setVersionName("版本20210101000000");
                        electricityPriceVersionBO.setTenantId("1437667581924274178");
                        electricityPriceVersionBO.setTenantName("罗森总部");
                        electricityPriceVersionBO.setSystemCode(systemCode);
                        electricityPriceVersionBO.setSystemName(systemName);
                        //设备
                        ElectricityPriceEquipmentBO electricityPriceEquipmentBO = new ElectricityPriceEquipmentBO();
                        electricityPriceEquipmentBO.setEquipmentId(equipmentId);
                        electricityPriceEquipmentBO.setEquipmentName(equipmentName);
                        electricityPriceVersionBO.setElectricityPriceEquipmentBO(electricityPriceEquipmentBO);
                        //规则
                        List<ElectricityPriceRuleBO> electricityPriceRuleBOList= new ArrayList<>();
                        ElectricityPriceRuleBO  electricityPriceRuleBO = new ElectricityPriceRuleBO();
                        electricityPriceRuleBO.setMaxCapacityPrice("0");
                        electricityPriceRuleBO.setIndustry("商业用电");
                        electricityPriceRuleBO.setTransformerCapacityPrice("0");
                        electricityPriceRuleBO.setStrategy("1");
                        electricityPriceRuleBO.setVoltageLevel("220V");
                        electricityPriceRuleBOList.add(electricityPriceRuleBO);
                        electricityPriceVersionBO.setElectricityPriceRuleBOList(electricityPriceRuleBOList);

                        //季节
                        List<ElectricityPriceSeasonBO> electricityPriceSeasonBOList = new ArrayList<>();
                        ElectricityPriceSeasonBO electricityPriceSeasonBO = new ElectricityPriceSeasonBO();
                        electricityPriceSeasonBO.setSeaStartDate("01-01");
                        electricityPriceSeasonBO.setSeaEndDate("12-31");
                        electricityPriceSeasonBO.setPricingMethod("p");
                        electricityPriceSeasonBO.setSeason("全年统一");
                        electricityPriceSeasonBOList.add(electricityPriceSeasonBO);
                        electricityPriceRuleBO.setElectricityPriceSeasonBOList(electricityPriceSeasonBOList);

                        //明细
                        List<ElectricityPriceDetailBO> electricityPriceDetailBOList = new ArrayList<>();
                        ElectricityPriceDetailBO electricityPriceDetailBO = new ElectricityPriceDetailBO();
                        electricityPriceDetailBO.setPrice(price);
                        electricityPriceDetailBOList.add(electricityPriceDetailBO);
                        electricityPriceSeasonBO.setElectricityPriceDetailBOList(electricityPriceDetailBOList);

                        electricityPriceService.addElectricityPrice(electricityPriceVersionBO, false);
                        successNum += 1;
                    }
                }
            } catch (Exception e) {
                failedNum += 1;
                log.error("失败行数:{},失败原因:{}",line,e.getMessage());
            }
        }

        targetFile.delete();
        return "success" + "导入成功" + successNum + "条，失败" + failedNum + "条";
    }


    private String getCellValue(Cell cell) {
        if (cell == null) {
            return "";
        } else if (Cell.CELL_TYPE_NUMERIC == cell.getCellType()) {
            return String.valueOf((int) cell.getNumericCellValue());
        } else if (Cell.CELL_TYPE_STRING == cell.getCellType()) {
            return cell.getStringCellValue().trim();
        } else {
            return "";
        }
    }
}
