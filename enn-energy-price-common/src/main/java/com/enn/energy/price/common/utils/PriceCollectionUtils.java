package com.enn.energy.price.common.utils;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

/**
 * 价格中心集合工具类.
 *
 * @author : wuchaon
 * @version : 1.0 2021/11/30 10:05
 * @since : 1.0
 **/
@Slf4j
public class PriceCollectionUtils {


    /**
     * 层级之间的实体转换
     * @param sourceList
     * @param clazz
     * @param <T>
     * @return
     */
    public static <T> List<T> convertEntity(List sourceList, Class<T> clazz) {

        List<T> targetList = new ArrayList<>();
        if (CollectionUtil.isEmpty(sourceList)) {
            return targetList;
        }
  //      try {
            for (Object s : sourceList) {
//                T target = clazz.newInstance();
//                BeanUtil.copyProperties(s, target);
//                BeanUtil.toBean(s,clazz);
                targetList.add(BeanUtil.toBean(s,clazz));
            }
//        } catch (InstantiationException e) {
//            log.info("实体转换实例化异常,{}", clazz.getName());
//        } catch (IllegalAccessException e) {
//            log.info("实体转换访问权限异常,{}", clazz.getName());
//        }
        return targetList;
    }


}
