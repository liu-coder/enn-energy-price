package com.enn.energy.price.biz.service.proxyelectricityprice.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
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
import com.enn.energy.price.dal.po.ext.ElectricityPriceDetailPO;
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
        ElectricityPriceVersionsByBindAreaBO electricityPriceVersionsByBindAreaBO = new ElectricityPriceVersionsByBindAreaBO();
        //根据省市区进行查询有效的版本
        List<ElectricityPriceVersion> electricityPriceVersionsToDistrict = electricityPriceVersionCustomMapper.queryPriceVersionList(new HashMap<String, Object>() {{
            put("provinceCode", electricityPriceBindVersionsBO.getProvinceCode());
            put("cityCode", electricityPriceBindVersionsBO.getCityCode());
            put("districtCode", electricityPriceBindVersionsBO.getDistrictCode());
            put("isCurEffectFlag", BoolLogic.YES.getCode());
        }});
        if (CollUtil.isNotEmpty(electricityPriceVersionsToDistrict)) {
            return ElectricityPriceVersionsByBindAreaBO.builder().provinceCode(electricityPriceBindVersionsBO.getProvinceCode()).cityCode(electricityPriceBindVersionsBO.getCityCode()).districtCode(electricityPriceBindVersionsBO.getDistrictCode()).electricityPriceVersionBOS(ElectricityPriceVersionUpdateBOConverMapper.INSTANCE.electricityPriceVersionListPOToBO(electricityPriceVersionsToDistrict)).build();
        }
        //根据省市进行查询有效版本
        List<ElectricityPriceVersion> electricityPriceVersionsToCity = electricityPriceVersionCustomMapper.queryPriceVersionList(new HashMap<String, Object>() {{
            put("provinceCode", electricityPriceBindVersionsBO.getProvinceCode());
            put("cityCode", electricityPriceBindVersionsBO.getCityCode());
            put("isCurEffectFlag", BoolLogic.YES.getCode());
        }});
        if (CollUtil.isNotEmpty(electricityPriceVersionsToDistrict)) {
            return ElectricityPriceVersionsByBindAreaBO.builder().provinceCode(electricityPriceBindVersionsBO.getProvinceCode()).cityCode(electricityPriceBindVersionsBO.getCityCode()).districtCode(null).electricityPriceVersionBOS(ElectricityPriceVersionUpdateBOConverMapper.INSTANCE.electricityPriceVersionListPOToBO(electricityPriceVersionsToDistrict)).build();
        }
        //根据省进行查询有效版本
        List<ElectricityPriceVersion> electricityPriceVersionsToProvince = electricityPriceVersionCustomMapper.queryPriceVersionList(new HashMap<String, Object>() {{
            put("provinceCode", electricityPriceBindVersionsBO.getProvinceCode());
            put("isCurEffectFlag", BoolLogic.YES.getCode());
        }});
        if (CollUtil.isNotEmpty(electricityPriceVersionsToDistrict)) {
            return ElectricityPriceVersionsByBindAreaBO.builder().provinceCode(electricityPriceBindVersionsBO.getProvinceCode()).cityCode(null).districtCode(null).electricityPriceVersionBOS(ElectricityPriceVersionUpdateBOConverMapper.INSTANCE.electricityPriceVersionListPOToBO(electricityPriceVersionsToDistrict)).build();
        }
        return ElectricityPriceVersionsByBindAreaBO.builder().provinceCode(electricityPriceBindVersionsBO.getProvinceCode()).electricityPriceVersionBOS(null).build();
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
        Map<String, ElectricityPriceEquipment> electricityPriceEuipmentNotExistRuleMap = electricityPriceEquipments.stream().collect(Collectors.toMap(ElectricityPriceEquipment::getEquipmentId, item -> item));
        List<ElectricityPriceBindNodeStatusBO.NodeBindStatus> collect = systemTreeList.stream().map(item -> {
            ElectricityPriceBindNodeStatusBO.NodeBindStatus nodeBindStatus = new ElectricityPriceBindNodeStatusBO.NodeBindStatus();
            nodeBindStatus.setNodeId(item.getNodeId().toString());
            nodeBindStatus.setNodeName(item.getNodeName());
            nodeBindStatus.setStatus(BindStatusEum.UNBOUND.getName());
            if (ObjectUtil.isNotEmpty(electricityPriceEuipmentNotExistRuleMap.get(item.getNodeId()))) {
                nodeBindStatus.setStatus(BindStatusEum.INVALID.getName());
            } else if (ObjectUtil.isNotEmpty(electricityPriceEuipmentNotExistRuleMap.get(item.getNodeId()))) {
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
