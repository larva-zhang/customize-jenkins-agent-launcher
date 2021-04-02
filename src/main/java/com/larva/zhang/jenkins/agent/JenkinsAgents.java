package com.larva.zhang.jenkins.agent;

import java.util.List;

/**
 * JenkinsAgents jenkins agent节点列表
 *
 * @author zhanghan
 * @date 2021/3/30
 * @since 1.0
 */
public class JenkinsAgents {

    private List<JenkinsAgentNode> nodes;

    public List<JenkinsAgentNode> getNodes() {
        return nodes;
    }

    public void setNodes(List<JenkinsAgentNode> nodes) {
        this.nodes = nodes;
    }
}
