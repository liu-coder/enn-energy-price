package com.enn.energy.price.biz.service.proxyelectricityprice;

import com.enn.energy.price.biz.service.bo.proxyprice.*;

/**
 * @description: 代购电价绑定service
 * @author:quyl
 * @createTime:2022/5/4 22:17
 */
public interface ProxyElectricityPriceBindService {

    /**
     * 代购电价电价绑定
     *
     * @param electricityPriceBindBO
     */
    void bindProxyElectricityPrice(ElectricityPriceBindBO electricityPriceBindBO);

    /**
     * 根据id进行解绑电价
     *
     * @param electricityPriceBindRemoveBO
     */
    void unBoundPriceRule(ElectricityPriceBindRemoveBO electricityPriceBindRemoveBO);

    /**
     * 根据省市区查找有效版本列表
     *
     * @param electricityPriceBindVersionsBO
     * @return
     */
    ElectricityPriceVersionsByBindAreaBO queryElectricityPriceVersionByBindArea(ElectricityPriceBindVersionsBO electricityPriceBindVersionsBO);

    /**
     * 根据企业id获取节点绑定状态
     *
     * @param entId
     * @return
     */
    ElectricityPriceBindNodeStatusBO getNodeBindPriceStatusByEntId(String entId);

    /**
     * 根据根节点获取绑定详情
     *
     * @param nodeId
     * @return
     */
    ElectricityPriceBindDetailBO getPriceBindDetail(Long nodeId);
}

