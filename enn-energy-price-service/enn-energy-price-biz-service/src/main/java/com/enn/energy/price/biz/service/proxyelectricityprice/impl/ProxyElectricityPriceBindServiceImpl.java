package com.enn.energy.price.biz.service.proxyelectricityprice.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.enn.energy.price.biz.service.bo.proxyprice.*;
import com.enn.energy.price.biz.service.convertMapper.ElectricityPriceVersionUpdateBOConverMapper;
import com.enn.energy.price.biz.service.proxyelectricityprice.ProxyElectricityPriceBindService;
import com.enn.energy.price.common.enums.*;
import com.enn.energy.price.common.error.ErrorCodeEnum;
import com.enn.energy.price.common.error.PriceException;
import com.enn.energy.price.dal.mapper.ext.ElectricityPriceRuleExtMapper;
import com.enn.energy.price.dal.mapper.ext.proxyprice.*;
import com.enn.energy.price.dal.mapper.mbg.ElectricityPriceEquipmentMapper;
import com.enn.energy.price.dal.mapper.mbg.SystemTreeMapper;
import com.enn.energy.price.dal.po.ext.*;
import com.enn.energy.price.dal.po.mbg.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @description:
 * @author:quyl
 * @createTime:2022/5/4 22:18
 */
@Service
@Slf4j
public class ProxyElectricityPriceBindServiceImpl implements ProxyElectricityPriceBindService {

    @Resource
    private ElectricityPriceVersionCustomMapper electricityPriceVersionCustomMapper;

    @Resource
    private ElectricityPriceStructureCustomMapper electricityPriceStructureCustomMapper;

    @Resource
    private ElectricityPriceRuleExtMapper electricityPriceRuleExtMapper;

    @Resource
    private ElectricityPriceCustomMapper electricityPriceCustomMapper;

    @Resource
    private ElectricityPriceEquipmentCustomMapper electricityPriceEquipmentCustomMapper;

    @Resource
    private SystemTreeMapper systemTreeMapper;

    @Resource
    private SystemTreeCustomerMapper systemTreeCustomerMapper;

    @Resource
    private ElectricityPriceEquipmentMapper electricityPriceEquipmentMapper;

    /**
     * 绑定电价（新增绑定和修改绑定）
     *
     * @param electricityPriceBindBO
     */
    @Override
    public void bindProxyElectricityPrice(ElectricityPriceBindBO electricityPriceBindBO) {
        //校验节点是否存在
        SystemTree systemTree = systemTreeMapper.selectByPrimaryKey(Long.valueOf(electricityPriceBindBO.getNodeId()));
        if (ObjectUtil.isEmpty(systemTree)) {
            throw new PriceException(ErrorCodeEnum.PROXY_ELECTRICITY_PRICE_BIND_SYSTEM_TREE_NOT_EXIST.getErrorCode(), ErrorCodeEnum.PROXY_ELECTRICITY_PRICE_BIND_SYSTEM_TREE_NOT_EXIST.getErrorMsg());
        }
        //获取版本信息
        ElectricityPriceVersion electricityPriceVersion = electricityPriceVersionCustomMapper.selectElectricityPriceVersionByVersionId(electricityPriceBindBO.getVersionId());
        if (ObjectUtil.isEmpty(electricityPriceVersion)) {
            throw new PriceException(ErrorCodeEnum.PROXY_ELECTRICITY_PRICE_BIND_PRICE_VERSION_NOT_EXIST.getErrorCode(), ErrorCodeEnum.PROXY_ELECTRICITY_PRICE_BIND_SYSTEM_TREE_NOT_EXIST.getErrorMsg());
        }
        //体系是否合法
        ElectricityPriceStructure electricityPriceStructure = electricityPriceStructureCustomMapper.selectElectricityPriceStructureByParams(new HashMap<String, Object>() {{
            put("structureId", electricityPriceBindBO.getStructureId());
        }});
        if (ObjectUtil.isEmpty(electricityPriceStructure)) {
            throw new PriceException(ErrorCodeEnum.PROXY_ELECTRICITY_PRICE_BIND_PRICE_STRUCT_NOT_EXIST.getErrorCode(), ErrorCodeEnum.PROXY_ELECTRICITY_PRICE_BIND_SYSTEM_TREE_NOT_EXIST.getErrorMsg());
        }
        //查询价格
        List<ElectricityPriceRule> electricityPriceRules = electricityPriceRuleExtMapper.selectRuleByCondition(new HashMap<String, Object>() {{
            put("ruleId", electricityPriceBindBO.getRuleId());
        }});
        if (CollUtil.isEmpty(electricityPriceRules)) {
            throw new PriceException(ErrorCodeEnum.PROXY_ELECTRICITY_PRICE_BIND_PRICE_STRUCT_RULE_NOT_EXIST.getErrorCode(), ErrorCodeEnum.PROXY_ELECTRICITY_PRICE_BIND_SYSTEM_TREE_NOT_EXIST.getErrorMsg());
        }
        //构建绑定关系
        ElectricityPriceEquipment electricityPriceEquipment = buildPriceEquipment(electricityPriceBindBO, systemTree.getNodeName(), electricityPriceRules.get(0).getStructureRuleId());
        if (ChangeTypeEum.ADD.getType().equals(electricityPriceBindBO.getChangeType())) {
            electricityPriceEquipment.setId(null);
            //TODO 保存企业所属地区 electricityPriceEquipment.set
            addElectricityPriceEquipmentBind(electricityPriceBindBO, electricityPriceVersion, electricityPriceEquipment);
        }
        //修改绑定关系
        if (ChangeTypeEum.UPDATE.getType().equals(electricityPriceBindBO.getChangeType())) {
            updateElectricityPriceEquipmentBind(electricityPriceBindBO, electricityPriceVersion, electricityPriceEquipment);
        }
    }

