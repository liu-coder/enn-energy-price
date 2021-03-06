package com.enn.energy.price.facade.handler;

import com.alibaba.fastjson.JSONObject;
import com.enn.energy.price.biz.service.*;
import com.enn.energy.price.biz.service.aop.MyCacheable;
import com.enn.energy.price.biz.service.bo.*;
import com.enn.energy.price.client.dto.request.ElectricityCimPointPriceReq;
import com.enn.energy.price.client.dto.request.ElectricityPriceCurrentVersionDetailReqDTO;
import com.enn.energy.price.client.dto.request.ElectricityPriceValueReqDTO;
import com.enn.energy.price.client.dto.request.ElectricityPriceVersionsReqDTO;
import com.enn.energy.price.client.dto.response.*;
import com.enn.energy.price.common.constants.CommonConstant;
import com.enn.energy.price.common.error.ErrorCodeEnum;
import com.enn.energy.price.common.utils.BeanUtil;
import com.enn.energy.price.common.utils.PriceDateUtils;
import com.enn.energy.price.core.service.impl.CacheService;
import com.enn.energy.price.dal.po.view.ElectricityPriceEquVersionView;
import com.enn.energy.price.integration.cim.client.CimApiClient;
import com.enn.energy.price.integration.cim.client.CimPriceClient;
import com.enn.energy.price.integration.cim.dto.CimDayPointPriceResp;
import com.enn.energy.price.integration.cim.dto.CimPointPriceReq;
import com.enn.energy.price.integration.cim.dto.CimPriceReq;
import com.enn.energy.price.integration.cim.dto.CimPriceResp;
import com.enn.energy.price.integration.cimzuul.dto.CimResponse;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import top.rdfa.framework.biz.ro.PagedRdfaResult;
import top.rdfa.framework.biz.ro.RdfaResult;
import top.rdfa.framework.cache.api.CacheClient;
import top.rdfa.framework.exception.RdfaException;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author ???chenchangtong
 * @date ???Created 2021/12/9 10:31
 * @description????????????????????????
 */
@Component
@Slf4j
public class ElectricityPriceSelectHandler {

    final private String HASH_KEY_SEPARATOR_SPILT = "#";
//    ThreadLocal<SimpleDateFormat> sf_hh = new ThreadLocal<SimpleDateFormat>(){
//        @Override
//        protected SimpleDateFormat initialValue() {
//            return new SimpleDateFormat("HH:mm:ss");
//        }
//    };

    ThreadLocal<SimpleDateFormat> sf_yyyy = new ThreadLocal<SimpleDateFormat>(){
        @Override
        protected SimpleDateFormat initialValue() {
            return new SimpleDateFormat("yyyy");
        }
    };

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
    @Autowired // ???????????? redis ??????
    private CacheService cacheService;
    @Autowired
    private CimPriceClient cimPriceClient;
    @Autowired
    private CacheClient cacheClient;
//    @Autowired
//    private StringRedisTemplate redisTemplate;
//    private RedissonConfig config;
//    @Autowired
//    private RedissonClient redissonClient;
//    @Autowired
//    private RedissonRedDisLock redDisLock;

    /**
     * ????????????
     * @param requestDto
     * @return
     */
    public RdfaResult<ElectricityPriceValueDetailRespDTO> selectElePrice(@Validated @RequestBody ElectricityPriceValueReqDTO requestDto){
        String timeStr = PriceDateUtils.formatTimeStr(requestDto.getEffectiveTime());
        if (StringUtils.isBlank(timeStr)){
            return RdfaResult.fail(ErrorCodeEnum.SELECT_PARAM_TIME_ERROR.getErrorCode(),ErrorCodeEnum.SELECT_PARAM_TIME_ERROR.getErrorMsg());
        }
        //??????redis???????????????????????????????????????????????????????????????????????????????????????,????????????:????????????+??????id,
        requestDto.setEffectiveTime(timeStr);
        String key = requestDto.getSystemCode() + CommonConstant.KEY_SPERATOR + requestDto.getEquipmentId();
        ElectricityPriceValueDetailRespDTO resp = getDataFromRedis(key,requestDto);
        if (resp != null){
            log.info("get data from redis !");
            return RdfaResult.success(resp);
        }
        RdfaResult<ElectricityPriceValueDetailRespDTO> detailFromDB = getPriceDetailFromDB(key, requestDto);
        return detailFromDB;
    }

