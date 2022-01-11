package com.enn.energy.price.client.dto.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @author ：chenchangtong
 * @date ：Created 2021/11/2 10:15
 * @description：快乐工作每一天
 */
@ApiModel("分页基类")
public class BasePageDto implements Serializable {
    public static final int DEFAULT_PAGE = 1;
    public static final int DEFAULT_SIZE = 10;
    public static final int MAX_SIZE = 1000;

    @ApiModelProperty(value = "查询第几页,默认从1开始")
    private Integer pageNum = DEFAULT_PAGE;
    @ApiModelProperty(value = "每页记录数，默认10条")
    private Integer pageSize = DEFAULT_SIZE;
    private Integer offSet = DEFAULT_PAGE;
    private Integer offSize = DEFAULT_SIZE;

    public Integer getPageNum() {
        if (pageNum == null || pageNum <= 0) {
            return DEFAULT_PAGE;
        }
        return pageNum;
    }

    //设置
    public void setPageNum(Integer pageNum) {
        this.pageNum = pageNum;
    }

    public Integer getPageSize() {
        if (pageSize == null || pageSize <= 0) {
            return DEFAULT_SIZE;
        }
        if (pageSize > MAX_SIZE) {
            pageSize = MAX_SIZE;
        }
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public void setOffSet(Integer offSet) {
        this.offSet = offSet;
    }

    public void setOffSize(Integer offSize) {
        this.offSize = offSize;
    }

    @ApiModelProperty(hidden = true)
    public static int countSet(Integer pageNum,Integer pageSize) {
        return (pageNum - 1) * pageSize;
    }

    @ApiModelProperty(hidden = true)
    public static int countSize(Integer pageNum,Integer pageSize) {
        return pageSize;
    }
}