    /**
     * 解绑电价规则
     *
     * @param electricityPriceBindRemoveBO
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void unBoundPriceRule(ElectricityPriceBindRemoveBO electricityPriceBindRemoveBO) {
        updateElectricityPriceEquipment(electricityPriceBindRemoveBO.getId());
        if (BoolLogic.YES.getCode().equals(electricityPriceBindRemoveBO.getNextChangeFlag())) {
            updateElectricityPriceEquipment(electricityPriceBindRemoveBO.getNextId());
        }
    }

    @Override
    public ElectricityPriceVersionsByBindAreaBO queryElectricityPriceVersionByBindArea(ElectricityPriceBindVersionsBO electricityPriceBindVersionsBO) {
        //根据省进行查询有效版本,若无则暂无数据
        List<ElectricityPriceVersion> electricityPriceVersionsToProvince = electricityPriceVersionCustomMapper.queryPriceVersionByCondition(new HashMap<String, Object>() {{
            put("provinceCode", electricityPriceBindVersionsBO.getProvinceCode());
            put("isCurEffectFlag", BoolLogic.YES.getCode());
        }});
        if (CollUtil.isEmpty(electricityPriceVersionsToProvince)) {
            return ElectricityPriceVersionsByBindAreaBO.builder().provinceCode(null).cityCode(null).districtCode(null).electricityPriceVersionBOS(null).build();
        }
        List<String> versionIds = electricityPriceVersionsToProvince.stream().map(item -> item.getVersionId()).collect(Collectors.toList());
        List<ElectricityPriceStructure> electricityPriceStructures = electricityPriceStructureCustomMapper.queryListByConditions(new HashMap<String, Object>() {{
            put("versionIds", versionIds);
            put("state", BoolLogic.NO.getCode());
        }});
        if (CollUtil.isEmpty(electricityPriceStructures)) {
            return ElectricityPriceVersionsByBindAreaBO.builder().provinceCode(electricityPriceBindVersionsBO.getProvinceCode()).cityCode(null).districtCode(null).electricityPriceVersionBOS(null).build();
        }
        //查看当前区下是否有版本，若有则版本+体系精确到区
        List<ElectricityPriceStructure> districtStructures = electricityPriceStructures.stream().filter(item -> item.getDistrictCodes().contains(electricityPriceBindVersionsBO.getDistrictCode())).collect(Collectors.toList());
        if (CollUtil.isNotEmpty(districtStructures)) {
            Map<String, List<ElectricityPriceStructure>> districtVersionStructureMap = districtStructures.stream().collect(Collectors.groupingBy(ElectricityPriceStructure::getVersionId));
            List<ElectricityPriceVersionStructureBO> electricityPriceVersionStructureBOS = buildPriceVersions(districtVersionStructureMap, electricityPriceVersionsToProvince);
            return ElectricityPriceVersionsByBindAreaBO.builder().provinceCode(electricityPriceBindVersionsBO.getProvinceCode()).cityCode(electricityPriceBindVersionsBO.getCityCode()).districtCode(electricityPriceBindVersionsBO.getDistrictCode()).electricityPriceVersionBOS(electricityPriceVersionStructureBOS).build();
        }
        //根据省市进行查询有效版本
        List<ElectricityPriceStructure> cityStructures = electricityPriceStructures.stream().filter(item -> item.getCityCodes().contains(electricityPriceBindVersionsBO.getCityCode())).collect(Collectors.toList());
        if (CollUtil.isNotEmpty(cityStructures)) {
            Map<String, List<ElectricityPriceStructure>> cityVersionStructureMap = cityStructures.stream().collect(Collectors.groupingBy(ElectricityPriceStructure::getVersionId));
            List<ElectricityPriceVersionStructureBO> electricityPriceVersionStructureBOS = buildPriceVersions(cityVersionStructureMap, electricityPriceVersionsToProvince);
            return ElectricityPriceVersionsByBindAreaBO.builder().provinceCode(electricityPriceBindVersionsBO.getProvinceCode()).cityCode(electricityPriceBindVersionsBO.getCityCode()).districtCode(null).electricityPriceVersionBOS(electricityPriceVersionStructureBOS).build();
        }
        //默认省级
        Map<String, List<ElectricityPriceStructure>> provinceVersionStructureMap = electricityPriceStructures.stream().collect(Collectors.groupingBy(ElectricityPriceStructure::getVersionId));
        List<ElectricityPriceVersionStructureBO> electricityPriceVersionStructureBOS = buildPriceVersions(provinceVersionStructureMap, electricityPriceVersionsToProvince);
        return ElectricityPriceVersionsByBindAreaBO.builder().provinceCode(electricityPriceBindVersionsBO.getProvinceCode()).cityCode(null).districtCode(null).electricityPriceVersionBOS(electricityPriceVersionStructureBOS).build();
    }

    private List<ElectricityPriceVersionStructureBO> buildPriceVersions(Map<String, List<ElectricityPriceStructure>> versionStructureMap, List<ElectricityPriceVersion> electricityPriceVersionsToProvince) {
        List<ElectricityPriceVersionStructureBO> electricityPriceVersionBOS = electricityPriceVersionsToProvince.stream().filter(item -> ObjectUtil.isNotEmpty(versionStructureMap.get(item.getVersionId())))
                .map(bo -> {
                    ElectricityPriceVersionStructureBO electricityPriceVersionStructureBO = new ElectricityPriceVersionStructureBO();
                    electricityPriceVersionStructureBO.setVersionId(bo.getVersionId());
                    electricityPriceVersionStructureBO.setVersionName(bo.getVersionName());
                    electricityPriceVersionStructureBO.setElectricityPriceStructureBOS(ElectricityPriceVersionUpdateBOConverMapper.INSTANCE.electricityPriceStructurePOListToBOList(versionStructureMap.get(bo.getVersionId())));
                    return electricityPriceVersionStructureBO;
                }).collect(Collectors.toList());
        return electricityPriceVersionBOS;
    }

    @Override
    public ElectricityPriceBindNodeStatusBO getNodeBindPriceStatusByEntId(String entId) {
        //根据企业id获取其下面根节点列表
        List<SystemTree> systemTreeList = systemTreeCustomerMapper.getSystemTreeListByMap(new HashMap<String, Object>() {{
            put("parentId", entId);
            put("nodeType", NodeTypeEum.ENTERPRISSE.getType());
            put("state", BoolLogic.NO.getCode().intValue());
        }});
        if (CollUtil.isEmpty(systemTreeList)) {
            return ElectricityPriceBindNodeStatusBO.builder().nodeBindStatusList(null).build();
        }
        List<Long> nodeIds = systemTreeList.stream().map(item -> item.getNodeId()).collect(Collectors.toList());
        //根据节点ids获取已绑定的
        List<ElectricityPriceEquipment> electricityPriceEquipments = electricityPriceEquipmentCustomMapper.getElectricityPriceEquipmentByNodeIds(nodeIds);
        Map<String, ElectricityPriceEquipment> electricityPriceEquipmentMap = electricityPriceEquipments.stream().collect(Collectors.toMap(ElectricityPriceEquipment::getEquipmentId, item -> item));
        //根据绑定的价格ruleIds查找，如果未找到对应规则则失效（按当前设计，不可能出现失效除非脏数据）
        List<String> ruleIds = electricityPriceEquipments.stream().map(item -> item.getRuleId()).collect(Collectors.toList());
        List<ElectricityPriceEquipment> electricityPriceEuipmentNotExistRule = electricityPriceEquipmentCustomMapper.getElectricityPriceEquipmentNotExistRule(ruleIds);
        Map<String, ElectricityPriceEquipment> electricityPriceEuipmentNotExistRuleMap = electricityPriceEuipmentNotExistRule.stream().collect(Collectors.toMap(ElectricityPriceEquipment::getEquipmentId, item -> item));
        List<ElectricityPriceBindNodeStatusBO.NodeBindStatus> collect = systemTreeList.stream().map(item -> {
            ElectricityPriceBindNodeStatusBO.NodeBindStatus nodeBindStatus = new ElectricityPriceBindNodeStatusBO.NodeBindStatus();
            nodeBindStatus.setNodeId(item.getNodeId().toString());
            nodeBindStatus.setNodeName(item.getNodeName());
            nodeBindStatus.setStatus(BindStatusEum.UNBOUND.getName());
            if (ObjectUtil.isNotEmpty(electricityPriceEuipmentNotExistRuleMap.get(item.getNodeId()))) {
                nodeBindStatus.setStatus(BindStatusEum.INVALID.getName());
                return nodeBindStatus;
            }
            if (ObjectUtil.isNotEmpty(electricityPriceEquipmentMap.get(item.getNodeId()))) {
                nodeBindStatus.setStatus(BindStatusEum.BIND.getName());
            }
            return nodeBindStatus;

        }).collect(Collectors.toList());
        return ElectricityPriceBindNodeStatusBO.builder().nodeBindStatusList(collect).build();
    }

    @Override
    public ElectricityPriceBindDetailBO getPriceBindDetail(Long nodeId) {
        //根据节点id查找当前时间段的绑定关系详情
        List<ElectricityPriceEquipment> electricityPriceEquipmentByNodeIds = electricityPriceEquipmentCustomMapper.getElectricityPriceEquipmentByNodeIds(Arrays.asList(nodeId));
        //根据绑定详情查找版本
        if (CollUtil.isEmpty(electricityPriceEquipmentByNodeIds) || ObjectUtil.isEmpty(electricityPriceEquipmentByNodeIds.get(0))) {
            return null;
        }
        ElectricityPriceEquipment electricityPriceEquipment = electricityPriceEquipmentByNodeIds.get(0);
        ElectricityPriceVersion electricityPriceVersion = electricityPriceVersionCustomMapper.selectElectricityPriceVersionByVersionId(electricityPriceEquipment.getVersionId());
        if (ObjectUtil.isEmpty(electricityPriceVersion)) {
            return null;
        }
        ElectricityPriceBindDetailBO electricityPriceBindDetailBO = new ElectricityPriceBindDetailBO();
        electricityPriceBindDetailBO.setId(electricityPriceEquipment.getId());
        electricityPriceBindDetailBO.setStructureId(electricityPriceEquipment.getStructureId());
        electricityPriceBindDetailBO.setNextChangeFlag(electricityPriceEquipment.getNextChangeFlag().toString());
        electricityPriceBindDetailBO.setVersionId(electricityPriceVersion.getVersionId());
        electricityPriceBindDetailBO.setVersionName(electricityPriceVersion.getVersionName());
        electricityPriceBindDetailBO.setStartDate(DateUtil.format(electricityPriceVersion.getStartDate(), DatePattern.NORM_DATE_PATTERN));
        electricityPriceBindDetailBO.setEndDate(DateUtil.format(electricityPriceVersion.getEndDate(), DatePattern.NORM_DATE_PATTERN));
        //根据绑定详情查找体系、规则、价格信息
        ElectricityPriceStructure electricityPriceStructure = electricityPriceStructureCustomMapper.selectElectricityPriceStructureByParams(new HashMap<String, Object>() {{
            put("structureId", electricityPriceEquipment.getStructureId());
            put("versionId", electricityPriceEquipment.getVersionId());
        }});
        ElectricityPriceDetailPO electricityPriceDetailPO = electricityPriceCustomMapper.getPriceDetailByRuleId(electricityPriceEquipment.getRuleId());
        if (ObjectUtil.isEmpty(electricityPriceStructure) || ObjectUtil.isEmpty(electricityPriceDetailPO)) {
            electricityPriceBindDetailBO.setBindStatus(BindStatusEum.INVALID.getName());
            return electricityPriceBindDetailBO;
        }
        ElectricityPriceDetailBO electricityPriceDetailBO = ElectricityPriceVersionUpdateBOConverMapper.INSTANCE.electricityPriceDetailPOToBO(electricityPriceDetailPO);
        electricityPriceBindDetailBO.setStructureName(electricityPriceStructure.getStructureName());
        electricityPriceBindDetailBO.setElectricityPriceDetailBO(electricityPriceDetailBO);
        //如果是继承则直接返回，
        if (BoolLogic.YES.getCode().equals(electricityPriceEquipment.getNextChangeFlag())) {
            //查找下一版本
            ElectricityPriceVersion nextVersion = electricityPriceVersionCustomMapper.selectNextElectricityPriceVersion(electricityPriceVersion);
            if (ObjectUtil.isEmpty(nextVersion)) {
                electricityPriceBindDetailBO.setNextVersionPriceBindBO(null);
                return electricityPriceBindDetailBO;
            }
            List<ElectricityPriceEquipment> nextElectricityPriceEquipments = electricityPriceEquipmentCustomMapper.queryElectricityPriceEquipmentByMap(new HashMap<String, Object>() {{
                put("versionId", nextVersion.getVersionId());
                put("equipmentId", nodeId);
            }});
            if (CollUtil.isEmpty(nextElectricityPriceEquipments)) {
                electricityPriceBindDetailBO.setNextVersionPriceBindBO(null);
                return electricityPriceBindDetailBO;
            }
            ElectricityPriceEquipment nextPriceEquipment = nextElectricityPriceEquipments.get(0);
            ElectricityPriceBindDetailBO.NextVersionPriceBindBO nextVersionPriceBindBO = new ElectricityPriceBindDetailBO.NextVersionPriceBindBO();
            nextVersionPriceBindBO.setId(nextPriceEquipment.getId());
            nextVersionPriceBindBO.setStructureId(nextPriceEquipment.getStructureId());
            nextVersionPriceBindBO.setVersionId(nextVersion.getVersionId());
            nextVersionPriceBindBO.setVersionName(nextVersion.getVersionName());
            nextVersionPriceBindBO.setStartDate(DateUtil.format(nextVersion.getStartDate(), DatePattern.NORM_DATE_PATTERN));
            nextVersionPriceBindBO.setEndDate(DateUtil.format(nextVersion.getEndDate(), DatePattern.NORM_DATE_PATTERN));
            //根据绑定详情查找体系、规则、价格信息
            ElectricityPriceStructure nextPriceStructure = electricityPriceStructureCustomMapper.selectElectricityPriceStructureByParams(new HashMap<String, Object>() {{
                put("structureId", nextPriceEquipment.getStructureId());
                put("versionId", nextPriceEquipment.getVersionId());
            }});
            ElectricityPriceDetailPO nextPriceDetailPO = electricityPriceCustomMapper.getPriceDetailByRuleId(nextPriceEquipment.getRuleId());
            if (ObjectUtil.isEmpty(electricityPriceStructure) || ObjectUtil.isEmpty(electricityPriceDetailPO)) {
                electricityPriceBindDetailBO.setNextVersionPriceBindBO(nextVersionPriceBindBO);
                return electricityPriceBindDetailBO;
            }
            ElectricityPriceDetailBO nextPriceDetailBO = ElectricityPriceVersionUpdateBOConverMapper.INSTANCE.electricityPriceDetailPOToBO(nextPriceDetailPO);
            nextVersionPriceBindBO.setStructureName(nextPriceStructure.getStructureName());
            nextVersionPriceBindBO.setElectricityPriceDetailBO(nextPriceDetailBO);
        }
        return electricityPriceBindDetailBO;
    }

    @Override
    public ElectricityPriceBindEditDetailBO getPriceBindDetailByEdit(ElectricityPriceBindEditBO electricityPriceBindEditBO) {
        ElectricityPriceBindEditDetailBO electricityPriceBindEditDetailBO = new ElectricityPriceBindEditDetailBO();
        electricityPriceBindEditDetailBO.setNextChangeFlag(electricityPriceBindEditBO.getNextChangeFlag());
        //查询当前节点绑定关系是否存在
        ElectricityPriceEquipment electricityPriceEquipment = electricityPriceEquipmentMapper.selectByPrimaryKey(electricityPriceBindEditBO.getId());
        if (ObjectUtil.isEmpty(electricityPriceEquipment)) {
            throw new PriceException(ErrorCodeEnum.PROXY_ELECTRICITY_PRICE_EQUIPMENT_BIND_NOT_FOUND.getErrorCode(), ErrorCodeEnum.PROXY_ELECTRICITY_PRICE_EQUIPMENT_BIND_NOT_FOUND.getErrorMsg());
        }
        //查询当前绑定详情
        ElectricityPriceBindEditDetailItemBO electricityPriceBindEditDetailItemBO = buildBindDetailItem(electricityPriceEquipment, electricityPriceBindEditBO.getId());
        electricityPriceBindEditDetailBO.setElectricityPriceBindEditDetailItemBO(electricityPriceBindEditDetailItemBO);
        electricityPriceBindEditDetailBO.setNodeId(electricityPriceEquipment.getEquipmentId());
//        TODO electricityPriceBindEditDetailBO.setProvinceCode(electricityPriceEquipment.);
        if (BoolLogic.NO.getCode().equals(electricityPriceBindEditBO.getNextChangeFlag())) {
            return electricityPriceBindEditDetailBO;
        }
        //查询下一个版本节点关系
        ElectricityPriceEquipment nextElectricityPriceEquipment = electricityPriceEquipmentMapper.selectByPrimaryKey(electricityPriceBindEditBO.getNextId());
        if (ObjectUtil.isEmpty(electricityPriceEquipment)) {
            throw new PriceException(ErrorCodeEnum.PROXY_ELECTRICITY_PRICE_BIND_NEXT_VERSION_NOT_EXIST.getErrorCode(), ErrorCodeEnum.PROXY_ELECTRICITY_PRICE_BIND_NEXT_VERSION_NOT_EXIST.getErrorMsg());
        }
        ElectricityPriceBindEditDetailItemBO nextElectricityPriceBindEditDetailItemBO = buildBindDetailItem(nextElectricityPriceEquipment, electricityPriceBindEditBO.getNextId());
        electricityPriceBindEditDetailBO.setNextVersionPriceBindDetailItemBO(nextElectricityPriceBindEditDetailItemBO);
        return electricityPriceBindEditDetailBO;
    }

    @Override
    public void doNextVersionPriceEquipmentBind(Date day) {
        //查询当前节点绑定的即将到期的版本
        List<ElectricityPriceEquipmentVersionDto> electricityPriceEquipmentVersionDtos = electricityPriceEquipmentCustomMapper.queryExpireEquipmentVersion(day);
        //获取下个版本
        List<String> provinceCodes = electricityPriceEquipmentVersionDtos.stream().map(ElectricityPriceEquipmentVersionDto::getProvinceCode).distinct().collect(Collectors.toList());
        DateTime nextDay = DateUtil.offsetDay(day, 1);
        List<ElectricityPriceVersionDto> nextVersionDtos = electricityPriceVersionCustomMapper.queryNextPriceVersions(provinceCodes, nextDay);
        Map<String, String> nextVersionMap = nextVersionDtos.stream().collect(Collectors.toMap(ElectricityPriceVersionDto::getProvinceCode, ElectricityPriceVersionDto::getVersionId));
        //获取当前节点绑定有默认继承且存在下个版本的 节点-当前版本-下个版本绑定关系
        List<ElectricityPriceNextVersionDto> nextVersionAndEquipmentList = electricityPriceEquipmentVersionDtos.stream().filter(v -> StrUtil.isNotEmpty(nextVersionMap.get(v.getProvinceCode())))
                .map(item -> {
                    ElectricityPriceNextVersionDto electricityPriceNextVersionDto = new ElectricityPriceNextVersionDto();
                    electricityPriceNextVersionDto.setEquipmentId(item.getEquipmentId());
                    electricityPriceNextVersionDto.setVersionId(item.getVersionId());
                    electricityPriceNextVersionDto.setProvinceCode(item.getProvinceCode());
                    electricityPriceNextVersionDto.setNextVersionId(nextVersionMap.get(item.getProvinceCode()));
                    return electricityPriceNextVersionDto;
                }).collect(Collectors.toList());

        //查询出节点+存在下个版本的数据，过滤未生成绑定关系的下个版本
        List<ElectricityPriceNextVersionDto> bindVersions = electricityPriceEquipmentCustomMapper.queryBindNextVersionPriceEquipment(nextVersionAndEquipmentList);
        Map<String, String> nextVersionEquipmentMap = bindVersions.stream().collect(Collectors.toMap(ElectricityPriceNextVersionDto::getEquipmentId, ElectricityPriceNextVersionDto::getNextVersionId));
        List<ElectricityPriceNextVersionDto> unBindPriceEquipments = nextVersionAndEquipmentList.stream().filter(item -> StrUtil.isEmpty(nextVersionEquipmentMap.get(item.getEquipmentId()))).collect(Collectors.toList());
        //查询这些未绑定价格设备的版本体系和规则
        List<String> curVersionIds = unBindPriceEquipments.stream().map(ElectricityPriceNextVersionDto::getVersionId).distinct().collect(Collectors.toList());
        List<ElectricityPriceVersionRuleDto> electricityPriceVersionRuleDtos = electricityPriceEquipmentCustomMapper.queryVersionStructAndRule(curVersionIds);
        Map<String, ElectricityPriceVersionRuleDto> curVersionStructurePriceMap = electricityPriceVersionRuleDtos.stream().collect(Collectors.toMap(dto -> dto.getVersionId() + dto.getStructureId() + dto.getRuleId(), dto -> dto));
        //未来版本体系价格信息
        List<String> nextVersionIds = unBindPriceEquipments.stream().map(ElectricityPriceNextVersionDto::getNextVersionId).distinct().collect(Collectors.toList());
        List<ElectricityPriceVersionRuleDto> nextElectricityPriceVersionRuleDtos = electricityPriceEquipmentCustomMapper.queryVersionStructAndRule(nextVersionIds);
        Map<String, ElectricityPriceVersionRuleDto> nextVersionStructurePriceMap = nextElectricityPriceVersionRuleDtos.stream().collect(Collectors.toMap(dto -> dto.getVersionId() + dto.getProvinceCode() + dto.getCityCodes() + dto.getDistrictCodes() + dto.getIndustry() + dto.getStrategy() + dto.getVoltageLevel(), dto -> dto));
        //当前价格设备绑定map
        Map<String, ElectricityPriceEquipmentVersionDto> curBindMap = electricityPriceEquipmentVersionDtos.stream().filter(item -> StrUtil.isEmpty(nextVersionEquipmentMap.get(item.getEquipmentId())))
                .collect(Collectors.toMap(dto -> dto.getEquipmentId() + dto.getVersionId(), dto -> dto));
        //构建下个版本绑定关系PO
        List<ElectricityPriceEquipment> electricityPriceEquipments = new ArrayList<>();
        for (ElectricityPriceNextVersionDto dto : unBindPriceEquipments) {
            //获取当前版本设备对应的绑定关系
            ElectricityPriceEquipmentVersionDto curVersionEquipment = curBindMap.get(dto.getEquipmentId() + dto.getVersionId());
            ElectricityPriceVersionRuleDto curStructurePrice = curVersionStructurePriceMap.get(dto.getVersionId() + curVersionEquipment.getStructureId());
            ElectricityPriceVersionRuleDto nextStructurePrice = nextVersionStructurePriceMap.get(dto.getNextVersionId() + curStructurePrice.getProvinceCode() + curStructurePrice.getCityCodes() + curStructurePrice.getDistrictCodes() + curStructurePrice.getIndustry() + curStructurePrice.getStrategy() + curStructurePrice.getVoltageLevel());
            //如果下一版本没有匹配的区域和三要素，则不继承
            if (ObjectUtil.isEmpty(nextStructurePrice)) {
                continue;
            }
            ElectricityPriceEquipment electricityPriceEquipment = new ElectricityPriceEquipment();
            BeanUtil.copyProperties(curVersionEquipment, electricityPriceEquipment);
            electricityPriceEquipment.setRuleId(nextStructurePrice.getRuleId());
            electricityPriceEquipment.setStructureId(nextStructurePrice.getStructureId());
            electricityPriceEquipment.setStructureRuleId(nextStructurePrice.getStructureRuleId());
            electricityPriceEquipment.setVersionId(dto.getNextVersionId());
            electricityPriceEquipments.add(electricityPriceEquipment);
        }
        electricityPriceEquipmentCustomMapper.batchAddElectricityPriceEquipment(electricityPriceEquipments);
    }

    private ElectricityPriceBindEditDetailItemBO buildBindDetailItem(ElectricityPriceEquipment electricityPriceEquipment, Long id) {
        ElectricityPriceVersion electricityPriceVersion = electricityPriceVersionCustomMapper.selectElectricityPriceVersionByVersionId(electricityPriceEquipment.getVersionId());
        if (ObjectUtil.isEmpty(electricityPriceVersion)) {
            throw new PriceException(ErrorCodeEnum.PROXY_ELECTRICITY_PRICE_BIND_PRICE_VERSION_NOT_EXIST.getErrorCode(), ErrorCodeEnum.PROXY_ELECTRICITY_PRICE_BIND_SYSTEM_TREE_NOT_EXIST.getErrorMsg());
        }
        ElectricityPriceBindEditDetailItemBO electricityPriceBindEditDetailItemBO = new ElectricityPriceBindEditDetailItemBO();
        electricityPriceBindEditDetailItemBO.setId(electricityPriceEquipment.getId());
        electricityPriceBindEditDetailItemBO.setRuleId(electricityPriceEquipment.getRuleId());
        electricityPriceBindEditDetailItemBO.setVersionId(electricityPriceEquipment.getVersionId());
        electricityPriceBindEditDetailItemBO.setStructureId(electricityPriceEquipment.getStructureId());
        electricityPriceBindEditDetailItemBO.setStartDate(DateUtil.format(electricityPriceVersion.getStartDate(), DatePattern.NORM_DATE_PATTERN));
        electricityPriceBindEditDetailItemBO.setEndDate(DateUtil.format(electricityPriceVersion.getEndDate(), DatePattern.NORM_DATE_PATTERN));
        return electricityPriceBindEditDetailItemBO;
    }

    /**
     * 根据电价设备绑定id更新状态
     *
     * @param id
     */
    private void updateElectricityPriceEquipment(Long id) {
        ElectricityPriceEquipment electricityPriceEquipment = electricityPriceEquipmentMapper.selectByPrimaryKey(id);
        if (ObjectUtil.isEmpty(electricityPriceEquipment)) {
            throw new PriceException(ErrorCodeEnum.PROXY_ELECTRICITY_PRICE_EQUIPMENT_BIND_NOT_FOUND.getErrorCode(), ErrorCodeEnum.PROXY_ELECTRICITY_PRICE_EQUIPMENT_BIND_NOT_FOUND.getErrorMsg());
        }
        ElectricityPriceEquipment updElectricityPriceEquipment = new ElectricityPriceEquipment();
        updElectricityPriceEquipment.setState(StateEum.DELETE.getValue());
        updElectricityPriceEquipment.setId(id);
        electricityPriceEquipmentMapper.updateByPrimaryKeySelective(updElectricityPriceEquipment);
    }