    @Autowired
    private CimApiClient cimApiClient;
    /**
     * ??????????????????????????????
     * @param cimPointPriceReq
     * @return
     */
    @MyCacheable(key = "#cimPointPriceReq.systemCode,#cimPointPriceReq.deviceId,#cimPointPriceReq.date", timeout = 24
            * 60 * 60, random = 300)
    public RdfaResult<ElectricityPricePointDetailRespDTO> selectPointRecord(ElectricityCimPointPriceReq cimPointPriceReq) {
        CimPointPriceReq priceReq = BeanUtil.map(cimPointPriceReq, CimPointPriceReq.class);
        CimResponse<CimDayPointPriceResp> dayPointRecord = cimPriceClient.getDayPointRecord(priceReq);
        if (!Objects.isNull(dayPointRecord) && !Objects.isNull(dayPointRecord.getCode()) && !Objects.isNull(dayPointRecord.getData())){
            if (ErrorCodeEnum.REQUEST_HANDLER_SUCCESS.getErrorCode().equals(dayPointRecord.getCode().toString())){
                ElectricityPricePointDetailRespDTO respDTO = BeanUtil.map(dayPointRecord.getData(), ElectricityPricePointDetailRespDTO.class);
                List<ElectricityPricePointDetailRespDTO.ElectricityCimPointPriceDetail> priceDetails = BeanUtil.mapList(dayPointRecord.getData().getPriceDataList(), ElectricityPricePointDetailRespDTO.ElectricityCimPointPriceDetail.class);
                respDTO.setPriceDataList(priceDetails);
                return RdfaResult.success(dayPointRecord.getCode().toString(),dayPointRecord.getMsg(),respDTO);
            }else {
                return RdfaResult.fail(dayPointRecord.getCode().toString(),dayPointRecord.getMsg());
            }
        }
        return RdfaResult.fail(ErrorCodeEnum.REQUEST_HANDLER_FAIL.getErrorCode(),ErrorCodeEnum.REQUEST_HANDLER_FAIL.getErrorMsg());
    }

    public PagedRdfaResult<ElectricityPriceVersionsRespDTO> selectVersions(@Validated @RequestBody ElectricityPriceVersionsReqDTO requestDto) throws ParseException {
        List<ElectricityPriceVersionsRespDTO> respDTOS = new ArrayList<>();
        Page<Object> page = PageHelper.startPage(requestDto.getPageNum(), requestDto.getPageSize());
        //??????????????????????????????????????????
        List<ElectricityPriceVersionBO> versionBos = electricityPriceVersionService.selectEquVersionsByCondition(requestDto.getEquipmentId(),requestDto.getSystemCode());
        for (ElectricityPriceVersionBO versionBo : versionBos){
            ElectricityPriceVersionsRespDTO respDTO = new ElectricityPriceVersionsRespDTO();
            respDTO.setVersionId(versionBo.getVersionId());
            respDTO.setVersionName(versionBo.getVersionName());
            List<String> seasonList = electricityPriceSeasonService.selectSeasonGroupByCondition(versionBo.getVersionId());
            String seasons = StringUtils.join(seasonList, CommonConstant.SEPARATOR_SPILT);
            respDTO.setSeasons(seasons);
            respDTO.setStartDate(sf_dd.get().format(versionBo.getStartDate()));
            respDTO.setVersionStatus(getVersionStatus(versionBo));
            respDTOS.add(respDTO);
        }
        removeThreadLocal();
        return PagedRdfaResult.success(page.getPageNum(), page.getPageSize(), page.getTotal(),respDTOS);
    }

