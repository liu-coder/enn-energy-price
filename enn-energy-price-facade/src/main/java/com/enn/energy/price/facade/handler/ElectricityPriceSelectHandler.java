package com.enn.energy.price.facade.handler;

import com.enn.energy.price.biz.service.*;
import com.enn.energy.price.biz.service.bo.*;
import com.enn.energy.price.client.dto.request.ElectricityPriceValueReqDTO;
import com.enn.energy.price.client.dto.request.ElectricityPriceVersionDetailReqDTO;
import com.enn.energy.price.client.dto.request.ElectricityPriceVersionsReqDTO;
import com.enn.energy.price.client.dto.response.ElectricityPriceValueDetailRespDTO;
import com.enn.energy.price.client.dto.response.ElectricityPriceVersionDetailRespDTO;
import com.enn.energy.price.client.dto.response.ElectricityPriceVersionsRespDTO;
import com.enn.energy.price.common.constants.CommonConstant;
import com.enn.energy.price.common.utils.PriceDateUtils;
import com.enn.energy.price.core.service.impl.CacheService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import top.rdfa.framework.biz.ro.PagedRdfaResult;
import top.rdfa.framework.biz.ro.RdfaResult;
import top.rdfa.framework.concurrent.api.exception.LockFailException;
import top.rdfa.framework.concurrent.redis.lock.RedissonRedDisLock;
import top.rdfa.framework.exception.RdfaException;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.concurrent.locks.Lock;
import java.util.stream.Collectors;

/**
 * @author ：chenchangtong
 * @date ：Created 2021/12/9 10:31
 * @description：快乐工作每一天
 */
@Component
@Slf4j
public class ElectricityPriceSelectHandler {

    final private String SEPARATOR_SPILT = "/";
    final private String HASH_KEY_SEPARATOR_SPILT = "#";
//    ThreadLocal<SimpleDateFormat> sf_hh = new ThreadLocal<SimpleDateFormat>(){
//        @Override
//        protected SimpleDateFormat initialValue() {
//            return new SimpleDateFormat("HH:mm:ss");
//        }
//    };

    ThreadLocal<SimpleDateFormat> sf_dd = new ThreadLocal<SimpleDateFormat>(){
        @Override
        protected SimpleDateFormat initialValue() {
            return new SimpleDateFormat("yyyy-MM-dd");
        }
    };

    ThreadLocal<SimpleDateFormat> sf_mmdd = new ThreadLocal<SimpleDateFormat>(){
        @Override
        protected SimpleDateFormat initialValue() {
            return new SimpleDateFormat("MM-dd");
        }
    };

    ThreadLocal<SimpleDateFormat> sf_all = new ThreadLocal<SimpleDateFormat>(){
        @Override
        protected SimpleDateFormat initialValue() {
            return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        }
    };
    @Value("${rdfa.redis.app-prefix}")
    private String appPrefix;
    @Value("${rdfa.redis.expire.base}")
    private Long expireBase;
    @Value("${random.long[3600,43200]}")
    private long randomNum;
    @Autowired
    private ElectricityPriceEquipmentService electricityPriceEquipmentService;
    @Autowired
    private ElectricityPriceVersionService electricityPriceVersionService;
    @Autowired
    private ElectricityPriceDetailService electricityPriceDetailService;
    @Autowired
    private ElectricityPriceRuleService electricityPriceRuleService;
    @Autowired
    private ElectricityPriceSeasonService electricityPriceSeasonService;
    @Autowired // 统一封装 redis 操作
    private CacheService cacheService;
    //    @Autowired
//    private CacheClient cacheClient;
//    @Autowired
//    private StringRedisTemplate redisTemplate;
//    @Autowired
//    private RedissonClient redissonClient;
    @Autowired
    private RedissonRedDisLock redDisLock;

    public RdfaResult<ElectricityPriceValueDetailRespDTO> selectElePrice(@Validated @RequestBody ElectricityPriceValueReqDTO requestDto){
        //先从redis中获取，如果获取成功，就直接返回，如果不成功，则查询数据库
        String key = requestDto.getEquipmentId();
        ElectricityPriceValueDetailRespDTO resp = getDataFromRedis(key,requestDto);
        if (resp != null){
            log.info("get data from redis !");
            return RdfaResult.success(resp);
        }
        RdfaResult<ElectricityPriceValueDetailRespDTO> detailFromDB = getPriceDetailFromDB(key, requestDto);
        return detailFromDB;
    }