    /**
     * 新增绑定电价
     *
     * @param electricityPriceBindBO
     * @param electricityPriceVersion
     * @param electricityPriceEquipment
     */
    @Transactional(rollbackFor = Exception.class)
    void addElectricityPriceEquipmentBind(ElectricityPriceBindBO electricityPriceBindBO, ElectricityPriceVersion electricityPriceVersion, ElectricityPriceEquipment electricityPriceEquipment) {
        addElectricityPriceEquipment(electricityPriceEquipment);
        //若下一版本不继承而是自定义，需要设置绑定关系
        if (BoolLogic.YES.getCode().equals(electricityPriceBindBO.getNextChangeFlag())) {
            ElectricityPriceEquipment nextVersionPriceEquipment = buildNextVersionPriceEquipment(electricityPriceVersion, electricityPriceBindBO);
            if (ObjectUtil.isNotEmpty(nextVersionPriceEquipment)) {
                nextVersionPriceEquipment.setEquipmentName(electricityPriceEquipment.getEquipmentName());
                addElectricityPriceEquipment(nextVersionPriceEquipment);
            }
        }
    }

    /**
     * 更新绑定电价
     *
     * @param electricityPriceBindBO
     * @param electricityPriceVersion
     * @param electricityPriceEquipment
     */
    @Transactional(rollbackFor = Exception.class)
    void updateElectricityPriceEquipmentBind(ElectricityPriceBindBO electricityPriceBindBO, ElectricityPriceVersion electricityPriceVersion, ElectricityPriceEquipment electricityPriceEquipment) {
        electricityPriceEquipmentMapper.updateByPrimaryKeySelective(electricityPriceEquipment);
        //若下一版本不继承而是自定义，需要设置绑定关系
        if (BoolLogic.YES.getCode().equals(electricityPriceBindBO.getNextChangeFlag())) {
            ElectricityPriceEquipment nextVersionPriceEquipment = buildNextVersionPriceEquipment(electricityPriceVersion, electricityPriceBindBO);
            if (ObjectUtil.isNotEmpty(nextVersionPriceEquipment)) {
                nextVersionPriceEquipment.setEquipmentName(electricityPriceEquipment.getEquipmentName());
                electricityPriceEquipmentMapper.updateByPrimaryKeySelective(electricityPriceEquipment);
            }
        }
    }

