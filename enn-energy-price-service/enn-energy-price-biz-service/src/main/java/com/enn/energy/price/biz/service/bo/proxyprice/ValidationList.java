package com.enn.energy.price.biz.service.bo.proxyprice;

import lombok.experimental.Delegate;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

/**
 * xxxxx此处为类的描述信息
 *
 * @author sunjidong
 * @date 2022/4/30
 **/
public class ValidationList<E> implements List<E> {

    @Delegate
    @Valid
    public List<E> list = new ArrayList<>();

}