    public PagedRdfaResult<ElectricityPriceVersionsRespDTO> selectVersions(@Validated @RequestBody ElectricityPriceVersionsReqDTO requestDto) throws ParseException {
        List<ElectricityPriceVersionsRespDTO> respDTOS = new ArrayList<>();
        Page<Object> page = PageHelper.startPage(requestDto.getPageNum(), requestDto.getPageSize());
        //设计分页查询，使用了关联查询
        List<ElectricityPriceVersionBO> versionBos = electricityPriceVersionService.selectEquVersionsByCondition(requestDto.getEquipmentId(),requestDto.getSystemCode());
        for (ElectricityPriceVersionBO versionBo : versionBos){
            ElectricityPriceVersionsRespDTO respDTO = new ElectricityPriceVersionsRespDTO();
            respDTO.setVersionId(versionBo.getVersionId());
            respDTO.setVersionName(versionBo.getVersionName());
            List<String> seasonList = electricityPriceSeasonService.selectSeasonGroupByCondition(versionBo.getVersionId());
            String seasons = StringUtils.join(seasonList, SEPARATOR_SPILT);
            respDTO.setSeasons(seasons);
            respDTO.setStartDate(sf_dd.get().format(versionBo.getStartDate()));
            respDTO.setVersionStatus(getVersionStatus(versionBo));
            respDTOS.add(respDTO);
        }
        removeThreadLocal();
        return PagedRdfaResult.success(page.getPageNum(), page.getPageSize(), page.getTotal(),respDTOS);
    }

    public RdfaResult<ElectricityPriceVersionDetailRespDTO> versionDetail(@Validated @RequestBody ElectricityPriceVersionDetailReqDTO detailReqDTO) throws Exception {
        //1、根据版本号查询版本信息
        ElectricityPriceVersionBO versionBo = electricityPriceVersionService.selectVersionByVersionId(detailReqDTO.getVersionId());
        if (versionBo == null){
            return RdfaResult.fail("E20005","电价版本数据未查询到，请填写准确的版本ID");
        }
        //2、根据版本号查询规则信息
        List<ElectricityPriceRuleBO> ruleBos = electricityPriceRuleService.selectRuleListByVersionId(detailReqDTO.getVersionId());
        if (ruleBos.size() == 0){
            return RdfaResult.fail("E20006","电价规则数据未查询到，请排查数据记录");
        }
        //3、根据版本ID、设备ID、cimCode 查询设备信息
        ElectricityPriceEquipmentBO equipmentBo = null;
        try {
            equipmentBo = electricityPriceEquipmentService.selectEquByCondition(detailReqDTO.getEquipmentId(), detailReqDTO.getVersionId(), detailReqDTO.getSystemCode());
        }catch (RdfaException e){
            log.error("通过版本Id、设备ID、cim编码查询设备存在异常 {}",e.getMessage());
            return RdfaResult.fail("E20007","设备数据存在异常");
        }
        //4、根据规则ID查询电价详情ID
        ElectricityPriceVersionDetailRespDTO respDTO = null;
        try {
            respDTO = convertVersionDetail(versionBo,ruleBos,equipmentBo);
        }catch (RdfaException e){
            log.error(e.getMessage());
            return RdfaResult.fail("E20008","季节数据存在异常");
        }
        return RdfaResult.success(respDTO);
    }

    private ElectricityPriceValueDetailRespDTO getDataFromRedis(String key ,ElectricityPriceValueReqDTO requestDto) {
        ElectricityPriceValueDetailRespDTO respDTO = null;
        try{
            Set<String> hKeys = cacheService.getHKeys(key, CommonConstant.ELECTRICITY_PRICE);
            String activeMonthDay = requestDto.getEffectiveTime().substring(5);
            for (String hkey : hKeys){
                String[] split = hkey.split(HASH_KEY_SEPARATOR_SPILT);
                if (PriceDateUtils.beforeOrEqual(split[1],requestDto.getEffectiveTime()) &&
                        PriceDateUtils.afterOrEqual(split[2],requestDto.getEffectiveTime()) &&
                        PriceDateUtils.beforeOrEqual(split[3],activeMonthDay) && PriceDateUtils.afterOrEqual(split[4],activeMonthDay)){
                    List<ElectricityPriceDetailCache> seasonPrices = (List<ElectricityPriceDetailCache>)cacheService.getHashData(key, CommonConstant.ELECTRICITY_PRICE, hkey);
                    if (seasonPrices!=null && seasonPrices.size() > 0){
                        respDTO = ElectricityPriceValueDetailRespDTO.builder().pricingMethod(split[5]).build();
                        List<ElectricityPriceValueDetailRespDTO.PriceDetail> detailList = seasonPrices.stream().map(item -> {
                            return ElectricityPriceValueDetailRespDTO.PriceDetail.builder().elePrice(item.getPrice()).
                                    startTime(item.getStartTime()).endTime(item.getEndTime()).step(item.getStep()).startStep(item.getStartStep()).
                                    endStep(item.getEndStep()).build();
                        }).collect(Collectors.toList());
                        respDTO.setPriceDetails(detailList);
                    }
                }
            }
        }catch (Exception e){
            log.error("get data from redis has error, {} " , e.getMessage());
        }
        return respDTO;
    }

