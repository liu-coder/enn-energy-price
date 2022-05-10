package com.enn.energy.price.web.vo.responsevo;

import com.enn.energy.price.biz.service.bo.ElectricityPriceDictionaryBO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * @author sunjidong
 * @version 1.0.0
 * @Date 2022/5/8 16:12
 */

public class ElectricityPriceDictionaryMapRespVO implements Serializable {

    private static final long serialVersionUID = -2078856332988903377L;

    Map<Integer, List<ElectricityPriceDictionaryBO>> typeDictionary;

    public Map<Integer, List<ElectricityPriceDictionaryBO>> getTypeDictionary() {
        return typeDictionary;
    }

    public void setTypeDictionary(Map<Integer, List<ElectricityPriceDictionaryBO>> typeDictionary) {
        this.typeDictionary = typeDictionary;
    }

    @Override
    public String toString() {
        return "ElectricityPriceDictionaryMapRespVO{" +
                "typeDictionary=" + typeDictionary +
                '}';
    }
}
