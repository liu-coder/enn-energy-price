package com.enn.energy.price.dal.po.ext;

import com.enn.energy.price.dal.po.mbg.ElectricityPriceEquipment;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @description: 设备版本绑定关系
 * @author:quyl
 * @createTime:2022/5/18 6:28
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ElectricityPriceEquipmentVersionDto extends ElectricityPriceEquipment {

    private String provinceCode;

}