    private void addElectricityPriceEquipment(ElectricityPriceEquipment electricityPriceEquipment) {
        //查询当前版本是否已绑定
        ElectricityPriceEquipment isExistEquipmentBind = electricityPriceEquipmentCustomMapper.queryIsExistEquipmentBind(electricityPriceEquipment);
        if (ObjectUtil.isNotEmpty(isExistEquipmentBind)) {
            throw new PriceException(ErrorCodeEnum.PROXY_ELECTRICITY_PRICE_EQUIPMENT_ALREADY_BIND.getErrorCode(), ErrorCodeEnum.PROXY_ELECTRICITY_PRICE_EQUIPMENT_ALREADY_BIND.getErrorMsg());
        }
        //创建价格规则和节点绑定关系
        electricityPriceEquipmentMapper.insert(electricityPriceEquipment);
    }

    /**
     * 构建下一版本绑定PO
     *
     * @param electricityPriceVersion
     * @param electricityPriceBindBO
     * @return
     */
    private ElectricityPriceEquipment buildNextVersionPriceEquipment(ElectricityPriceVersion electricityPriceVersion, ElectricityPriceBindBO electricityPriceBindBO) {
        //相同区域对应的下个版本
        ElectricityPriceVersion nextElectricityPriceVersion = electricityPriceVersionCustomMapper.selectNextElectricityPriceVersion(electricityPriceVersion);
        if (ObjectUtil.isEmpty(nextElectricityPriceVersion) || !nextElectricityPriceVersion.getVersionId().equals(electricityPriceBindBO.getNextVersionStructurePriceBO().getVersionId())) {
            throw new PriceException(ErrorCodeEnum.PROXY_ELECTRICITY_PRICE_BIND_NEXT_VERSION_NOT_EXIST.getErrorCode(), ErrorCodeEnum.PROXY_ELECTRICITY_PRICE_BIND_NEXT_VERSION_NOT_EXIST.getErrorMsg());
        }
        //下个版本下是否存在对应的体系
        Map<String, Object> nextElectricityPriceStructureMap = new HashMap<>();
        nextElectricityPriceStructureMap.put("versionId", nextElectricityPriceVersion.getVersionId());
        nextElectricityPriceStructureMap.put("state", BoolLogic.NO.getCode());
        nextElectricityPriceStructureMap.put("structureId", electricityPriceBindBO.getNextVersionStructurePriceBO().getStructureId());
        ElectricityPriceStructure nextElectricityPriceStructure = electricityPriceStructureCustomMapper.selectElectricityPriceStructureByParams(nextElectricityPriceStructureMap);
        if (ObjectUtil.isEmpty(nextElectricityPriceStructure)) {
            log.info("未找到体系信息");
            throw new PriceException(ErrorCodeEnum.PROXY_ELECTRICITY_PRICE_BIND_NEXT_VERSION_NOT_EXIST.getErrorCode(), ErrorCodeEnum.PROXY_ELECTRICITY_PRICE_BIND_NEXT_VERSION_NOT_EXIST.getErrorMsg());
        }
        Map<String, Object> mapElectricityPriceRule = new HashMap<>();
        mapElectricityPriceRule.put("versionId", nextElectricityPriceVersion.getVersionId());
        mapElectricityPriceRule.put("structureId", nextElectricityPriceStructure.getStructureId());
        mapElectricityPriceRule.put("state", BoolLogic.NO.getCode());
        mapElectricityPriceRule.put("ruleId", electricityPriceBindBO.getNextVersionStructurePriceBO().getRuleId());
        List<ElectricityPriceRule> electricityPriceRules = electricityPriceRuleExtMapper.selectRuleByCondition(mapElectricityPriceRule);
        if (CollUtil.isEmpty(electricityPriceRules)) {
            log.info("未找到下个版本的电价规则");
            throw new PriceException(ErrorCodeEnum.PROXY_ELECTRICITY_PRICE_BIND_NEXT_VERSION_NOT_EXIST.getErrorCode(), ErrorCodeEnum.PROXY_ELECTRICITY_PRICE_BIND_NEXT_VERSION_NOT_EXIST.getErrorMsg());
        }
        ElectricityPriceEquipment electricityPriceEquipment = BeanUtil.copyProperties(electricityPriceBindBO, ElectricityPriceEquipment.class);
        electricityPriceEquipment.setEquipmentId(electricityPriceBindBO.getNodeId());
        electricityPriceEquipment.setStructureRuleId(electricityPriceRules.get(0).getStructureRuleId());
        electricityPriceEquipment.setRuleId(electricityPriceBindBO.getNextVersionStructurePriceBO().getRuleId());
        electricityPriceEquipment.setVersionId(electricityPriceBindBO.getNextVersionStructurePriceBO().getVersionId());
        electricityPriceEquipment.setState(StateEum.NORMAL.getValue());
        electricityPriceEquipment.setCreateTime(new Date());
        electricityPriceEquipment.setNextChangeFlag(BoolLogic.NO.getCode());
        electricityPriceEquipment.setId(electricityPriceBindBO.getNextVersionStructurePriceBO().getId());
        return electricityPriceEquipment;
    }

    private ElectricityPriceEquipment buildPriceEquipment(ElectricityPriceBindBO electricityPriceBindBO, String nodeName, String structureRuleId) {
        ElectricityPriceEquipment electricityPriceEquipment = BeanUtil.copyProperties(electricityPriceBindBO, ElectricityPriceEquipment.class);
        electricityPriceEquipment.setEquipmentId(electricityPriceBindBO.getNodeId());
        electricityPriceEquipment.setEquipmentName(nodeName);
        electricityPriceEquipment.setStructureRuleId(structureRuleId);
        electricityPriceEquipment.setState(StateEum.NORMAL.getValue());
        electricityPriceEquipment.setCreateTime(new Date());
        return electricityPriceEquipment;
    }
}