    public RdfaResult<ElectricityPriceVersionDetailRespDTO> versionDetail(String equipmentId,String systemCode,String versionId){
        //1????????????????????????????????????
        ElectricityPriceVersionBO versionBo = electricityPriceVersionService.selectVersionByCondition(equipmentId,systemCode,versionId);
        if (versionBo == null){
            return RdfaResult.fail(ErrorCodeEnum.SELECT_VERSION_VALID_ERROR.getErrorCode(),ErrorCodeEnum.SELECT_VERSION_VALID_ERROR.getErrorMsg());
        }
        //2????????????????????????????????????
        List<ElectricityPriceRuleBO> ruleBos = electricityPriceRuleService.selectRuleListByVersionId(versionId);
        if (CollectionUtils.isEmpty(ruleBos)){
            return RdfaResult.fail(ErrorCodeEnum.SELECT_RULE_VALID_ERROR.getErrorCode(),ErrorCodeEnum.SELECT_RULE_VALID_ERROR.getErrorMsg());
        }
        //3???????????????ID?????????ID???cimCode ??????????????????
//        ElectricityPriceEquipmentBO equipmentBo = null;
//        try {
//            equipmentBo = electricityPriceEquipmentService.selectEquByCondition(equipmentId, versionId, systemCode);
//        }catch (RdfaException e){
//            log.error("????????????Id?????????ID???cim?????????????????????????????? {}",e.getMessage());
//            return RdfaResult.fail(ErrorCodeEnum.SELECT_EQUIPMENT_VALID_ERROR.getErrorCode(),ErrorCodeEnum.SELECT_EQUIPMENT_VALID_ERROR.getErrorMsg());
//        }
        //4???????????????ID??????????????????ID
        ElectricityPriceVersionDetailRespDTO respDTO = null;
        try {
            respDTO = convertVersionDetail(versionBo,ruleBos,null);
        }catch (RdfaException e){
            log.error(e.getMessage());
            return RdfaResult.fail(ErrorCodeEnum.SELECT_SEASON_VALID_ERROR.getErrorCode(),ErrorCodeEnum.SELECT_SEASON_VALID_ERROR.getErrorMsg());
        }
        return RdfaResult.success(respDTO);
    }

    /**
     * ?????????????????????????????????
     * @param reqDTO
     * @return
     */
    public RdfaResult<ElectricityPriceVersionDetailRespDTO> currentVersionDetail(ElectricityPriceCurrentVersionDetailReqDTO reqDTO) {
        Date activeTime = new Date();
        //??????????????????????????????
        ElectricityPriceEquVersionView equVersionView = electricityPriceVersionService.selectNearestVersionByCondition(reqDTO.getEquipmentId(), reqDTO.getSystemCode(),activeTime);
        if (Objects.isNull(equVersionView)){
            return RdfaResult.fail(ErrorCodeEnum.SELECT_CURRENT_VERSION_VALID_ERROR.getErrorCode(),ErrorCodeEnum.SELECT_CURRENT_VERSION_VALID_ERROR.getErrorMsg());
        }
        RdfaResult<ElectricityPriceVersionDetailRespDTO> rdfaResult = versionDetail(reqDTO.getEquipmentId(), reqDTO.getSystemCode(), equVersionView.getVersionId());
        return rdfaResult;
    }

    /**
     *
     * @param reqDTOList
     */
    public RdfaResult<ElectricityPriceVersionDetailListRespDTO> currentVersionDetailList(List<ElectricityPriceCurrentVersionDetailReqDTO> reqDTOList) {
        //1????????? ??????ID?????????Code??????????????? ??????????????????
        ElectricityPriceVersionDetailListRespDTO respDTO = new ElectricityPriceVersionDetailListRespDTO();
        List<ElectricityPriceVersionDetailRespDTO> respDTOList = new ArrayList<>();
        for (ElectricityPriceCurrentVersionDetailReqDTO reqDTO : reqDTOList){
            if (StringUtils.isBlank(reqDTO.getEquipmentId()) || StringUtils.isBlank(reqDTO.getSystemCode())){
                continue;
            }
            RdfaResult<ElectricityPriceVersionDetailRespDTO> rdfaResult = currentVersionDetail(reqDTO);
            if (rdfaResult.isSuccess()){
                respDTOList.add(rdfaResult.getData());
            }
        }
        respDTO.setVersionDetailList(respDTOList);
        return CollectionUtils.isEmpty(respDTOList) ? RdfaResult.fail(ErrorCodeEnum.SELECT_VERSION_VALID_ERROR.getErrorCode(),ErrorCodeEnum.SELECT_VERSION_VALID_ERROR.getErrorMsg())
                                                        : RdfaResult.success(respDTO);
    }

