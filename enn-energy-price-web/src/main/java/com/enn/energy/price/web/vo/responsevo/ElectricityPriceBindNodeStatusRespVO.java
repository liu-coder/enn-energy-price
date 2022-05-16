package com.enn.energy.price.web.vo.responsevo;

import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * @description: 电价绑定节点状态列表响应对象
 * @author:quyl
 * @createTime:2022/5/11 7:25
 */
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(description = "电价绑定节点状态列表响应对象")
public class ElectricityPriceBindNodeStatusRespVO implements Serializable {

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
