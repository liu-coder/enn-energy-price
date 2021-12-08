package com.enn.energy.price.integration.participant.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import top.rdfa.framework.biz.dto.BaseResponse;

/**
 * @author Zhiqiang Li
 * @date 2021/12/1
 * @description:
 **/
@EqualsAndHashCode(callSuper = false)
@Data
public class ParticipantResponse <T> extends BaseResponse {

    private String msg;

    private Integer code;

    private T data;

    public boolean success() {
        return code == 200;
    }

    @Override
    public String toLog() {
        return toString();
    }
}
