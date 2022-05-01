package com.enn.energy.price.web.vo.requestvo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.validator.constraints.Length;
import org.springframework.format.annotation.DateTimeFormat;
import javax.validation.constraints.Future;
import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

/**
 * 电价版本请求VO
 *
 * @author sunjidong
 * @date 2022/4/30
 **/

@ApiModel("电价版本请求VO")
public class ElectricityPriceVersionReqVO implements Serializable {

    private static final long serialVersionUID = 7566019418409200305L;

    @ApiModelProperty(value = "版本名称", required = true, dataType = "string")
    @NotBlank(message = "版本名称不能为空")
    @Length(min = 1, max = 50)
    private String versionName;

    @ApiModelProperty(value = "省编码code", required = true, dataType = "string")
    @NotBlank(message = "省编码code不能为空")
    private String provinceCode;

    @ApiModelProperty(value = "省名称", required = true, dataType = "string")
    @NotBlank(message = "省名称不能为空")
    private String province;

    @ApiModelProperty(value = "租户id", required = true, dataType = "string")
    @NotBlank(message = "租户id不能为空")
    private String tenantId;

    @ApiModelProperty(value = "租户名称", required = true, dataType = "string")
    @NotBlank(message = "租户名称不能为空")
    private String tenantName;

    @ApiModelProperty(value = "系统编码", required = true, dataType = "string")
    private String systemCode;

    @ApiModelProperty(value = "系统名称", required = true, dataType = "string")
    private String systemName;

    @ApiModelProperty(value = "设备id", required = true, dataType = "string")
    private String equipmentId;

    @ApiModelProperty(value = "设备名称", required = true, dataType = "string")
    private String equipmentName;

    @ApiModelProperty(value = "版本生效期", required = true, dataType = "date")
    @NotNull
    @FutureOrPresent(message = "生效日期必须为未来时")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date startDate;

    @ApiModelProperty(value = "电价类型,0:目录电价;1:自定义电价;3:代购电价", required = true, dataType = "string")
    @NotBlank(message = "电价类型不能为空")
    private String priceType;

    @ApiModelProperty(value = "绑定类型,0:企业;1:设备;2:行政区域", required = true, dataType = "string")
    @NotBlank(message = "电价类型不能为空")
    private String bindType;

    public String getVersionName() {
        return versionName;
    }

    public void setVersionName(String versionName) {
        this.versionName = versionName;
    }

    public String getProvinceCode() {
        return provinceCode;
    }

    public void setProvinceCode(String provinceCode) {
        this.provinceCode = provinceCode;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getTenantId() {
        return tenantId;
    }

    public void setTenantId(String tenantId) {
        this.tenantId = tenantId;
    }

    public String getTenantName() {
        return tenantName;
    }

    public void setTenantName(String tenantName) {
        this.tenantName = tenantName;
    }

    public String getSystemCode() {
        return systemCode;
    }

    public void setSystemCode(String systemCode) {
        this.systemCode = systemCode;
    }

    public String getSystemName() {
        return systemName;
    }

    public void setSystemName(String systemName) {
        this.systemName = systemName;
    }

    public String getEquipmentId() {
        return equipmentId;
    }

    public void setEquipmentId(String equipmentId) {
        this.equipmentId = equipmentId;
    }

    public String getEquipmentName() {
        return equipmentName;
    }

    public void setEquipmentName(String equipmentName) {
        this.equipmentName = equipmentName;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public String getPriceType() {
        return priceType;
    }

    public void setPriceType(String priceType) {
        this.priceType = priceType;
    }

    public String getBindType() {
        return bindType;
    }

    public void setBindType(String bindType) {
        this.bindType = bindType;
    }

    @Override
    public String toString() {
        return "ElectricityPriceVersionReqVO{" +
                "versionName='" + versionName + '\'' +
                ", provinceCode='" + provinceCode + '\'' +
                ", province='" + province + '\'' +
                ", tenantId='" + tenantId + '\'' +
                ", tenantName='" + tenantName + '\'' +
                ", systemCode='" + systemCode + '\'' +
                ", systemName='" + systemName + '\'' +
                ", equipmentId='" + equipmentId + '\'' +
                ", equipmentName='" + equipmentName + '\'' +
                ", startDate=" + startDate +
                ", priceType='" + priceType + '\'' +
                ", bindType='" + bindType + '\'' +
                '}';
    }
}