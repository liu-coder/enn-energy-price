package com.enn.energy.price.biz.service.bo.proxyprice;

import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * @description:电价绑定请求参数
 * @author:quyl
 * @createTime:2022/5/4 15:17
 */
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ElectricityPriceBindNodeStatusBO implements Serializable {

    List<NodeBindStatus> nodeBindStatusList;

    @AllArgsConstructor
    @NoArgsConstructor
    @ApiModel(description = "节点绑定状态对象")
    public static class NodeBindStatus implements Serializable {

        private String nodeId;

        private String nodeName;

        private String status;

        public String getNodeId() {
            return nodeId;
        }

        public void setNodeId(String nodeId) {
            this.nodeId = nodeId;
        }

        public String getNodeName() {
            return nodeName;
        }

        public void setNodeName(String nodeName) {
            this.nodeName = nodeName;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }
    }

    public List<NodeBindStatus> getNodeBindStatusList() {
        return nodeBindStatusList;
    }

    public void setNodeBindStatusList(List<NodeBindStatus> nodeBindStatusList) {
        this.nodeBindStatusList = nodeBindStatusList;
    }
}