    private ElectricityPriceValueDetailRespDTO getDataFromRedis(String key ,ElectricityPriceValueReqDTO requestDto) {
        ElectricityPriceValueDetailRespDTO respDTO = null;
        try{ // 2022-07-13#2099-12-31#919898525357514752#04-01#06-30#tp
            Set<String> hKeys = cacheService.getSortHKeys(key, CommonConstant.ELECTRICITY_PRICE);
            for (String hkey : hKeys){
                String activeMonthDay = requestDto.getEffectiveTime().substring(CommonConstant.TIME_MONTH_DAY_SPILT);
                String[] split = hkey.split(HASH_KEY_SEPARATOR_SPILT);
                if (PriceDateUtils.beforeOrEqual(split[0],requestDto.getEffectiveTime()) &&//???????????????????????????????????????
                        PriceDateUtils.afterOrEqual(split[1],requestDto.getEffectiveTime()) &&
                        PriceDateUtils.beforeOrEqual(split[3],activeMonthDay) && PriceDateUtils.afterOrEqual(split[4],activeMonthDay)){
                    List<ElectricityPriceDetailCache> seasonPrices = cacheService.getListHashData(key,CommonConstant.ELECTRICITY_PRICE,hkey,ElectricityPriceDetailCache.class);
                    if (!CollectionUtils.isEmpty(seasonPrices)){
                        respDTO = ElectricityPriceValueDetailRespDTO.builder().pricingMethod(split[5]).build();
                        List<ElectricityPriceValueDetailRespDTO.PriceDetail> detailList = seasonPrices.stream().map(item -> {
                            return ElectricityPriceValueDetailRespDTO.PriceDetail.builder().elePrice(StringUtils.isNotBlank(item.getPrice())?new BigDecimal(item.getPrice()):null).
                                    startTime(StringUtils.isNotBlank(item.getStartTime())?item.getStartTime():"00:00").endTime(StringUtils.isNotBlank(item.getEndTime())?item.getEndTime():"24:00").step(item.getStep()).startStep(item.getStartStep()).periods(item.getPeriods()).
                                    endStep(item.getEndStep()).build();
                        }).collect(Collectors.toList());
                        respDTO.setPriceDetails(detailList);
                    }
                    break;
                }
                if (CommonConstant.LEAP_YEAR_FEB_END.equals(activeMonthDay)){//??????2???29????????????????????????2???28????????????
                    activeMonthDay = CommonConstant.NOLEAP_YEAR_FEB_END;
                    if (PriceDateUtils.beforeOrEqual(split[0],requestDto.getEffectiveTime()) &&//???????????????????????????????????????
                            PriceDateUtils.afterOrEqual(split[1],requestDto.getEffectiveTime()) &&
                            PriceDateUtils.beforeOrEqual(split[3],activeMonthDay) && PriceDateUtils.afterOrEqual(split[4],activeMonthDay)){
                        Integer year = Integer.valueOf(sf_yyyy.get().format(sf_dd.get().parse(split[0])));
                        if((year%4==0 && year%100!=0) || (year%400==0)){//????????????????????????
                            break;
                        }
                        List<ElectricityPriceDetailCache> seasonPrices = cacheService.getListHashData(key,CommonConstant.ELECTRICITY_PRICE,hkey,ElectricityPriceDetailCache.class);
                        if (seasonPrices!=null && seasonPrices.size() > 0){
                            respDTO = ElectricityPriceValueDetailRespDTO.builder().pricingMethod(split[5]).build();
                            List<ElectricityPriceValueDetailRespDTO.PriceDetail> detailList = seasonPrices.stream().map(item -> {
                                return ElectricityPriceValueDetailRespDTO.PriceDetail.builder().elePrice(StringUtils.isNotBlank(item.getPrice())?new BigDecimal(item.getPrice()):null).
                                        startTime(item.getStartTime()).endTime(item.getEndTime()).step(item.getStep()).startStep(item.getStartStep()).periods(item.getPeriods()).
                                        endStep(item.getEndStep()).build();
                            }).collect(Collectors.toList());
                            respDTO.setPriceDetails(detailList);
                        }
                        break;
                    }
                }
            }
        }catch (Exception e){
            log.error("get data from redis has error, {} " , e.getMessage());
        }
        return respDTO;
    }

