package com.enn.energy.price.web.vo.responsevo;

import java.io.Serializable;
import java.util.List;

/**
 * @author sunjidong
 * @version 1.0.0
 * @Date 2022/5/8 17:54
 */
public class ElectricityPriceSeasonDetailForCreateRespVO implements Serializable {

    private static final long serialVersionUID = -7775831418969043941L;

    private String seasonName;

    private List<SeasonDateForCreateRespVO> seasonDateForCreateRespVOList;

    private List<ElectricityPriceStrategyForCreateRespVO> priceStrategyForCreateRespVOList;

    public String getSeasonName() {
        return seasonName;
    }

    public void setSeasonName(String seasonName) {
        this.seasonName = seasonName;
    }

    public List<SeasonDateForCreateRespVO> getSeasonDateForCreateRespVOList() {
        return seasonDateForCreateRespVOList;
    }

    public void setSeasonDateForCreateRespVOList(List<SeasonDateForCreateRespVO> seasonDateForCreateRespVOList) {
        this.seasonDateForCreateRespVOList = seasonDateForCreateRespVOList;
    }

    public List<ElectricityPriceStrategyForCreateRespVO> getPriceStrategyForCreateRespVOList() {
        return priceStrategyForCreateRespVOList;
    }

    public void setPriceStrategyForCreateRespVOList(List<ElectricityPriceStrategyForCreateRespVO> priceStrategyForCreateRespVOList) {
        this.priceStrategyForCreateRespVOList = priceStrategyForCreateRespVOList;
    }

    @Override
    public String toString() {
        return "ElectricityPriceSeasonDetailForCreateRespVO{" +
                "seasonName='" + seasonName + '\'' +
                ", seasonDateForCreateRespVOList=" + seasonDateForCreateRespVOList +
                ", priceStrategyForCreateRespVOList=" + priceStrategyForCreateRespVOList +
                '}';
    }
}