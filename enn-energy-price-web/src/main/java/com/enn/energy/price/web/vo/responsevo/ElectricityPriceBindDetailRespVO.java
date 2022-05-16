package com.enn.energy.price.web.vo.responsevo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @description: 电价绑定详情响应对象
 * @author:quyl
 * @createTime:2022/5/11 7:25
 */
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(description = "电价绑定详情响应对象")
public class ElectricityPriceBindDetailRespVO implements Serializable {

    @ApiModelProperty(required = true, name = "设备绑定id")
    private Long id;

    private String versionId;

    private String versionName;

    private String structureId;

    private String structureName;

    private String startDate;

    private String endDate;

    private ElectricityPriceDetailRespVO electricityPriceDetailRespVO;

    private String nextChangeFlag;

    private NextVersionPriceBindVO nextVersionPriceBindVO;

    @AllArgsConstructor
    @NoArgsConstructor
    @ApiModel(value = "不继承（自定义）体系时的电价对象")
    public static class NextVersionPriceBindVO {

        @ApiModelProperty(required = true, name = "设备绑定id")
        private Long id;

        private String versionId;

        private String versionName;

        private String structureId;

        private String structureName;

        private String startDate;

        private String endDate;

        private ElectricityPriceDetailRespVO electricityPriceDetailRespVO;

        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public String getVersionId() {
            return versionId;
        }

        public void setVersionId(String versionId) {
            this.versionId = versionId;
        }

        public String getVersionName() {
            return versionName;
        }

        public void setVersionName(String versionName) {
            this.versionName = versionName;
        }

        public String getStructureId() {
            return structureId;
        }

        public void setStructureId(String structureId) {
            this.structureId = structureId;
        }

        public String getStructureName() {
            return structureName;
        }

        public void setStructureName(String structureName) {
            this.structureName = structureName;
        }

        public String getStartDate() {
            return startDate;
        }

        public void setStartDate(String startDate) {
            this.startDate = startDate;
        }

        public String getEndDate() {
            return endDate;
        }

        public void setEndDate(String endDate) {
            this.endDate = endDate;
        }

        public ElectricityPriceDetailRespVO getElectricityPriceDetailRespVO() {
            return electricityPriceDetailRespVO;
        }

        public void setElectricityPriceDetailRespVO(ElectricityPriceDetailRespVO electricityPriceDetailRespVO) {
            this.electricityPriceDetailRespVO = electricityPriceDetailRespVO;
        }
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getVersionId() {
        return versionId;
    }

    public void setVersionId(String versionId) {
        this.versionId = versionId;
    }

    public String getVersionName() {
        return versionName;
    }

    public void setVersionName(String versionName) {
        this.versionName = versionName;
    }

    public String getStructureId() {
        return structureId;
    }

    public void setStructureId(String structureId) {
        this.structureId = structureId;
    }

    public String getStructureName() {
        return structureName;
    }

    public void setStructureName(String structureName) {
        this.structureName = structureName;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public ElectricityPriceDetailRespVO getElectricityPriceDetailRespVO() {
        return electricityPriceDetailRespVO;
    }

    public void setElectricityPriceDetailRespVO(ElectricityPriceDetailRespVO electricityPriceDetailRespVO) {
        this.electricityPriceDetailRespVO = electricityPriceDetailRespVO;
    }

    public String getNextChangeFlag() {
        return nextChangeFlag;
    }

    public void setNextChangeFlag(String nextChangeFlag) {
        this.nextChangeFlag = nextChangeFlag;
    }

    public NextVersionPriceBindVO getNextVersionPriceBindVO() {
        return nextVersionPriceBindVO;
    }

    public void setNextVersionPriceBindVO(NextVersionPriceBindVO nextVersionPriceBindVO) {
        this.nextVersionPriceBindVO = nextVersionPriceBindVO;
    }
}