    private RdfaResult<ElectricityPriceValueDetailRespDTO> getPriceDetailFromDB(String key ,ElectricityPriceValueReqDTO requestDto){
//        String lockKey = key + CommonConstant.KEY_SPERATOR + requestDto.getEffectiveTime();
//        Lock lock = null;
//        try {//TODO ????????????????????????
//            lock = redDisLock.lock(lockKey);
        //?????????redis??????
        ElectricityPriceValueDetailRespDTO respDTO = getDataFromRedis(key,requestDto);
        if (respDTO != null){
            log.info("get data from redis !");
            return RdfaResult.success(respDTO);
        }
        log.info("can not get data from redis,begin select from database");

        //1???????????????ID???systemCode??????????????????????????????????????????????????? < ???????????? ?????????
        Date activeTime = null;
        try {
            activeTime = sf_dd.get().parse(requestDto.getEffectiveTime());
        } catch (ParseException e) {
            log.error("?????????????????? ",e.getMessage());
            return RdfaResult.fail(ErrorCodeEnum.SELECT_PARAM_TIME_ERROR.getErrorCode(),ErrorCodeEnum.SELECT_PARAM_TIME_ERROR.getErrorMsg());
        }
        ElectricityPriceEquVersionView equVersionView = electricityPriceEquipmentService.selectEquVersionLastOneValidByTime(requestDto.getEquipmentId(), requestDto.getSystemCode(),activeTime);
        if (equVersionView == null){
            return RdfaResult.fail(ErrorCodeEnum.SELECT_VERSION_VALID_ERROR.getErrorCode(),ErrorCodeEnum.SELECT_VERSION_VALID_ERROR.getErrorMsg());
        }
        ElectricityPriceRuleBO electricityPriceRuleBo = electricityPriceRuleService.selectRuleByCondition(equVersionView.getRuleId(),equVersionView.getVersionId());
        if (electricityPriceRuleBo == null){
            return RdfaResult.fail(ErrorCodeEnum.SELECT_RULE_VALID_ERROR.getErrorCode(),ErrorCodeEnum.SELECT_RULE_VALID_ERROR.getErrorMsg());
        }
        //?????????????????????ID?????????ID????????????????????????????????????ID
        ElectricityPriceSeasonBO electricityPriceSeasonBO = null;
        try {
            electricityPriceSeasonBO = electricityPriceSeasonService.selectSeasonByCondition(equVersionView.getVersionId(), equVersionView.getRuleId(), activeTime);
        } catch (RdfaException e) {
            log.error("??????????????????????????????????????????, {} ",e.getMessage());
            return RdfaResult.fail(ErrorCodeEnum.SELECT_SEASON_VALID_ERROR.getErrorCode(),ErrorCodeEnum.SELECT_SEASON_VALID_ERROR.getErrorMsg());
        }
        //????????????ID?????????ID??????????????????
        List<ElectricityPriceDetailBO> detailBos = electricityPriceDetailService.selectDetailByCondition(equVersionView.getVersionId(),equVersionView.getRuleId(),electricityPriceSeasonBO.getSeasonId());
        if (CollectionUtils.isEmpty(detailBos)){
            return RdfaResult.fail(ErrorCodeEnum.SELECT_DETAIL_VALID_ERROR.getErrorCode(),ErrorCodeEnum.SELECT_DETAIL_VALID_ERROR.getErrorMsg());
        }
        respDTO = convert(electricityPriceRuleBo,electricityPriceSeasonBO, detailBos);
        putRedisValue(key,equVersionView,electricityPriceSeasonBO,detailBos);
        removeThreadLocal();
        return RdfaResult.success(respDTO);
//        } catch(LockFailException e){
//            log.error(e.getMessage(), e);
//            return RdfaResult.fail(ErrorCodeEnum.REIDS_LOCK_ERROR.getErrorCode(),ErrorCodeEnum.REIDS_LOCK_ERROR.getErrorMsg());
//        } finally{
////            redDisLock.unlock(lock);
//        }
    }

