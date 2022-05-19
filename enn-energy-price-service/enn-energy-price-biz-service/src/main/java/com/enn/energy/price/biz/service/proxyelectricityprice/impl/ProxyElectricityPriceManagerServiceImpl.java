package com.enn.energy.price.biz.service.proxyelectricityprice.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.enn.energy.price.biz.service.bo.ElectricityPriceDictionaryBO;
import com.enn.energy.price.biz.service.bo.proxyprice.*;
import com.enn.energy.price.biz.service.convertMapper.ElectricityPriceVersionUpdateBOConverMapper;
import com.enn.energy.price.biz.service.proxyelectricityprice.ProxyElectricityPriceManagerService;
import com.enn.energy.price.common.constants.CommonConstant;
import com.enn.energy.price.common.enums.BoolLogic;
import com.enn.energy.price.common.enums.ChangeTypeEum;
import com.enn.energy.price.common.error.ErrorCodeEnum;
import com.enn.energy.price.common.utils.BeanUtil;
import com.enn.energy.price.common.utils.SnowFlake;
import com.enn.energy.price.dal.mapper.ext.ExtElectricityPriceDictionaryMapper;
import com.enn.energy.price.dal.mapper.ext.proxyprice.*;
import com.enn.energy.price.dal.mapper.mbg.*;
import com.enn.energy.price.dal.po.ext.ElectricityPriceDetailPO;
import com.enn.energy.price.dal.po.mbg.*;
import com.github.xiaoymin.knife4j.core.util.CollectionUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import top.rdfa.framework.biz.ro.RdfaResult;
import top.rdfa.framework.concurrent.redis.lock.RedissonRedDisLock;
import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 代购电价service实现
 *
 * @author sunjidong
 * @date 2022/5/1
 **/
@Service
@Slf4j
public class ProxyElectricityPriceManagerServiceImpl implements ProxyElectricityPriceManagerService {

    @Autowired
    ElectricityPriceVersionCustomMapper electricityPriceVersionCustomMapper;

    @Autowired
    ElectricityPriceStructureRuleCustomMapper electricityPriceStructureRuleCustomMapper;

    @Autowired
    ElectricityPriceStructureCustomMapper electricityPriceStructureCustomMapper;

    @Autowired
    ElectricityPriceEquipmentCustomMapper electricityPriceEquipmentCustomMapper;

    @Autowired
    ElectricityPriceSeasonSectionCustomMapper electricityPriceSeasonSectionCustomMapper;

    @Autowired
    ElectricityTimeSectionCustomMapper electricityTimeSectionCustomMapper;

    @Autowired
    ElectricityPriceRuleCustomMapper electricityPriceRuleCustomMapper;

    @Autowired
    ElectricityPriceCustomMapper electricityPriceCustomMapper;

    @Resource
    ExtElectricityPriceDictionaryMapper electricityPriceDictionaryCustomMapper;

    @Resource
    ElectricityPriceVersionMapper electricityPriceVersionMapper;

    @Resource
    ElectricityPriceStructureMapper electricityPriceStructureMapper;

    @Resource
    ElectricityPriceEquipmentMapper electricityPriceEquipmentMapper;

    @Resource
    private ElectricityPriceStructureRuleMapper priceStructureRuleMapper;

    @Resource
    private ElectricitySeasonSectionMapper seasonSectionMapper;

    @Resource
    private ElectricityTimeSectionMapper timeSectionMapper;

    @Resource
    private ElectricityPriceRuleMapper priceRuleMapper;

    @Resource
    private ElectricityPriceMapper priceMapper;

    @Resource
    private RedissonRedDisLock redDisLock;


    @Value("${system.tenantId}")
    private String tenantId;

    @Value("${system.tenantName}")
    private String tenantName;


    /**
     * @describtion 通过匹配季节三要素与规则三要素，生成规则电价明细
     * @author sunjidong
     * @date 2022/5/3 9:07
     * @param priceStructureRuleList,priceRuleList
     */
    private void generatePriceRuleDetail(List<ElectricityPriceStructureRule> priceStructureRuleList,
                                         List<ElectricityPriceRule> priceRuleList){
        //先找到默认类型
        ElectricityPriceStructureRule defaultPriceStructureRule = priceStructureRuleList.stream().filter(priceStructureRule -> CommonConstant.DEFAULT_TYPE.equals( priceStructureRule.getIndustries() ) ).collect(Collectors.toList()).get(0);

        List<ElectricityPriceRule> toBeUpdatedPriceRuleList = priceRuleList.stream().map(priceRule -> {
            String industry = priceRule.getIndustry();
            String strategy = priceRule.getStrategy();
            String voltageLevel = priceRule.getVoltageLevel();
            boolean matchFail = true;
            for (ElectricityPriceStructureRule priceStructureRule : priceStructureRuleList) {
                String industries = priceStructureRule.getIndustries();
                String strategies = priceStructureRule.getStrategies();
                String voltageLevels = priceStructureRule.getVoltageLevels();
                if (industries.contains(industry) && strategies.contains(strategy) && voltageLevels.contains(voltageLevel)) {
                    priceRule.setStructureRuleId(priceStructureRule.getStructureRuleId());
                    matchFail = false;
                    break;
                }
            }
            if(matchFail){
                priceRule.setStructureRuleId(defaultPriceStructureRule.getStructureRuleId());
            }
            return priceRule;
        }).collect(Collectors.toList());

        //更新电价规则
        toBeUpdatedPriceRuleList.forEach(toBeUpdatedPriceRule -> {
            priceRuleMapper.insert(toBeUpdatedPriceRule);
        });
    }







