package com.enn.energy.price.biz.service.bo.proxyprice;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * @author liujin
 * @version 1.0.0
 * @Date 2022/5/7 11:18
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProvinceListBO implements Serializable {
    private static final long serialVersionUID = 9135770276534879328L;
    List<ProvinceBO> provinceBOList;
}
