package com.enn.energy.price.biz.service.bo.proxyprice;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
 * @author liujin
 * @version 1.0.0
 * @Date 2022/5/7 15:29
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
public class ElectricityPriceStructureBO implements Serializable {
    private static final long serialVersionUID = -1094837809157335450L;

    private Long id;

    private String structureId;

    private String structureName;

    private String provinceCode;

    private String cityCodes;

    private String districtCodes;

    private Byte state;

    private String parentid;

    private String versionId;

    private String creator;

    private String updator;

    private Date createTime;

    private Date updateTime;

}
