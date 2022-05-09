package com.enn.energy.price.web.vo.responsevo;

import com.enn.energy.price.biz.service.bo.proxyprice.CityBO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * @author liujin
 * @version 1.0.0
 * @Date 2022/5/7 14:43
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CityRespVO implements Serializable{

    private static final long serialVersionUID = 3521433193765576411L;
    private String cityCode;

    private String city;

    List<DistrictVO> districtList;
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class DistrictVO implements Serializable{
        private static final long serialVersionUID = -5354208567653766903L;
        private String districtCode;

        private String district;
    }
}