    private RdfaResult<ElectricityPriceValueDetailRespDTO> getPriceDetailFromDB(String key ,ElectricityPriceValueReqDTO requestDto){
        String lockKey = key + CommonConstant.KEY_SPERATOR + requestDto.getEffectiveTime();
        Lock lock = null;
        try {
            lock = redDisLock.lock(lockKey);
            //重新从redis获取
            ElectricityPriceValueDetailRespDTO dataFromRedis = getDataFromRedis(key,requestDto);
            if (dataFromRedis != null){
                log.info("get data from redis !");
                return RdfaResult.success(dataFromRedis);
            }
            log.info("can not get data from redis,begin select from database");
            //1、根据设备ID，查询版本ID,提取版本列表ID -> versionIds
            List<ElectricityPriceEquipmentBO> electricityPriceEquipmentBos = electricityPriceEquipmentService.selectEquByConditionOrderByUpdateTime(
                    requestDto.getEquipmentId(),requestDto.getSystemCode());
            if (electricityPriceEquipmentBos.size() == 0){
                return RdfaResult.fail("E20001","未查到有效的设备数据");
            }
            List<String> versionIds = electricityPriceEquipmentBos.stream().map(ElectricityPriceEquipmentBO::getVersionId).collect(Collectors.toList());
            ElectricityPriceVersionBO electricityPriceVersionBo = null;
            Date activeTime = null;
            try {
                activeTime = sf_dd.get().parse(requestDto.getEffectiveTime());
                //2、根据版本列表ID、有效时间，确认唯一版本
                electricityPriceVersionBo = electricityPriceVersionService.selectVersionByCondition(versionIds, activeTime);
            } catch (RdfaException | ParseException e) {
                log.error("get unique version data has error, {} ",e.getMessage());
                if (e instanceof ParseException){
                    return RdfaResult.fail("E20009","时间格式错误，请输入正确的参数格式 yyyy-MM-dd");
                }
                return RdfaResult.fail("E20002","设备绑定的版本数据存在异常，请排查版本的有效时间");
            }
            //提取唯一版本对应的设备表中的规则ID 同一生效时间内：一个设备只能绑定一个规则ID、版本ID，只存在一条有效
            //TODO 存在问题：一个设备会绑定相同版本的不同规则吗？？？如果存在此种情况，则无法判定生效规则，并且无法判定电价 detail
            String ruleId = "";
            for (ElectricityPriceEquipmentBO bo : electricityPriceEquipmentBos){
                if (bo.getVersionId().equals(electricityPriceVersionBo.getVersionId())){//设备的版本与查询到的唯一的版本的id比较。
                    ruleId = bo.getRuleId();
                    break;
                }
            }
            ElectricityPriceRuleBO electricityPriceRuleBo = null;
            if (StringUtils.isNotBlank(ruleId)){
                electricityPriceRuleBo = electricityPriceRuleService.selectRuleByCondition(ruleId,electricityPriceVersionBo.getVersionId());
            }
            if (StringUtils.isBlank(ruleId) || electricityPriceRuleBo == null){
                return RdfaResult.fail("E20003","未查到电费规则");
            }
            //根据确定的版本ID、规则ID、有效时间确定唯一的季节ID
            ElectricityPriceSeasonBO electricityPriceSeasonBO = null;
            try {
                electricityPriceSeasonBO = electricityPriceSeasonService.selectSeasonByCondition(electricityPriceVersionBo.getVersionId(), ruleId, activeTime);
            } catch (RdfaException e) {
                log.error("查询季节数据遇到异常，请排查, {} ",e.getMessage());
                return RdfaResult.fail("E20004","季节数据异常");
            }
            //根据规则ID、季节ID确定电价详情
            List<ElectricityPriceDetailBO> detailBos = electricityPriceDetailService.selectDetailByCondition(electricityPriceVersionBo.getVersionId(),ruleId,electricityPriceSeasonBO.getSeasonId());
            ElectricityPriceValueDetailRespDTO convert = convert(electricityPriceRuleBo,electricityPriceSeasonBO, detailBos);
            putRedisValue(requestDto,electricityPriceVersionBo,electricityPriceSeasonBO,detailBos);
            removeThreadLocal();
            return RdfaResult.success(convert);
        } catch(LockFailException e){
            log.error(e.getMessage(), e);
            return RdfaResult.fail("E20010","获取锁失败");
        } finally{
            redDisLock.unlock(lock);
        }
    }

