package com.enn.energy.price.common.utils;

import com.alibaba.fastjson.JSON;
import org.springframework.beans.BeanUtils;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author ：chenchangtong
 * @date ：Created 2021/11/22 18:04
 * @description：快乐工作每一天
 */
public class BeanUtil {
    public static <T> T map(Object source, Class<T> target) {
        if (source == null) {
            return null;
        } else {
            try {
                T ret = target.newInstance();
                BeanUtils.copyProperties(source, ret);
                return ret;
            } catch (Exception var3) {
                throw new IllegalStateException(var3);
            }
        }
    }

    public static <T> T deepMap(Object source, Class<T> target) {
        if (source == null) {
            return null;
        } else {
            try {
                return JSON.parseObject(JSON.toJSONString(source), target);
            } catch (Exception var3) {
                throw new IllegalStateException(var3);
            }
        }
    }

    public static <T> List<T> mapList(List<?> source, Class<T> target) {
        return source == null ? null : (List)source.stream().map((e) -> {
            return map(e, target);
        }).collect(Collectors.toList());
    }
}
