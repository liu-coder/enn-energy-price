package com.enn.energy.price.web.vo.responsevo;

import com.enn.energy.price.biz.service.bo.proxyprice.ElectricityPriceUpdateBO;
import io.swagger.annotations.ApiModel;

import java.io.Serializable;
import java.util.List;

/**
 * xxxxx此处为类的描述信息
 *
 * @author sunjidong
 * @date 2022/5/16
 **/
@ApiModel("上传模板响应VO")
public class UploadTemplateRespVO implements Serializable {

    private static final long serialVersionUID = -6557574265431640906L;

    private List<ElectricityPriceUpdateBO> priceUpdateBOList;

    public List<ElectricityPriceUpdateBO> getPriceUpdateBOList() {
        return priceUpdateBOList;
    }

    public void setPriceUpdateBOList(List<ElectricityPriceUpdateBO> priceUpdateBOList) {
        this.priceUpdateBOList = priceUpdateBOList;
    }

    @Override
    public String toString() {
        return "UploadTemplateRespVO{" +
                "priceUpdateBOList=" + priceUpdateBOList +
                '}';
    }
}