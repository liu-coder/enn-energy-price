package com.enn.energy.price.dal.po.ext;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @author liujin
 * @version 1.0.0
 * @Date 2022/5/7 14:19
 */
@Data
@AllArgsConstructor
@NoArgsConstructor

public class CityCode {
    private String id;
    private String areaCode;
    private String name;
    private String level;
    private String parentId;
    private Byte state;
    private Date createTime;
    private Date updateTime;
}
