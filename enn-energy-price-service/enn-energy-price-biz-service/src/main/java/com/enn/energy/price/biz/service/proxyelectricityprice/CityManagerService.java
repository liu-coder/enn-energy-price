package com.enn.energy.price.biz.service.proxyelectricityprice;

import com.enn.energy.price.biz.service.bo.proxyprice.CityListBO;
import com.enn.energy.price.biz.service.bo.proxyprice.ProvinceBO;
import com.enn.energy.price.biz.service.bo.proxyprice.ProvinceListBO;

public interface CityManagerService {
    /**
     * 查询城市编码
     * @param provinceBO
     * @return
     */
    CityListBO queryCitys(ProvinceBO provinceBO);

    /**
     * 查询省列表
     * @return
     */
    ProvinceListBO queryProvinces();
}
