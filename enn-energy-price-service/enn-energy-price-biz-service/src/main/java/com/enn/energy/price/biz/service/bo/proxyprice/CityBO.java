package com.enn.energy.price.biz.service.bo.proxyprice;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * @author liujin
 * @version 1.0.0
 * @Date 2022/5/7 13:48
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CityBO implements Serializable {
    private static final long serialVersionUID = 6595246775056064586L;
    private String cityCode;

    private String city;

    List<District> districtList;
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class District implements Serializable{
        private static final long serialVersionUID = -6101016447787994874L;
        private String districtCode;

        private String district;
    }
}
