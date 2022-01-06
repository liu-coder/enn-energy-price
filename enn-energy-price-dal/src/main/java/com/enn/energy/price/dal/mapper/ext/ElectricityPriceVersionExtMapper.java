package com.enn.energy.price.dal.mapper.ext;

import com.enn.energy.price.dal.po.mbg.ElectricityPriceVersion;
import com.enn.energy.price.dal.po.view.ElectricityPriceEquVersionView;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

/**
 * 电价版本表Mapper.
 *
 * @author : wuchaon
 * @version : 1.0 2021/11/26 23:27
 * @since : 1.0
 **/
@Mapper
public interface ElectricityPriceVersionExtMapper {

    ElectricityPriceVersion selectByVersionId(String versionId);

    void addElectricityPriceVersion(ElectricityPriceVersion electricityPriceVersion);

    void batchAddElectricityPriceVersion(List<ElectricityPriceVersion> electricityPriceVersionList);

    void updatePriceVersionState(String versionId);

    void updatePriceVersion(ElectricityPriceVersion electricityPriceVersion);

    void batchUpdatePriceVersion(List<ElectricityPriceVersion> electricityPriceVersionLis);

    List<ElectricityPriceVersion> selectByVersionIds(List<String> versionIdList);

    List<ElectricityPriceVersion> selectVersionByCondition(Map<String,Object> map);

    List<ElectricityPriceVersion> selectEquVersionsByCondition(Map<String,Object> map);

    ElectricityPriceEquVersionView selectNearestVersion(Map<String, Object> map);
}