    private void putRedisValue(ElectricityPriceValueReqDTO requestDto,ElectricityPriceVersionBO versionBo,
                               ElectricityPriceSeasonBO seasonBO ,
                               List<ElectricityPriceDetailBO> detailBos){
        try{
            //封装 hash value值,放入 redis 缓存
            String key = requestDto.getEquipmentId();
            String hashKey = new StringBuilder(versionBo.getVersionId()).append(HASH_KEY_SEPARATOR_SPILT).append(sf_dd.get().format(versionBo.getStartDate()))
                    .append(HASH_KEY_SEPARATOR_SPILT).append(sf_dd.get().format(versionBo.getEndDate())).append(HASH_KEY_SEPARATOR_SPILT)
                    .append(seasonBO.getSeaStartDate()).append(HASH_KEY_SEPARATOR_SPILT).append(seasonBO.getSeaEndDate()).append(HASH_KEY_SEPARATOR_SPILT)
                    .append(seasonBO.getPricingMethod()).toString();
            List<ElectricityPriceDetailCache> seasonPrices = cacheService.getHashData(key, CommonConstant.ELECTRICITY_PRICE, hashKey);
            if (seasonPrices == null || seasonPrices.size() == 0){//如果此时取到了这个季节的redis数据，不更新。
                seasonPrices = detailBos.stream().map(detailBO -> {
                    return ElectricityPriceDetailCache.builder().price(detailBO.getPrice()).startTime(detailBO.getStartTime()).
                            endTime(detailBO.getEndTime()).step(detailBO.getStep()).startStep(detailBO.getStartStep()).endStep(detailBO.getEndStep()).build();
                }).collect(Collectors.toList());
                cacheService.hPutWithTimeOut(key,CommonConstant.ELECTRICITY_PRICE,hashKey,seasonPrices,getExpireSeconds(expireBase,randomNum));
            }
        }catch (Exception e){
            log.error("redis缓存异常 {}",e.getMessage());
            e.printStackTrace();
        }
    }

    private long getExpireSeconds(long expireBase,long randomNum){
        return expireBase + randomNum;
    }


    private ElectricityPriceValueDetailRespDTO convert(ElectricityPriceRuleBO ruleBo, ElectricityPriceSeasonBO seasonBO, List<ElectricityPriceDetailBO> detailBos){
        ElectricityPriceValueDetailRespDTO respDTO = ElectricityPriceValueDetailRespDTO.builder()
                .strategy(ruleBo.getStrategy())
                .pricingMethod(seasonBO.getPricingMethod())
                .baseCapacityPrice(ruleBo.getTransformerCapacityPrice())
                .maxCapacityPrice(ruleBo.getMaxCapacityPrice()).build();
        List<ElectricityPriceValueDetailRespDTO.PriceDetail> detailList = new ArrayList<>();
        for (ElectricityPriceDetailBO detailBo : detailBos){
            ElectricityPriceValueDetailRespDTO.PriceDetail detail = new ElectricityPriceValueDetailRespDTO.PriceDetail();
            detail.setPeriods(detailBo.getPeriods());
            detail.setStartTime(detailBo.getStartTime());
            detail.setEndTime(detailBo.getEndTime());
            detail.setStep(detailBo.getStep());
            detail.setStartStep(detailBo.getStartStep());
            detail.setEndStep(detailBo.getEndStep());
            detail.setElePrice(detailBo.getPrice());
            detailList.add(detail);
        }
        respDTO.setPriceDetails(detailList);
        return respDTO;
    }

