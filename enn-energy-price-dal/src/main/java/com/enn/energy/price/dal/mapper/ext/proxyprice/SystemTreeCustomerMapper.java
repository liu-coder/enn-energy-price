package com.enn.energy.price.dal.mapper.ext.proxyprice;

import com.enn.energy.price.dal.po.mbg.SystemTree;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface SystemTreeCustomerMapper {

    /**
     * 根据父节点查找节点下子节点列表
     *
     * @param map
     * @return
     */
    List<SystemTree> getSystemTreeListByMap(Map<String,Object> map);

}