    @Override
    @Transactional(rollbackFor = Exception.class)
    public RdfaResult<Boolean> updatePriceVersion(ElectricityPriceVersionUpdateBO electricityPriceVersionUpdateBO) {
        //1.根据变更类型判断出体系的变更状态
        //1.1根据是否有id区分是否新增
        List<ElectricityPriceStructureUpdateBO> electricityPriceStructureUpdateBOList = electricityPriceVersionUpdateBO.getElectricityPriceStructureUpdateBOList();
        List<ElectricityPriceStructureUpdateBO> addCollect = electricityPriceStructureUpdateBOList.stream().filter( t -> StringUtils.isEmpty( t.getId() )).collect( Collectors.toList() );
        //1.2根据changeType判断是否修改
        List<ElectricityPriceStructureUpdateBO> updateCollect = electricityPriceStructureUpdateBOList.stream().filter( t -> ChangeTypeEum.UPDATE.getType().equals( t.getChangeType() ) ).collect( Collectors.toList() );
        //1.3查询版本下面的体系列表比对哪些缺失
        List<String> deleteIdList = queryDeleteStructureIds( electricityPriceStructureUpdateBOList, electricityPriceVersionUpdateBO.getId() );
        //2.1 增加体系直接增加
        if(CollectionUtils.isNotEmpty(addCollect)){
            addCollect.forEach( t->{
                createStructure(t,electricityPriceVersionUpdateBO.getId());
            } );
        }
        //2.2 删除体系直接删除
        if(CollectionUtils.isNotEmpty( deleteIdList )){
            deleteIdList.forEach( this::deleteStructure );
        }
        //2.3 修改体系
        if(CollectionUtils.isNotEmpty(updateCollect)){
            updateCollect.forEach( t->{
                /*updateStructure(t,electricityPriceVersionUpdateBO.getId());*/
                updateStructurePlan3( t,electricityPriceVersionUpdateBO.getId() );

            } );
        }
        return RdfaResult.success( true );

    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public RdfaResult<Boolean> deletePriceVersion(ElectricityPriceVersionDeleteBO electricityPriceVersionDeleteBO) {
        //根据电价版本id查询电价体系id
        Map<String, Object> map=new HashMap<>();
        map.put( "versionId", electricityPriceVersionDeleteBO.getId());
        map.put( "state",BoolLogic.NO.getCode() );
        List<ElectricityPriceStructure> electricityPriceStructures = electricityPriceStructureCustomMapper.queryListByConditions( map );
        //根据电价体系id删除
        if(CollectionUtils.isNotEmpty(electricityPriceStructures  )){
            electricityPriceStructures.forEach( t->{
                deleteStructure( t.getStructureId() );
            } );
        }
        //查询当前版本的上个版本
        ElectricityPriceVersion electricityPriceVersion=new ElectricityPriceVersion();
        electricityPriceVersion.setProvinceCode( electricityPriceVersionDeleteBO.getProvinceCode() );
        electricityPriceVersion.setStartDate( DateUtil.parse(electricityPriceVersionDeleteBO.getStartDate(), DatePattern.NORM_DATE_PATTERN) );
        electricityPriceVersion.setEndDate(  DateUtil.parse(electricityPriceVersionDeleteBO.getEndDate(), DatePattern.NORM_DATE_PATTERN));
        ElectricityPriceVersion beforePriceVersion = electricityPriceVersionCustomMapper.queryBeforePriceVersion( electricityPriceVersion );
        //更新上一个版本 并删除当前版本
        if(ObjectUtil.isNotNull( beforePriceVersion )){
            beforePriceVersion.setEndDate( DateUtil.parse(electricityPriceVersionDeleteBO.getEndDate(), DatePattern.NORM_DATE_PATTERN) );
            beforePriceVersion.setUpdator( tenantId );
            beforePriceVersion.setUpdateTime( DateUtil.date() );
            electricityPriceVersionMapper.updateByPrimaryKey( beforePriceVersion );
        }
        HashMap<String, Object> versionUpdateMap = new HashMap<>();
        versionUpdateMap.put( "id", electricityPriceVersionDeleteBO.getId());
        versionUpdateMap.put( "afterState", BoolLogic.YES.getCode());
        versionUpdateMap.put( "state", BoolLogic.NO.getCode());
        versionUpdateMap.put( "updator",tenantId );
        versionUpdateMap.put( "updateTime",DateUtil.date() );
        electricityPriceVersionCustomMapper.updateElectricityPriceVersionCondition(versionUpdateMap);
        return RdfaResult.success(true);
    }

    @Override
    public List<ElectricityPriceVersionBO> queryPriceVersionList(String provinceCode) {
        HashMap<String, Object> map = new HashMap<>();
        map.put( "provinceCode",provinceCode );
        map.put( "state",BoolLogic.NO.getCode() );
        List<ElectricityPriceVersion> electricityPriceVersions = electricityPriceVersionCustomMapper.queryPriceVersionList( map );
        if(CollectionUtil.isEmpty(electricityPriceVersions  )){
            return null;
        }
        return ElectricityPriceVersionUpdateBOConverMapper.INSTANCE.electricityPriceVersionListPOToBO( electricityPriceVersions );
    }

    @Override
    public List<ElectricityPriceStructureBO> queryPriceVersionStructureList(String versionId) {
        Map<String, Object> map = new HashMap<>();
        map.put("versionId",versionId);
        map.put( "state",BoolLogic.NO.getCode() );
        List<ElectricityPriceStructure> electricityPriceStructures = electricityPriceStructureCustomMapper.queryListByConditions( map );
        if(CollectionUtil.isEmpty( electricityPriceStructures )){
            return null;
        }
        List<ElectricityPriceStructureBO> electricityPriceStructureBOS = ElectricityPriceVersionUpdateBOConverMapper.INSTANCE.electricityPriceStructurePOListToBOList( electricityPriceStructures );
        return electricityPriceStructureBOS;
    }

    @Override
    public ElectricityPriceStructureDetailBO getStructureDetail(String structureId) {
        //根据体系id获取体系信息
        Map<String,Object> map=new HashMap<>();
        map.put( "structureId", structureId);
        map.put( "state",BoolLogic.NO.getCode() );
        List<ElectricityPriceStructure> electricityPriceStructures = electricityPriceStructureCustomMapper.queryListByConditions( map );
        if(CollectionUtil.isEmpty( electricityPriceStructures )){
            return null;
        }
        ElectricityPriceStructure electricityPriceStructure = electricityPriceStructures.get(0);
        ElectricityPriceStructureDetailBO electricityPriceStructureDetailBO = ElectricityPriceVersionUpdateBOConverMapper.INSTANCE.ElectricityPriceStructurePOTOElectricityPriceStructureDetailBO( electricityPriceStructure );
        //创建体系规则详情
        List<ElectricityPriceStructureRuleDetailBO> electricityPriceStructureRuleDetailBOS=new ArrayList<>();
        //根据体系id获取体系规则列表
        Map<String,Object> structureRuleQueryMap=new HashMap<>();
        structureRuleQueryMap.put( "structureId", structureId);
        structureRuleQueryMap.put( "state", BoolLogic.NO.getCode());
        List<ElectricityPriceStructureRule> electricityPriceStructureRules = electricityPriceStructureRuleCustomMapper.queryElectricityPriceRulesByCondition( structureRuleQueryMap );
        //规则列表组装
        electricityPriceStructureRules.forEach( t->{
            //创建体系规则详情与季节列表
            ElectricityPriceStructureRuleDetailBO electricityPriceStructureRuleDetailBO = ElectricityPriceVersionUpdateBOConverMapper.INSTANCE.ElectricityPriceStructureRulePOTOBO( t);
            List<ElectricityPriceSeasonDetailBO> seasonDetailList=new ArrayList<>();
            //根据体系规则id查找季节列表
            HashMap<String, Object> seasonQueryMap = new HashMap<>();
            seasonQueryMap.put( "structureRuleIds", Collections.singletonList( t.getStructureRuleId() ) );
            seasonQueryMap.put( "state", BoolLogic.NO.getCode());
            List<ElectricitySeasonSection> electricitySeasonSections = electricityPriceSeasonSectionCustomMapper.querySeasonSectionIdsByStructureRuleIds( seasonQueryMap );
            //筛选出季节
            Set<String> SeasonSectionIds = electricitySeasonSections.stream().map( ElectricitySeasonSection::getSeasonSectionId ).collect( Collectors.toSet() );
            //对季节区间进行合并(同一个季节)
            SeasonSectionIds.forEach( i->{
                ElectricityPriceSeasonDetailBO electricityPriceSeasonDetailBO = new ElectricityPriceSeasonDetailBO();
                electricityPriceSeasonDetailBO.setSeasonSectionId( i );
                List<SeasonDateBO> seasonDateBOList =new ArrayList<>();
                electricitySeasonSections.stream().filter( h->h.getSeasonSectionId().equals(i) ).forEach( h->{
                    electricityPriceSeasonDetailBO.setSeasonName(h.getSeasonSectionName());
                    //添加季节区间时间信息
                    SeasonDateBO seasonDateBO = new SeasonDateBO();
                    seasonDateBO.setSeasonId( h.getSeasonSectionId() );
                    seasonDateBO.setSeaEndDate( h.getSeaEndDate() );
                    seasonDateBO.setSeaStartDate( h.getSeaStartDate() );
                    seasonDateBOList.add( seasonDateBO );
                } );
                //根据季节id查询分时区间信息
                List<ElectricityTimeSection> timeSectionList = electricityTimeSectionCustomMapper.getTimeSectionListBySeasonSectionId( i );
                Map<String, List<ElectricityTimeSection>> timeMap = timeSectionList.stream().collect(
                        Collectors.groupingBy(
                                //将时区按照策略分组
                                time -> time.getCompare() + "-" + time.getTemperature()
                        ) );
                //组装规则列表
                List<ElectricityPriceStrategyBO> electricityPriceStrategyBOList=new ArrayList<>();
                timeMap.forEach((k,v)->{
                    String[] split = k.split( "-" );
                    ElectricityPriceStrategyBO electricityPriceStrategyBO = new ElectricityPriceStrategyBO();
                    electricityPriceStrategyBO.setCompare( split[0] );
                    electricityPriceStrategyBO.setTemperature( split[1] );
                    List<ElectricityTimeSectionUpdateBO> electricityTimeSectionUpdateBOS = ElectricityPriceVersionUpdateBOConverMapper.INSTANCE.ElectricityTimeSectionPOListToBOList( v );
                    electricityPriceStrategyBO.setElectricityTimeSectionUpdateBOList( electricityTimeSectionUpdateBOS );
                    electricityPriceStrategyBOList.add( electricityPriceStrategyBO );

                });
                //策略列表添加入季节
                electricityPriceSeasonDetailBO.setElectricityPriceStrategyBOList( electricityPriceStrategyBOList );
                //季节列表时间添加入季节
                electricityPriceSeasonDetailBO.setSeasonDateList( seasonDateBOList );
                //季节添加进季节列表
                seasonDetailList.add( electricityPriceSeasonDetailBO);
            } );
            //季节列表添加进体系规则
            electricityPriceStructureRuleDetailBO.setElectricityPriceSeasonDetailBOS( seasonDetailList );
            electricityPriceStructureRuleDetailBO.setStrategies( t.getStrategies() );
            electricityPriceStructureRuleDetailBO.setId( t.getStructureRuleId() );
            electricityPriceStructureRuleDetailBO.setIndustries( t.getIndustries() );
            electricityPriceStructureRuleDetailBO.setVoltageLevels( t.getVoltageLevels() );
            //将体系规则添加进规则列表
            electricityPriceStructureRuleDetailBOS.add(electricityPriceStructureRuleDetailBO);

        } );
        //组装体系价格列表
        List<ElectricityPriceDetailPO> priceDetailPOList = electricityPriceCustomMapper.getPriceDetailListByStructureId( structureId );
        List<ElectricityPriceDetailBO> electricityPriceDetailBOs = ElectricityPriceVersionUpdateBOConverMapper.INSTANCE.electricityPriceDetailPOListToBOList( priceDetailPOList );

        //将规则列表添加进体系
        electricityPriceStructureDetailBO.setElectricityPriceStructureRuleDetailBOS( electricityPriceStructureRuleDetailBOS );
        electricityPriceStructureDetailBO.setElectricityPriceDetailBOList( electricityPriceDetailBOs );
        return electricityPriceStructureDetailBO;
    }

/**
 * 获取字典列表
 * @author sunjidong
 * @date 2022/5/15 10:27
 */
    @Override
    public Map<Integer, List<ElectricityPriceDictionaryBO>> getPriceElectricityDictionaries(String type, String province) {
        HashMap<String, Object> hashMap = new HashMap<>();
        if(StrUtil.isNotBlank(type)){
            hashMap.put(CommonConstant.TYPE, Integer.parseInt(type));
        }
        hashMap.put(CommonConstant.PROVINCE, province);
        hashMap.put(CommonConstant.STATE, CommonConstant.VALID);
        List<ElectricityPriceDictionary> electricityPriceDictionaries = electricityPriceDictionaryCustomMapper.selectDictionaryByCondition(hashMap);
        List<ElectricityPriceDictionaryBO> dictionaryBOList = BeanUtil.mapList(electricityPriceDictionaries, ElectricityPriceDictionaryBO.class);
        return dictionaryBOList.stream()
                .collect(Collectors.groupingBy(ElectricityPriceDictionaryBO ::getType));

    }

    @Override
    public RdfaResult<Boolean> versionDeleteValidate(ElectricityPriceVersionDeleteBO electricityPriceVersionDeleteBO) {
        if(DateUtil.parse(electricityPriceVersionDeleteBO.getStartDate(), DatePattern.NORM_DATE_PATTERN).isBefore( DateUtil.date() )){
            return RdfaResult.fail( ErrorCodeEnum.VERSION_IS_NOT_ALLOW_DELETE.getErrorCode(), ErrorCodeEnum.VERSION_IS_NOT_ALLOW_DELETE.getErrorMsg());
        }
        //判断版本下体系是否绑定了设备
        HashMap<String, Object> queryStructureMap = new HashMap<>();
        queryStructureMap.put( "versionId",electricityPriceVersionDeleteBO.getId() );
        queryStructureMap.put( "state", BoolLogic.NO.getCode());
        List<ElectricityPriceStructure> electricityPriceStructures = electricityPriceStructureCustomMapper.queryListByConditions( queryStructureMap );
        Set<String> bindingEquipmentStructureNameSet = electricityPriceStructures.stream().filter( t -> judgeStructureEquipmentBinding( t.getId(), t.getProvinceCode(), t.getCityCodes(), t.getDistrictCodes() ) ).collect( Collectors.toList() ).stream().map( ElectricityPriceStructure::getStructureName ).collect( Collectors.toSet() );
        if(CollectionUtils.isNotEmpty( bindingEquipmentStructureNameSet )){
            return RdfaResult.fail( ErrorCodeEnum.VERSION_IS_NOT_ALLOW_DELETE.getErrorCode(),"版本下体系名为"+StringUtils.join( bindingEquipmentStructureNameSet,"," )+"绑定了设备,不能删除" );
        }
        return RdfaResult.success( true );
    }

    private boolean judgeStructureEquipmentBinding(Long id, String provinceCode, String cityCodes, String districtCodes) {
        //根据体系id查询设备表
        List<ElectricityPriceEquipment> electricityPriceEquipments = electricityPriceEquipmentCustomMapper.queryEquipmentBindingByStructureId( String.valueOf( id ) );
        //todo 暂未知设备表怎么存储的
        List<ElectricityPriceEquipment> collect = electricityPriceEquipments.stream().filter( t -> {
            //判断值是否匹配体系的区域,
            return true;
        } ).collect( Collectors.toList() );
        return CollectionUtils.isNotEmpty( collect );
    }

    @Override
    public RdfaResult<Boolean> structureDeleteValidate(ElectricityPriceStructureDeleteValidateBO s) {
        if(DateUtil.parse(s.getEndDate(), DatePattern.NORM_DATE_PATTERN).isBefore( DateUtil.date() )){
            return RdfaResult.fail( ErrorCodeEnum.STRUCTURE_IS_NOT_ALLOW_DELETE.getErrorCode(), ErrorCodeEnum.STRUCTURE_IS_NOT_ALLOW_DELETE.getErrorMsg());
        }
        //根据体系id查询体系区域
        boolean b = judgeStructureEquipmentBinding( Long.valueOf( s.getStructureId() ), s.getProvinceCode(), s.getCityCodes(), s.getDistrictCodes() );
        if(b){
            return RdfaResult.fail( ErrorCodeEnum.STRUCTURE_IS_NOT_ALLOW_DELETE.getErrorCode(), ErrorCodeEnum.STRUCTURE_IS_NOT_ALLOW_DELETE.getErrorMsg());
        }
        return RdfaResult.success( true );
    }

    /**
     * 查找哪些体系被删除了
     * @param electricityPriceStructureUpdateBOList
     * @return
     */
    private List<String> queryDeleteStructureIds(List<ElectricityPriceStructureUpdateBO> electricityPriceStructureUpdateBOList,String versionId) {
        //根据版本id查询当前版本下的体系id列表
        Map<String,Object> map = new HashMap<>();
        map.put("versionId",versionId);
        map.put( "state",BoolLogic.NO.getCode() );
        List<ElectricityPriceStructure> electricityPriceStructures = electricityPriceStructureCustomMapper.queryListByConditions( map );
        //筛选出现有的id
        List<String> ids = electricityPriceStructureUpdateBOList.stream().map( ElectricityPriceStructureUpdateBO::getId ).filter( StringUtils::isNotEmpty ).collect( Collectors.toList() );
        //拿版本下的体系列表与现有的对比，得出哪些删除了
        return electricityPriceStructures.stream().map( ElectricityPriceStructure::getStructureId ).collect( Collectors.toList() ).stream().filter( f -> !ids.contains(f) ).collect( Collectors.toList() );
    }

    /**
     * 修改体系
     * @param priceStructureUpdateBO
     */
    private void updateStructure(ElectricityPriceStructureUpdateBO priceStructureUpdateBO,String versionId) {
        //查询当前体系绑定的设备信息
        List<ElectricityPriceEquipment> electricityPriceEquipments = electricityPriceEquipmentCustomMapper.queryEquipmentBindingByStructureId( String.valueOf(priceStructureUpdateBO.getId()) );
        //更新操作,先删除当前体系下的数据然后新增
        deleteStructure(priceStructureUpdateBO.getId());
        String structureId = createStructure( priceStructureUpdateBO, versionId );
        List<ElectricityPriceRule> electricityPriceRules = electricityPriceRuleCustomMapper.queryRuleListByStructureId( structureId );
        //更新设备绑定,根据设备绑定的规则id找到对应的三要素,匹配新体系下的三要素
        electricityPriceEquipments.forEach( t->{
            ElectricityPriceRule electricityPriceRule = priceRuleMapper.selectByPrimaryKey( Long.valueOf(t.getId()) );
            ElectricityPriceRule electricityPriceRule1 = electricityPriceRules.stream().filter( f -> {
                if (f.getIndustry().equals( electricityPriceRule.getIndustry() )
                        && f.getStrategy().equals( electricityPriceRule.getStrategy() )
                        && f.getVoltageLevel().equals( electricityPriceRule.getVoltageLevel() )) {
                    return true;
                }
                return false;
            } ).findFirst().orElse( null );
            if(Optional.ofNullable(electricityPriceRule1).isPresent()){
                String structureRuleId = electricityPriceRule1.getStructureRuleId();
                //更新设备表
                t.setStructureId(structureId );
                t.setStructureRuleId( structureRuleId );
                t.setUpdator( tenantId );
                t.setUpdateTime(DateUtil.date());
                electricityPriceEquipmentMapper.updateByPrimaryKey(t);
            }
        } );
    }


    public void updateStructurePlan3(ElectricityPriceStructureUpdateBO priceStructureUpdateBO,String versionId){
        //todo 判断体系的名称区域是否发生修改
        HashMap<String, Object> structureQueryMap = new HashMap<>();
        structureQueryMap.put( "structureId",priceStructureUpdateBO.getId() );
        structureQueryMap.put( "state", BoolLogic.NO.getCode());
        List<ElectricityPriceStructure> electricityPriceStructures = electricityPriceStructureCustomMapper.queryListByConditions( structureQueryMap );
        if(CollectionUtils.isEmpty(electricityPriceStructures)){
            return;
        }
        ElectricityPriceStructure electricityPriceStructure = electricityPriceStructures.get( 0 );
        if(!priceStructureUpdateBO.getStructureName().equals( electricityPriceStructure.getStructureName() )
                || !priceStructureUpdateBO.getProvinceCode().equals( electricityPriceStructure.getProvinceCode())
                || !priceStructureUpdateBO.getDistrictCodes().equals(electricityPriceStructure.getDistrictCodes())
                || priceStructureUpdateBO.getCityCodes().equals(electricityPriceStructure.getCityCodes()  )){
            electricityPriceStructure.setCityCodes( priceStructureUpdateBO.getCityCodes() );
            electricityPriceStructure.setDistrictCodes( priceStructureUpdateBO.getDistrictCodes() );
            electricityPriceStructure.setProvinceCode( priceStructureUpdateBO.getCityCodes() );
            electricityPriceStructure.setStructureName( priceStructureUpdateBO.getStructureName() );
            electricityPriceStructureCustomMapper.updateByStructureId( electricityPriceStructure );
        }

        String structureId = priceStructureUpdateBO.getId();
        //根据体系规则id判断体系规则的变化
        List<ElectricityPriceStructureRuleUpdateBO> electricityPriceStructureRuleUpdateBOList = priceStructureUpdateBO.getElectricityPriceStructureRuleUpdateBOList();
        List<ElectricityPriceStructureRuleUpdateBO> haveIdList = electricityPriceStructureRuleUpdateBOList.stream().filter( t -> StringUtils.isNotEmpty( t.getStructureRuleId() ) ).collect( Collectors.toList() );
        List<ElectricityPriceStructureRuleUpdateBO> addList = electricityPriceStructureRuleUpdateBOList.stream().filter( t -> StringUtils.isEmpty( t.getStructureRuleId() ) ).collect( Collectors.toList() );
        List<ElectricityPriceStructureRuleUpdateBO> updateList = haveIdList.stream().filter( t -> ChangeTypeEum.UPDATE.getType().equals( t.getChangeType() ) ).collect( Collectors.toList() );

        //根据体系id查询当前体系原有的体系规则列表
        HashMap<String , Object> structureRuleQueryMap = new HashMap<>();
        structureRuleQueryMap.put( "structureId", structureId );
        structureRuleQueryMap.put( "state",BoolLogic.NO.getCode() );
        List<ElectricityPriceStructureRule> electricityPriceStructureRules = electricityPriceStructureRuleCustomMapper.queryElectricityPriceRulesByCondition( structureRuleQueryMap );
        List<String> haveIds = haveIdList.stream().map( ElectricityPriceStructureRuleUpdateBO::getStructureRuleId ).collect( Collectors.toList() );
        List<String> deleteIds = new ArrayList<>();
        for (ElectricityPriceStructureRule t : electricityPriceStructureRules) {
            if (!haveIds.contains( t.getStructureRuleId() )) {
                String id = t.getStructureRuleId();
                deleteIds.add( id );
            }
        }
        //规则的删除
        if(CollectionUtil.isNotEmpty( deleteIds )){
            deleteStructureRuleByStructureRuleIds(deleteIds);
        }
        //规则新增
        if(CollectionUtil.isNotEmpty(addList)){
            createStructureRuleAndSeason(addList, structureId );
        }
        //规则更新
        updateList.forEach( t->{
            ElectricityPriceStructureRule structureRule = new ElectricityPriceStructureRule();
            structureRule.setStructureRuleId( t.getStructureRuleId() );
            structureRule.setIndustries( t.getIndustries() );
            structureRule.setStrategies( t.getStrategies() );
            structureRule.setVoltageLevels( t.getVoltageLevels() );
            structureRule.setUpdateTime( DateUtil.date() );
            electricityPriceStructureRuleCustomMapper.updateStructureRule( structureRule );
            //根据体系规则id查询季节列表
            HashMap<String, Object> seasonQueryMap = new HashMap<>();
            seasonQueryMap.put( "structureRuleIds",Collections.singletonList( t.getStructureRuleId() ) );
            seasonQueryMap.put( "state", BoolLogic.NO.getCode());
            List<ElectricitySeasonSection> electricitySeasonSections = electricityPriceSeasonSectionCustomMapper.querySeasonSectionIdsByStructureRuleIds( seasonQueryMap );
            //规则下面的季节列表
            Set<String> collect = new HashSet<>();
            for (ElectricitySeasonSection electricitySeasonSection : electricitySeasonSections) {
                String sectionId = electricitySeasonSection.getSeasonSectionId();
                collect.add( sectionId );
            }
            //判断哪些seasonSectionId删除了
            List<String> havaSeasonIds = t.getElectricityPriceSeasonUpdateReqVOList().stream().filter( l -> StringUtils.isNotEmpty( l.getSeasonSectionId() ) ).map( ElectricityPriceSeasonUpdateBO::getSeasonSectionId ).collect( Collectors.toList() );
            Set<String> deleteSeasonIds = collect.stream().filter( n -> !havaSeasonIds.contains( n ) ).collect( Collectors.toSet() );
            List<ElectricityPriceSeasonUpdateBO> updateSeasonList = t.getElectricityPriceSeasonUpdateReqVOList().stream().filter( n -> StringUtils.isNotEmpty( n.getSeasonSectionId() ) && ChangeTypeEum.UPDATE.getType().equals( n.getChangeType() ) ).collect( Collectors.toList() );
            List<ElectricityPriceSeasonUpdateBO> addSeasonList = t.getElectricityPriceSeasonUpdateReqVOList().stream().filter( n -> StringUtils.isEmpty( n.getSeasonSectionId() ) ).collect( Collectors.toList() );
            //删除
            if(CollectionUtils.isNotEmpty( deleteSeasonIds )){
                batchDeleteSeasonAndTime( deleteSeasonIds );
            }
            //增加
            if(CollectionUtils.isNotEmpty(addSeasonList)){
                addSeasonList.forEach( n->{
                    createSeasonsAndTimes(n,String.valueOf( structureId ),t.getStructureRuleId());
                } );
            }
            //修改
            if(CollectionUtils.isNotEmpty(updateSeasonList)){
                updateSeasonList.forEach( n->{
                    String seasonName = n.getSeasonName();
                    String seasonSectionId = n.getSeasonSectionId();
                    //日期部分
                    List<SeasonDateBO> seasonDateList = n.getSeasonDateList();
                    //季节id
                    List<SeasonDateBO> haveDateIdList = seasonDateList.stream().filter( m -> Optional.ofNullable( m.getSeasonId() ).isPresent() ).collect( Collectors.toList() );
                    List<SeasonDateBO> updateDateList = haveDateIdList.stream().filter( m -> ChangeTypeEum.UPDATE.getType().equals(m.getChangeType()  ) ).collect( Collectors.toList() );
                    List<SeasonDateBO> addDateList = seasonDateList.stream().filter( m -> !Optional.ofNullable( m.getSeasonId() ).isPresent() ).collect( Collectors.toList() );
                    //electricitySeasonSections 体系规则下的所有季节, 查询当前季节下的所有季节列表
                    List<ElectricitySeasonSection> seasonSections = electricitySeasonSections.stream().filter( m -> m.getSeasonSectionId().equals( n.getSeasonSectionId() ) ).collect( Collectors.toList() );
                    //将数据库中的季节列表匹配现有的季节列表
                    List<String> haveDateIds = haveDateIdList.stream().map( SeasonDateBO::getSeasonId ).collect( Collectors.toList() );
                    List<String> deleteDateIds = seasonSections.stream().map( ElectricitySeasonSection::getSeasonSectionId ).filter( m -> !haveDateIds.contains( m ) ).collect( Collectors.toList() );
                    //删除date_time
                    if(CollectionUtils.isNotEmpty( deleteDateIds )){
                        HashMap<String , Object> timeDeleteMap = new HashMap<>();
                        timeDeleteMap.put( "ids", deleteDateIds);
                        timeDeleteMap.put( "updateTime",DateUtil.date() );
                        timeDeleteMap.put( "state", BoolLogic.YES.getCode());
                        electricityPriceSeasonSectionCustomMapper.batchDeleteSeasonSectionByIds(timeDeleteMap  );
                    }
                    //增加date_time
                    if(CollectionUtils.isNotEmpty( addDateList )){
                        batchCreateSeasonDate(addDateList,seasonName,seasonSectionId,String.valueOf( structureId ),String.valueOf( t.getStructureRuleId() ));
                    }
                    //更新时间
                    if(CollectionUtils.isNotEmpty(updateDateList)){
                        batchUpdateSeasonDate(updateDateList);
                    }
                    //策略分时部分
                    List<ElectricityPriceStrategyBO> strategyList = n.getElectricityPriceStrategyBOList();
                    //根据季节分时查找分时list,然后根据策略分组
                    List<ElectricityTimeSection> timeSectionList = electricityTimeSectionCustomMapper.getTimeSectionListBySeasonSectionId( n.getSeasonSectionId() );
                    Map<String, List<ElectricityTimeSection>> groupStrategyList = timeSectionList.stream().collect( Collectors.groupingBy( m -> m.getCompare() + "-" + m.getTemperature() ) );
                    List<String> strategyNameList = strategyList.stream().map( m -> m.getCompare() + "-" + m.getTemperature() ).collect( Collectors.toList() );
                    groupStrategyList.forEach( (k,v)->{
                        if(strategyNameList.contains( k )){
                            strategyList.forEach( m->{
                                String strategyName = m.getCompare() + "-" + m.getTemperature();
                                if(strategyName.equals( k )){
                                    //设置标记 匹配上
                                    m.setComply( true );
                                    List<ElectricityTimeSectionUpdateBO> hasIdList = m.getElectricityTimeSectionUpdateBOList().stream().filter( h -> Optional.ofNullable( h.getId() ).isPresent() ).collect( Collectors.toList() );
                                    List<ElectricityTimeSectionUpdateBO> updateIdList = hasIdList.stream().filter( h -> ChangeTypeEum.UPDATE.getType().equals( h.getChangeType() ) ).collect( Collectors.toList() );
                                    List<ElectricityTimeSectionUpdateBO> addIdList = m.getElectricityTimeSectionUpdateBOList().stream().filter( h -> !Optional.ofNullable( h.getId() ).isPresent() ).collect( Collectors.toList() );
                                    List<Long> datasourceIdList = v.stream().map( ElectricityTimeSection::getId ).collect( Collectors.toList() );
                                    List<Long> hasIds = hasIdList.stream().map( ElectricityTimeSectionUpdateBO::getId ).collect( Collectors.toList() );
                                    List<Long> deleteIdList = datasourceIdList.stream().filter( hasIds::contains ).collect( Collectors.toList() );
                                    //删除
                                    HashMap<String, Object> deleteMap = new HashMap<>();
                                    deleteMap.put("ids",deleteIdList);
                                    deleteMap.put( "state", BoolLogic.YES.getCode());
                                    deleteMap.put( "updateTime",DateUtil.date() );
                                    electricityTimeSectionCustomMapper.batchDeleteTimeSectionByIds( deleteMap );
                                    //增加
                                    batchCreateTime(addIdList,seasonSectionId,m.getCompare(),m.getTemperature());
                                    //更新
                                    List<ElectricityTimeSection> timeUpdateList=new ArrayList<>();
                                    updateIdList.forEach( h->{
                                        ElectricityTimeSection electricityTimeSection=new ElectricityTimeSection();
                                        electricityTimeSection.setTemperature( m.getTemperature() );
                                        electricityTimeSection.setCompare( m.getCompare() );
                                        electricityTimeSection.setStartTime( h.getStartTime() );
                                        electricityTimeSection.setEndTime( h.getEndTime() );
                                        electricityTimeSection.setPeriods( h.getPeriods() );
                                        electricityTimeSection.setId( h.getId() );
                                        electricityTimeSection.setUpdateTime( DateUtil.date() );
                                        timeUpdateList.add( electricityTimeSection );
                                    } );
                                    electricityTimeSectionCustomMapper.batchUpdateTimeSection(timeUpdateList);
                                }
                            } );
                        }
                        //删除无用的time
                        List<Long> delete = v.stream().map( ElectricityTimeSection::getId ).collect( Collectors.toList() );
                        HashMap<String, Object> deleteMap = new HashMap<>();
                        deleteMap.put("ids",delete);
                        deleteMap.put( "state", BoolLogic.YES.getCode());
                        deleteMap.put( "updateTime",DateUtil.date() );
                        electricityTimeSectionCustomMapper.batchDeleteTimeSectionByIds( deleteMap );
                    } );

                    //增加未匹配上的策略
                    List<ElectricityPriceStrategyBO> addStrategyList = strategyList.stream().filter( m -> !Optional.ofNullable( m.getComply() ).isPresent()|| !m.getComply()).collect( Collectors.toList() );
                    if(CollectionUtils.isNotEmpty(addStrategyList  )){
                        addStrategyList.forEach( m->{
                            batchCreateTime(m.getElectricityTimeSectionUpdateBOList(),seasonSectionId,m.getCompare(),m.getTemperature());
                        } );
                    }
                } );
            }
        } );
        //todo 价格部分
        List<ElectricityPriceUpdateBO> rulePriceList = priceStructureUpdateBO.getElectricityPriceUpdateBOList();
        List<ElectricityPriceUpdateBO> addRuleList = rulePriceList.stream().filter( t -> StringUtils.isEmpty( t.getRuleId()) ).collect( Collectors.toList() );
        List<ElectricityPriceUpdateBO> hasRuleIdList = rulePriceList.stream().filter( t -> StringUtils.isNotEmpty( t.getRuleId() ) ).collect( Collectors.toList() );
        List<ElectricityPriceUpdateBO> updateRuleList = hasRuleIdList.stream().filter( t -> ChangeTypeEum.UPDATE.getType().equals(t.getRuleChangeType()  ) ).collect( Collectors.toList() );
        List<ElectricityPriceRule> electricityPriceRules = electricityPriceRuleCustomMapper.queryRuleListByStructureId( structureId );
        List<String> dataSourceRules = electricityPriceRules.stream().map( ElectricityPriceRule::getRuleId ).collect( Collectors.toList() );
        List<String> hasRuleId = hasRuleIdList.stream().map( ElectricityPriceUpdateBO::getRuleId ).collect( Collectors.toList() );
        List<String> deleteRuleList = dataSourceRules.stream().filter( t -> !hasRuleId.contains( t ) ).collect( Collectors.toList() );
        //删除
        if(CollectionUtils.isNotEmpty(deleteRuleList)){
            batchDeleteRulePriceAndEquipment(deleteRuleList );
        }
        //增加
        if(CollectionUtils.isNotEmpty(addRuleList)){
            addRuleList.forEach( t->{
                String structureRuleId = queryStructureRuleId( t.getIndustry(), t.getStrategy(), t.getVoltageLevel(), String.valueOf( structureId ) );
                createPriceAndRule(t,versionId,String.valueOf( structureId ),structureRuleId);
            } );
        }
        //更新
        if(CollectionUtils.isNotEmpty(updateRuleList)){
            updateRuleList.forEach( t->{
                String structureRuleId = queryStructureRuleId( t.getIndustry(), t.getStrategy(), t.getVoltageLevel(), String.valueOf( structureId ) );
                ElectricityPriceRule electricityPriceRule = new ElectricityPriceRule();
                electricityPriceRule.setVoltageLevel( t.getVoltageLevel() );
                electricityPriceRule.setStructureRuleId( structureRuleId );
                electricityPriceRule.setIndustry( t.getIndustry() );
                electricityPriceRule.setStrategy(t.getStrategy());
                electricityPriceRule.setUpdateTime( DateUtil.date() );
                electricityPriceRule.setState( Integer.valueOf( BoolLogic.NO.getCode()  ));
                electricityPriceRule.setRuleId( t.getRuleId() );
                electricityPriceRule.setMaxCapacityPrice( t.getMaxCapacityPrice() );
                electricityPriceRule.setTransformerCapacityPrice( t.getTransformerCapacityPrice() );
                electricityPriceRuleCustomMapper.updateRuleByConditions(electricityPriceRule);
                //更新价格
                ElectricityPrice electricityPrice = ElectricityPriceVersionUpdateBOConverMapper.INSTANCE.electricityPriceBOToPO( t );
                electricityPrice.setState( Integer.valueOf( BoolLogic.NO.getCode() ) );
                electricityPrice.setUpdateTime( DateUtil.date() );
                electricityPriceCustomMapper.updatePriceByConditions( electricityPrice );
                //更新设备绑定 todo(暂不考虑)
            } );
        }
    }

    /**
     * 创建分时信息
     * @param addIdList
     * @param seasonSectionId
     * @param compare
     * @param Temperature
     */
    public void batchCreateTime(List<ElectricityTimeSectionUpdateBO> addIdList,String seasonSectionId,String compare,String Temperature){
        List<ElectricityTimeSection> electricityTimeSectionList=new ArrayList<>();
        addIdList.forEach( h->{
            ElectricityTimeSection electricityTimeSection=new ElectricityTimeSection();
            String timeSectionId = String.valueOf(SnowFlake.getInstance().nextId());
            electricityTimeSection.setSeasonSectionId(seasonSectionId);
            electricityTimeSection.setTimeSectionId( timeSectionId );
            electricityTimeSection.setStartTime( h.getStartTime() );
            electricityTimeSection.setEndTime(h.getEndTime());
            electricityTimeSection.setPeriods( h.getPeriods() );
            electricityTimeSection.setState(BoolLogic.NO.getCode());
            electricityTimeSection.setCreateTime(DateUtil.date());
            electricityTimeSection.setCompare( compare );
            electricityTimeSection.setTemperature(Temperature );
            electricityTimeSectionList.add(electricityTimeSection);
        } );
        electricityTimeSectionCustomMapper.batchInsertTimeSection( electricityTimeSectionList );

    }





    /**
     * 插入seasonList
     * @param addDateList
     * @param seasonName
     * @param seasonSectionId
     * @param structureId
     * @param structureRuleId
     */
    public void batchCreateSeasonDate(List<SeasonDateBO> addDateList, String seasonName, String seasonSectionId, String structureId, String structureRuleId){
        if(CollectionUtils.isNotEmpty( addDateList )){
            addDateList.forEach( t->{
                ElectricitySeasonSection seasonSection = new ElectricitySeasonSection();
                seasonSection.setSeasonSectionName( seasonName );
                seasonSection.setSeasonSectionId( seasonSectionId);
                seasonSection.setCreateTime( DateUtil.date() );
                seasonSection.setStructureId( structureId );
                seasonSection.setStructureRuleId( structureRuleId );
                seasonSection.setState( BoolLogic.NO.getCode() );
                seasonSection.setSeaEndDate( t.getSeaEndDate() );
                seasonSection.setSeaStartDate( t.getSeaStartDate() );
                seasonSectionMapper.insert( seasonSection );
            } );
        }
    }

    /**
     * 批量更新季节日期列表
     * @param updateDateList
     */
    public void batchUpdateSeasonDate(List<SeasonDateBO> updateDateList){
        if(CollectionUtils.isNotEmpty( updateDateList )){
            ArrayList<Object> list = new ArrayList<>();
            updateDateList.forEach( t->{
                HashMap<String, Object> map = new HashMap<>();
                map.put( "id", t.getSeasonId());
                map.put( "seaStartDate",t.getSeaStartDate() );
                map.put( "seaEndDate",t.getSeaEndDate() );
                map.put( "updateTime",DateUtil.date() );
                list.add( map );
            } );
            electricityPriceSeasonSectionCustomMapper.batchUpdateSeason( list );
        }
    }

    /**
     * 根据三要素匹配体系规则
     * @param industry
     * @param strategy
     * @param voltageLevel
     * @param id
     * @return
     */
    private String queryStructureRuleId(String industry, String strategy, String voltageLevel, String id) {
        Map<String,Object> map=new HashMap<>();
        map.put( "state", BoolLogic.NO.getCode());
        map.put( "structureId",id );
        List<ElectricityPriceStructureRule> priceStructureRuleList = electricityPriceStructureRuleCustomMapper.queryElectricityPriceRulesByCondition( map );
        ElectricityPriceStructureRule electricityPriceStructureRule = priceStructureRuleList.stream().filter( t -> {
            boolean a = Arrays.asList( t.getStrategies().split( "," ) ).contains( strategy );
            boolean b = Arrays.asList( t.getIndustries().split( "," ) ).contains( industry );
            boolean c = Arrays.asList( t.getVoltageLevels().split( "," ) ).contains( voltageLevel );
            if (a && b && c) {
                return true;
            }
            return false;
        } ).collect( Collectors.toList() ).stream().findFirst().orElse( null );
        if(Optional.ofNullable(electricityPriceStructureRule).isPresent()){
            return electricityPriceStructureRule.getStructureRuleId();
        }
        return null;
    }


    /**
     * 删除体系
     * @param structureId 体系id
     */
    private void deleteStructure(String structureId) {
        //根据体系id删除体系规则,季节分时,分时区间,规则,价格
        ElectricityPriceStructure electricityPriceStructure = new ElectricityPriceStructure();
        electricityPriceStructure.setStructureId( structureId );
        electricityPriceStructure.setState( BoolLogic.YES.getCode() );
        electricityPriceStructure.setUpdator( tenantId );
        electricityPriceStructure.setUpdateTime( DateUtil.date() );
        electricityPriceStructureCustomMapper.updateByStructureId( electricityPriceStructure );
        //根据体系id查询体系规则,根据体系规则ids删除体系规则
        HashMap<String, Object> structureRuleQueryMap = new HashMap<>();
        structureRuleQueryMap.put( "structureId", structureId);
        structureRuleQueryMap.put( "state", BoolLogic.NO.getCode());
        List<ElectricityPriceStructureRule> electricityPriceStructureRules = electricityPriceStructureRuleCustomMapper.queryElectricityPriceRulesByCondition( structureRuleQueryMap);
        List<String> electricityPriceStructureRuleIds = electricityPriceStructureRules.stream().map( ElectricityPriceStructureRule::getStructureRuleId ).collect( Collectors.toList() );
        if(CollectionUtils.isNotEmpty( electricityPriceStructureRuleIds )){
            deleteStructureRuleByStructureRuleIds(electricityPriceStructureRuleIds);
        }
        //批量删除体系规则下对应的规则与价格(根据体系id)
        List<ElectricityPriceRule> electricityPriceRules = electricityPriceRuleCustomMapper.queryRuleListByStructureId( structureId );
        List<String> electricityPriceRuleIds = electricityPriceRules.stream().map( ElectricityPriceRule::getRuleId ).collect( Collectors.toList() );
        if(CollectionUtils.isNotEmpty(electricityPriceRuleIds)){
            batchDeleteRulePriceAndEquipment(electricityPriceRuleIds);
        }
    }

    /**
     * 根据体系规则id删除体系规则以及对应的季节分时,分时区间
     */
    public void deleteStructureRuleByStructureRuleIds(List<String> electricityPriceStructureRuleIds){
        //根据体系规则id删除体系规则
        HashMap<String, Object> map = new HashMap<>();
        map.put( "ids", electricityPriceStructureRuleIds );
        map.put("state", BoolLogic.YES.getCode() );
        map.put( "updateTime",DateUtil.date() );
        electricityPriceStructureRuleCustomMapper.batchDeleteStructureRuleByIds(map);
        //根据体系规则id查询出体系规则下对应的季节id ,根据季节id删除季节与分时信息
        HashMap<String, Object> seasonQueryMap = new HashMap<>();
        seasonQueryMap.put( "structureRuleIds",electricityPriceStructureRuleIds );
        seasonQueryMap.put( "state", BoolLogic.NO.getCode());
        List<ElectricitySeasonSection> electricitySeasonSections = electricityPriceSeasonSectionCustomMapper.querySeasonSectionIdsByStructureRuleIds( seasonQueryMap );
        //多个季节区间公用一个季节区间id,需要去重,得到体系规则下的多个季节列表
        Set<String> seasonSectionIds = electricitySeasonSections.stream().map( ElectricitySeasonSection::getSeasonSectionId ).collect( Collectors.toSet() );
        batchDeleteSeasonAndTime(seasonSectionIds);

    }

    /**
     * 删除季节分时与分时区间
     */
    public void batchDeleteSeasonAndTime(Set<String> seasonSectionIds){
        //根据季节sectionId删除季节与分时信息
        Map<String, Object> map = new HashMap<>();
        map.put( "seasonSectionIds",seasonSectionIds );
        map.put("state", BoolLogic.YES.getCode() );
        map.put( "updateTime",DateUtil.date() );
        electricityPriceSeasonSectionCustomMapper.batchDeleteSeasonSectionBySectionIds(map );
        electricityTimeSectionCustomMapper.batchDeleteTimeSectionBySeasonSectionIds(map);
    }

    /**
     * 批量删除规则与价格,与设备绑定关系
     */
    public void batchDeleteRulePriceAndEquipment(List<String> electricityPriceRuleIds){
        Map<String,Object> map=new HashMap<>();
        map.put( "ruleIds", electricityPriceRuleIds);
        map.put( "updateTime",DateUtil.date() );
        map.put( "state",BoolLogic.YES.getCode());
        electricityPriceRuleCustomMapper.bacthDeletePriceRuleByRuleIds( map );
        electricityPriceCustomMapper.batchDeletePriceByRuleIds( map );
        electricityPriceEquipmentCustomMapper.batchDeleteEquipmentBindingByConditions( map );
    }



    /**
     * 创建体系
     */
    public String createStructure(ElectricityPriceStructureUpdateBO electricityPriceStructureUpdateBO,String versionId){
        ElectricityPriceStructure electricityPriceStructure = ElectricityPriceVersionUpdateBOConverMapper.INSTANCE.electricityPriceStructureBOTOPO( electricityPriceStructureUpdateBO );
        String structureId = String.valueOf( SnowFlake.getInstance().nextId() );
        electricityPriceStructure.setStructureId(structureId);
        electricityPriceStructure.setState(BoolLogic.NO.getCode());
        electricityPriceStructure.setVersionId(versionId);
        electricityPriceStructure.setCreateTime(DateUtil.date());
        electricityPriceStructure.setCreator( tenantId );
        electricityPriceStructureMapper.insert(electricityPriceStructure);
        String[] defStructureRuleId = {null};
        //创建体系规则 季节分时与分时区间
        electricityPriceStructureUpdateBO.getElectricityPriceStructureRuleUpdateBOList().forEach( t->{
            //创建体系规则
            ElectricityPriceStructureRule electricityPriceStructureRule = ElectricityPriceVersionUpdateBOConverMapper.INSTANCE.electricityPriceStructureRuleBOTOPO( t );
            electricityPriceStructureRule.setStructureId( structureId );
            String structureRuleId = String.valueOf( SnowFlake.getInstance().nextId() );
            electricityPriceStructureRule.setStructureRuleId( structureRuleId );
            electricityPriceStructureRule.setState(BoolLogic.NO.getCode());
            electricityPriceStructureRule.setCreateTime(DateUtil.date());
            electricityPriceStructureRuleCustomMapper.insertElectricityPriceStructureRule(electricityPriceStructureRule);
            //默认体系规则
            if(electricityPriceStructureRule.getStrategies().equals( CommonConstant.DEFAULT_TYPE )
                    && electricityPriceStructureRule.getVoltageLevels().equals( CommonConstant.DEFAULT_TYPE ) && electricityPriceStructureRule.getIndustries().equals( CommonConstant.DEFAULT_TYPE )){
                defStructureRuleId[0]=electricityPriceStructureRule.getStructureRuleId();
            }

            //Long structureRuleAutoId=electricityPriceStructureRule.getId();
            //创建季节分时与分时区间
            t.getElectricityPriceSeasonUpdateReqVOList().forEach( season->{
                createSeasonsAndTimes(season,structureId,structureRuleId);
            } );
            //判断规则属于哪个体系规则下 ->最后会判断出三要素属于哪个体系规则,如果匹配不上的属于默认体系规则
            electricityPriceStructureUpdateBO.getElectricityPriceUpdateBOList().forEach( p->{
                String industry = p.getIndustry();
                String strategy = p.getStrategy();
                String voltageLevel = p.getVoltageLevel();
                List<String> industryList = Arrays.asList( electricityPriceStructureRule.getIndustries().split( "," ) );
                List<String> strategyList = Arrays.asList( electricityPriceStructureRule.getStrategies().split( "," ) );
                List<String> voltageLevelList = Arrays.asList( electricityPriceStructureRule.getVoltageLevels().split( "," ) );
                //为了区分默认体系规则跟非默认的
                if(industryList.stream().anyMatch(i->i.equals( industry ) ) && strategyList.stream().anyMatch(s->s.equals(strategy))
                        && voltageLevelList.stream().anyMatch( v->v.equals( voltageLevel ) )){
                    p.setStructureRuleId( electricityPriceStructureRule.getStructureRuleId());
                }
            } );
        } );

        //创建规则与价格
        electricityPriceStructureUpdateBO.getElectricityPriceUpdateBOList().forEach( p->{
            String belongStructureRuleId=defStructureRuleId[0];
            if(Optional.ofNullable(p.getStructureRuleId()).isPresent()){
                belongStructureRuleId=p.getStructureRuleId();
            }
            createPriceAndRule(p,versionId,structureId,belongStructureRuleId);
        } );

        return electricityPriceStructure.getStructureId();
    }

    /**
     * 创建体系规则跟季节
     * @param electricityPriceStructureRuleUpdateBOList
     * @param structureId
     */
    public void createStructureRuleAndSeason(List<ElectricityPriceStructureRuleUpdateBO> electricityPriceStructureRuleUpdateBOList,String structureId){
        //创建体系规则
        electricityPriceStructureRuleUpdateBOList.forEach( t->{
            ElectricityPriceStructureRule electricityPriceStructureRule = ElectricityPriceVersionUpdateBOConverMapper.INSTANCE.electricityPriceStructureRuleBOTOPO( t );
            electricityPriceStructureRule.setStructureId( structureId );
            String structureRuleId = String.valueOf( SnowFlake.getInstance().nextId() );
            electricityPriceStructureRule.setStructureRuleId( structureRuleId );
            electricityPriceStructureRule.setState(BoolLogic.NO.getCode());
            electricityPriceStructureRule.setCreateTime(DateUtil.date());
            electricityPriceStructureRuleCustomMapper.insertElectricityPriceStructureRule(electricityPriceStructureRule);
            //创建季节分时与分时区间
            t.getElectricityPriceSeasonUpdateReqVOList().forEach( season->{
                createSeasonsAndTimes(season,structureId,structureRuleId);
            } );
        } );
    }




    /**
     * 创建规则与价格
     * @param p
     * @param versionId
     * @param structureId
     * @param structureRuleId
     */
    private void createPriceAndRule(ElectricityPriceUpdateBO p, String versionId, String structureId, String structureRuleId) {
        ElectricityPriceRule electricityPriceRule = ElectricityPriceVersionUpdateBOConverMapper.INSTANCE.electricityPriceRuleBOTOPO( p );
        String ruleId = String.valueOf(SnowFlake.getInstance().nextId());
        electricityPriceRule.setRuleId( ruleId );
        electricityPriceRule.setVersionId( versionId );
        electricityPriceRule.setStructureId(structureId);
        electricityPriceRule.setStructureRuleId( structureRuleId );
        electricityPriceRule.setState(Integer.valueOf(BoolLogic.NO.getCode()));
        electricityPriceRule.setCreateTime(DateUtil.date());
        electricityPriceRule.setTenantId( tenantId );
        electricityPriceRule.setTenantName( tenantName );
        priceRuleMapper.insert(electricityPriceRule );

        ElectricityPrice electricityPrice = ElectricityPriceVersionUpdateBOConverMapper.INSTANCE.electricityPriceBOTOPO( p );
        String detailId = String.valueOf(SnowFlake.getInstance().nextId());
        electricityPrice.setDetailId( detailId);
        electricityPrice.setRuleId( ruleId );
        electricityPrice.setTenantId( tenantId );
        electricityPrice.setTenantName( tenantName );
        electricityPrice.setState(Integer.valueOf(BoolLogic.NO.getCode()));
        electricityPrice.setCreateTime(DateUtil.date());
        priceMapper.insert( electricityPrice);
    }

    /**
     * 创建季节分时与分时区间,以及体系规则
     */
    public void createSeasonsAndTimes(ElectricityPriceSeasonUpdateBO electricityPriceSeasonUpdateBO,String structureId,String structureRuleId){
        //根据季节区间创建季节区间表,同一个季节公用一个季节区间id
        String seasonSectionId = String.valueOf(SnowFlake.getInstance().nextId());
        electricityPriceSeasonUpdateBO.getSeasonDateList().forEach( t->{
            ElectricitySeasonSection electricitySeasonSection=new ElectricitySeasonSection();
            electricitySeasonSection.setStructureId( structureId );
            electricitySeasonSection.setStructureRuleId( structureRuleId );
            electricitySeasonSection.setSeasonSectionId(seasonSectionId );
            electricitySeasonSection.setSeasonSectionName( electricityPriceSeasonUpdateBO.getSeasonName() );
            electricitySeasonSection.setSeaStartDate( t.getSeaStartDate() );
            electricitySeasonSection.setSeaEndDate( t.getSeaEndDate() );
            electricitySeasonSection.setState(BoolLogic.NO.getCode());
            electricitySeasonSection.setCreateTime(DateUtil.date());
            seasonSectionMapper.insert(electricitySeasonSection);
        } );
        //根据策略维度插入time
        electricityPriceSeasonUpdateBO.getElectricityPriceStrategyBOList().forEach( t->{
            batchCreateTime(t.getElectricityTimeSectionUpdateBOList(),seasonSectionId, t.getCompare(), t.getTemperature());
        } );

    }
}