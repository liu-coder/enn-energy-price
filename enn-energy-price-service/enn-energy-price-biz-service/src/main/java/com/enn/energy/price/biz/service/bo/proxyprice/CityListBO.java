package com.enn.energy.price.biz.service.bo.proxyprice;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * @author liujin
 * @version 1.0.0
 * @Date 2022/5/7 13:53
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
public class CityListBO implements Serializable {
    private static final long serialVersionUID = 936325510844708744L;

    List<CityBO> cityItemList;
}
