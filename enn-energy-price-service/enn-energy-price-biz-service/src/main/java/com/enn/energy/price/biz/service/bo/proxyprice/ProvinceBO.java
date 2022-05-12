package com.enn.energy.price.biz.service.bo.proxyprice;

import io.swagger.models.auth.In;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author liujin
 * @version 1.0.0
 * @Date 2022/5/7 9:45
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProvinceBO implements Serializable {
    private String id;
    private String areaCode;
    private String name;
}