    private void putRedisValue(String key,ElectricityPriceEquVersionView equVersionView,
                               ElectricityPriceSeasonBO seasonBO ,
                               List<ElectricityPriceDetailBO> detailBos){
        try{
            //?????? hash value???,?????? redis ??????
            String hashKey = new StringBuilder(sf_dd.get().format(equVersionView.getStartDate())).append(HASH_KEY_SEPARATOR_SPILT)
                    .append(sf_dd.get().format(equVersionView.getEndDate())).append(HASH_KEY_SEPARATOR_SPILT).append(equVersionView.getVersionId()).append(HASH_KEY_SEPARATOR_SPILT)
                    .append(seasonBO.getSeaStartDate()).append(HASH_KEY_SEPARATOR_SPILT).append(seasonBO.getSeaEndDate()).append(HASH_KEY_SEPARATOR_SPILT)
                    .append(seasonBO.getPricingMethod()).toString();
            List<ElectricityPriceDetailCache> seasonPrices = cacheService.getHashData(key, CommonConstant.ELECTRICITY_PRICE, hashKey);
            if (CollectionUtils.isEmpty(seasonPrices)){//????????????????????????????????????redis?????????????????????
                seasonPrices = detailBos.stream().map(detailBO -> {
                    return ElectricityPriceDetailCache.builder().price(detailBO.getPrice()).startTime(detailBO.getStartTime()).periods(detailBO.getPeriods()).
                            endTime(detailBO.getEndTime()).step(detailBO.getStep()).startStep(detailBO.getStartStep()).endStep(detailBO.getEndStep()).build();
                }).collect(Collectors.toList());
                cacheService.hPutWithTimeOut(key,CommonConstant.ELECTRICITY_PRICE,hashKey,JSONObject.toJSONString(seasonPrices),getExpireSeconds(expireBase,randomNum));
            }
        }catch (Exception e){
            log.error("redis???????????? {}",e.getMessage());
        }
    }

    private long getExpireSeconds(long expireBase,long randomNum){
        return expireBase + randomNum;
    }


    private ElectricityPriceValueDetailRespDTO convert(ElectricityPriceRuleBO ruleBo, ElectricityPriceSeasonBO seasonBO, List<ElectricityPriceDetailBO> detailBos){
        ElectricityPriceValueDetailRespDTO respDTO = ElectricityPriceValueDetailRespDTO.builder()
                .strategy(ruleBo.getStrategy())
                .pricingMethod(seasonBO.getPricingMethod())
                .baseCapacityPrice(StringUtils.isNotBlank(ruleBo.getTransformerCapacityPrice())?new BigDecimal(ruleBo.getTransformerCapacityPrice()):null)
                .maxCapacityPrice(StringUtils.isNotBlank(ruleBo.getMaxCapacityPrice())?new BigDecimal(ruleBo.getMaxCapacityPrice()):null).build();
        List<ElectricityPriceValueDetailRespDTO.PriceDetail> detailList = new ArrayList<>();
        for (ElectricityPriceDetailBO detailBo : detailBos){
            ElectricityPriceValueDetailRespDTO.PriceDetail detail = new ElectricityPriceValueDetailRespDTO.PriceDetail();
            detail.setPeriods(detailBo.getPeriods());
            detail.setStartTime(StringUtils.isNotBlank(detailBo.getStartTime())?detailBo.getStartTime():"00:00");
            detail.setEndTime(StringUtils.isNotBlank(detailBo.getEndTime())?detailBo.getEndTime():"24:00");
            detail.setStep(detailBo.getStep());
            detail.setStartStep(detailBo.getStartStep());
            detail.setEndStep(detailBo.getEndStep());
            detail.setElePrice(StringUtils.isNotBlank(detailBo.getPrice())?new BigDecimal(detailBo.getPrice()):null);
            detailList.add(detail);
        }
        respDTO.setPriceDetails(detailList);
        return respDTO;
    }