    private ElectricityPriceVersionDetailRespDTO convertVersionDetail
            (ElectricityPriceVersionBO versionBo, List<ElectricityPriceRuleBO> ruleBos,
             ElectricityPriceEquipmentBO equipmentBo) throws RdfaException{
        ElectricityPriceVersionDetailRespDTO respDTO = ElectricityPriceVersionDetailRespDTO.builder().versionId(versionBo.getVersionId()).versionName(versionBo.getVersionName())
                .startDate(sf_dd.get().format(versionBo.getStartDate())).endDate(sf_dd.get().format(versionBo.getEndDate())).systemCode(versionBo.getSystemCode()).cimName(versionBo.getSystemName())
                .province(versionBo.getProvince()).provinceCode(versionBo.getProvinceCode()).city(versionBo.getCity()).cityCode(versionBo.getCityCode())
                .district(versionBo.getDistrict()).districtCode(versionBo.getDistrictCode()).priceType(versionBo.getPriceType()).enterprise(versionBo.getEnterprise())
                .bindType(versionBo.getBindType()).build();
        List<ElectricityPriceVersionDetailRespDTO.VersionRuleDTO> ruleDTOS = ruleBos.stream().map(item -> {
            ElectricityPriceVersionDetailRespDTO.VersionRuleDTO ruleDTO = ElectricityPriceVersionDetailRespDTO.VersionRuleDTO.builder().ruleId(item.getRuleId()).strategy(item.getStrategy())
                    .transformerCapacityPrice(item.getTransformerCapacityPrice()).maxCapacityPrice(item.getMaxCapacityPrice())
                    .industry(item.getIndustry()).voltageLevel(item.getVoltageLevel()).build();
            //根据 ruleId、版本ID 查询 seasonId列表
            List<ElectricityPriceSeasonBO> seasonBOS = electricityPriceSeasonService.selectSeasonByCondition(item.getVersionId(), item.getRuleId());
            List<ElectricityPriceVersionDetailRespDTO.VersionSeasonPriceDetail> versionSeasonPriceDetails = new ArrayList<>();
            for (ElectricityPriceSeasonBO seasonBO : seasonBOS){
                ElectricityPriceVersionDetailRespDTO.VersionSeasonPriceDetail seasonPriceDetail = ElectricityPriceVersionDetailRespDTO.VersionSeasonPriceDetail.builder().season(seasonBO.getSeason()).seasonId(seasonBO.getSeasonId())
                        .seaStartDate(seasonBO.getSeaStartDate()).seaEndDate(seasonBO.getSeaEndDate()).pricingMethod(seasonBO.getPricingMethod()).build();
                //通过季节ID、规则ID 查询 电价详情
                List<ElectricityPriceDetailBO> detailBos = electricityPriceDetailService.selectDetailByCondition(versionBo.getVersionId(),seasonBO.getRuleId(), seasonBO.getSeasonId());
                List<ElectricityPriceVersionDetailRespDTO.VersionPriceDetail> priceDetailDTOs = new ArrayList<>();
                for (ElectricityPriceDetailBO detailBo : detailBos) {
                    ElectricityPriceVersionDetailRespDTO.VersionPriceDetail priceDetail = ElectricityPriceVersionDetailRespDTO.VersionPriceDetail.builder().detailId(detailBo.getDetailId())
                            .periods(detailBo.getPeriods()).price(detailBo.getPrice()).detailId(detailBo.getDetailId())
                            .step(detailBo.getStep()).startStep(detailBo.getStartStep()).endStep(detailBo.getEndStep())
                            .startTime(detailBo.getStartTime()).endTime(detailBo.getEndTime()).build();
                    //通过 seasonId 查询季节表
                    priceDetailDTOs.add(priceDetail);
                }
                seasonPriceDetail.setElectricityPriceDetailDTOList(priceDetailDTOs);
                versionSeasonPriceDetails.add(seasonPriceDetail);
            }
            ruleDTO.setElectricityPriceSeasonDTOList(versionSeasonPriceDetails);
            return ruleDTO;
        }).collect(Collectors.toList());
        respDTO.setElectricityPriceRuleDTOList(ruleDTOS);
        return respDTO;
    }



    private Integer getVersionStatus(ElectricityPriceVersionBO versionBo) throws ParseException{
        Integer versionStatus = -1;
        if (PriceDateUtils.beforeNoEqual(versionBo.getEndDate(), PriceDateUtils.getToday())){
            versionStatus = 1; // 结束时间 < 今天 历史版本
        }else if (PriceDateUtils.beforeOrEqual(versionBo.getStartDate(),PriceDateUtils.getToday())
                && PriceDateUtils.afterOrEqual(versionBo.getEndDate(),PriceDateUtils.getToday())){
            versionStatus = 2; //开始时间 <= 今天 && 今天 <= 结束时间 生效版本
        }else if (PriceDateUtils.afterNoEqual(versionBo.getStartDate(),PriceDateUtils.getToday())){
            versionStatus = 3;//开始时间 > 今天 未来版本
        }
        return versionStatus;
    }
    private void removeThreadLocal(){
        sf_dd.remove();
//        sf_hh.remove();
        sf_all.remove();
        sf_mmdd.remove();
    }

}