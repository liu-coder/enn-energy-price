package com.enn.energy.price.biz.service;

import cn.hutool.core.collection.CollectionUtil;
import com.enn.energy.price.biz.service.bo.ElectricityPriceSeasonBO;
import com.enn.energy.price.common.utils.BeanUtil;
import com.enn.energy.price.dal.mapper.ext.ElectricityPriceSeasonExtMapper;
import com.enn.energy.price.dal.po.mbg.ElectricityPriceSeason;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import top.rdfa.framework.exception.RdfaException;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 电价季节service.
 *
 * @author : wuchaon
 * @version : 1.0 2021/12/1 14:12
 * @since : 1.0
 **/
@Service
public class ElectricityPriceSeasonService {

    ThreadLocal<SimpleDateFormat> sf_mm_dd = new ThreadLocal<SimpleDateFormat>(){
        @Override
        protected SimpleDateFormat initialValue() {
            return new SimpleDateFormat("MM-dd");
        }
    };
    @Autowired
    private ElectricityPriceSeasonExtMapper electricityPriceSeasonExtMapper;

    /**
     * 批量添加电价季节
     * @param electricityPriceSeasonList
     */
    public void batchAddElectricityPriceSeason(List<ElectricityPriceSeason> electricityPriceSeasonList){
        if(CollectionUtil.isEmpty(electricityPriceSeasonList)){
            return;
        }
        electricityPriceSeasonExtMapper.batchAddElectricityPriceSeason(electricityPriceSeasonList);
    }


    /**
     * 根据版本ID、有效时间 查询唯一的seasonId。
     * @param versionId
     * @param activeTime
     * @return
     */
    public ElectricityPriceSeasonBO selectSeasonByCondition(String versionId, String ruleId, Date activeTime) throws RdfaException {
        Map<String,Object> map = new HashMap<>();
        map.put("versionId",versionId);
        map.put("ruleId",ruleId);
        map.put("state",0);
        map.put("seaStartDate",sf_mm_dd.get().format(activeTime));
        map.put("seaEndDate",sf_mm_dd.get().format(activeTime));
        List<ElectricityPriceSeason> seasons = electricityPriceSeasonExtMapper.selectSeasonByCondition(map);
        if (CollectionUtils.isEmpty(seasons) && sf_mm_dd.get().format(activeTime).equals("02-29")){
            map.put("seaStartDate","02-28");
            map.put("seaEndDate","02-28");
            seasons = electricityPriceSeasonExtMapper.selectSeasonByCondition(map);
        }
        if (seasons.size() > 1 || CollectionUtils.isEmpty(seasons)){
            throw new RdfaException("季节数据存在异常，请联系技术人员进行排查");
        }
        List<ElectricityPriceSeasonBO> seasonBOS = BeanUtil.mapList(seasons, ElectricityPriceSeasonBO.class);
        removeLocal();
        return seasonBOS.get(0);
    }

    public List<String> selectSeasonGroupByCondition(String versionId) {
        Map<String,Object> map = new HashMap<>();
        map.put("versionId",versionId);
        map.put("state",0);
        List<String> seasons = electricityPriceSeasonExtMapper.selectSeasonGroupByCondition(map);
        return seasons;
    }


    public void removeLocal(){
        sf_mm_dd.remove();
    }

    public ElectricityPriceSeasonBO selectSeasonBySeasonId(String versionId, String seasonId){
        Map<String,Object> map = new HashMap<>();
        map.put("versionId",versionId);
        map.put("seasonId",seasonId);
        map.put("state",0);
        List<ElectricityPriceSeason> seasons = electricityPriceSeasonExtMapper.selectSeasonByCondition(map);
        List<ElectricityPriceSeasonBO> seasonBOS = BeanUtil.mapList(seasons, ElectricityPriceSeasonBO.class);
        return seasonBOS.get(0);
    }

    public List<ElectricityPriceSeasonBO> selectSeasonByCondition(String versionId, String ruleId){
        Map<String,Object> map = new HashMap<>();
        map.put("versionId",versionId);
        map.put("ruleId",ruleId);
        map.put("state",0);
        List<ElectricityPriceSeason> seasons = electricityPriceSeasonExtMapper.selectSeasonByCondition(map);
        List<ElectricityPriceSeasonBO> seasonBOS = BeanUtil.mapList(seasons, ElectricityPriceSeasonBO.class);
        return seasonBOS;
    }

    public int updateSeasonStateByVersionId(String versionId) {
        int update = electricityPriceSeasonExtMapper.updateSeasonStateByVersionId(versionId);
        return update;
    }
}