    private ElectricityPriceVersionDetailRespDTO convertVersionDetail
            (ElectricityPriceVersionBO versionBo, List<ElectricityPriceRuleBO> ruleBos,
             ElectricityPriceEquipmentBO equipmentBo) throws RdfaException{
        ElectricityPriceVersionDetailRespDTO.VersionEquipmentDTO electricityPriceEquipmentDTO = ElectricityPriceVersionDetailRespDTO.VersionEquipmentDTO.builder().equipmentId(versionBo.getEquipmentId()).systemCode(versionBo.getSystemCode()).build();
        ElectricityPriceVersionDetailRespDTO respDTO = ElectricityPriceVersionDetailRespDTO.builder().versionId(versionBo.getVersionId()).versionName(versionBo.getVersionName())
                .startDate(sf_dd.get().format(versionBo.getStartDate())).endDate(sf_dd.get().format(versionBo.getEndDate())).systemCode(versionBo.getSystemCode()).cimName(versionBo.getSystemName())
                .province(versionBo.getProvince()).provinceCode(versionBo.getProvinceCode()).city(versionBo.getCity()).cityCode(versionBo.getCityCode()).electricityPriceEquipmentDTO(electricityPriceEquipmentDTO)
                .district(versionBo.getDistrict()).districtCode(versionBo.getDistrictCode()).priceType(versionBo.getPriceType()).enterprise(versionBo.getEnterprise())
                .bindType(versionBo.getBindType()).build();


        List<ElectricityPriceVersionDetailRespDTO.VersionRuleDTO> ruleDTOS = ruleBos.stream().map(item -> {
            ElectricityPriceVersionDetailRespDTO.VersionRuleDTO ruleDTO = ElectricityPriceVersionDetailRespDTO.VersionRuleDTO.builder().ruleId(item.getRuleId()).strategy(item.getStrategy())
                    .transformerCapacityPrice(item.getTransformerCapacityPrice()).maxCapacityPrice(item.getMaxCapacityPrice())
                    .industry(item.getIndustry()).voltageLevel(item.getVoltageLevel()).build();
            //?????? ruleId?????????ID ?????? seasonId??????
            List<ElectricityPriceSeasonBO> seasonBOS = electricityPriceSeasonService.selectSeasonByCondition(item.getVersionId(), item.getRuleId());
            List<ElectricityPriceVersionDetailRespDTO.VersionSeasonPriceDetail> versionSeasonPriceDetails = new ArrayList<>();
            for (ElectricityPriceSeasonBO seasonBO : seasonBOS){
                ElectricityPriceVersionDetailRespDTO.VersionSeasonPriceDetail seasonPriceDetail = ElectricityPriceVersionDetailRespDTO.VersionSeasonPriceDetail.builder().season(seasonBO.getSeason()).seasonId(seasonBO.getSeasonId())
                        .seaStartDate(seasonBO.getSeaStartDate()).seaEndDate(seasonBO.getSeaEndDate()).pricingMethod(seasonBO.getPricingMethod()).build();
                //????????????ID?????????ID ?????? ????????????
                List<ElectricityPriceDetailBO> detailBos = electricityPriceDetailService.selectDetailByCondition(versionBo.getVersionId(),seasonBO.getRuleId(), seasonBO.getSeasonId());
                List<ElectricityPriceVersionDetailRespDTO.VersionPriceDetail> priceDetailDTOs = new ArrayList<>();
                for (ElectricityPriceDetailBO detailBo : detailBos) {
                    ElectricityPriceVersionDetailRespDTO.VersionPriceDetail priceDetail = ElectricityPriceVersionDetailRespDTO.VersionPriceDetail.builder().detailId(detailBo.getDetailId())
                            .periods(detailBo.getPeriods()).price(detailBo.getPrice()).detailId(detailBo.getDetailId())
                            .step(detailBo.getStep()).startStep(detailBo.getStartStep()).endStep(detailBo.getEndStep())
                            .startTime(detailBo.getStartTime()).endTime(detailBo.getEndTime()).build();
                    //?????? seasonId ???????????????
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
        Integer versionStatus = CommonConstant.VERSION_HISTORY;
        if (PriceDateUtils.beforeNoEqual(versionBo.getEndDate(), PriceDateUtils.getToday())){
            versionStatus = CommonConstant.VERSION_HISTORY; // ???????????? < ?????? ????????????
        }else if (PriceDateUtils.beforeOrEqual(versionBo.getStartDate(),PriceDateUtils.getToday())
                && PriceDateUtils.afterOrEqual(versionBo.getEndDate(),PriceDateUtils.getToday())){
            versionStatus = CommonConstant.VERSION_CURRENT; //???????????? <= ?????? && ?????? <= ???????????? ????????????
        }else if (PriceDateUtils.afterNoEqual(versionBo.getStartDate(),PriceDateUtils.getToday())){
            versionStatus = CommonConstant.VERSION_FUTURE;//???????????? > ?????? ????????????
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
