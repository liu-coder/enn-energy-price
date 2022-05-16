package com.enn.energy.price.biz.service.bo.proxyprice;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @description: 电价绑定详情响应对象
 * @author:quyl
 * @createTime:2022/5/11 7:25
 */
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ElectricityPriceBindDetailBO implements Serializable {

    private Long id;

    private String versionId;

    private String versionName;

    private String structureId;

    private String structureName;

    private String startDate;

    private String endDate;

    private String bindStatus;

    private ElectricityPriceDetailBO electricityPriceDetailBO;

    private String nextChangeFlag;

    private NextVersionPriceBindBO nextVersionPriceBindBO;

    @AllArgsConstructor
    @NoArgsConstructor
    public static class NextVersionPriceBindBO {

        private Long id;

        private String versionId;

        private String versionName;

        private String structureId;

        private String structureName;

        private String startDate;

        private String endDate;

        private ElectricityPriceDetailBO electricityPriceDetailBO;

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

        public ElectricityPriceDetailBO getElectricityPriceDetailBO() {
            return electricityPriceDetailBO;
        }

        public void setElectricityPriceDetailBO(ElectricityPriceDetailBO electricityPriceDetailBO) {
            this.electricityPriceDetailBO = electricityPriceDetailBO;
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

    public ElectricityPriceDetailBO getElectricityPriceDetailBO() {
        return electricityPriceDetailBO;
    }

    public void setElectricityPriceDetailBO(ElectricityPriceDetailBO electricityPriceDetailBO) {
        this.electricityPriceDetailBO = electricityPriceDetailBO;
    }

    public String getNextChangeFlag() {
        return nextChangeFlag;
    }

    public void setNextChangeFlag(String nextChangeFlag) {
        this.nextChangeFlag = nextChangeFlag;
    }

    public NextVersionPriceBindBO getNextVersionPriceBindBO() {
        return nextVersionPriceBindBO;
    }

    public void setNextVersionPriceBindBO(NextVersionPriceBindBO nextVersionPriceBindBO) {
        this.nextVersionPriceBindBO = nextVersionPriceBindBO;
    }

    public String getBindStatus() {
        return bindStatus;
    }

    public void setBindStatus(String bindStatus) {
        this.bindStatus = bindStatus;
    }
}
