package com.larva.zhang.jenkins.agent;

import com.larva.zhang.jenkins.agent.mode.DirectLaunchMode;
import com.larva.zhang.jenkins.agent.mode.JnlpFileLaunchMode;

import java.util.StringJoiner;

/**
 * JenkinsAgentNode
 *
 * 定义了常用的jenkins agent节点启动参数，更多参数详见{@link hudson.remoting.Launcher} 和 {@link hudson.remoting.jnlp.Main}
 *
 * @see hudson.remoting.Launcher
 * @see hudson.remoting.jnlp.Main
 * @author zhanghan
 * @date 2021/3/30
 * @since 1.0
 */
public class JenkinsAgentNode {

    /**
     * agent主机名FQDN，一台机器仅允许部署一个agent
     */
    private String hostname;

    /**
     * 下载jnlp文件启动模式，和{@link #directLaunchMode} 互斥，不能同时存在
     */
    private JnlpFileLaunchMode jnlpFileLaunchMode;

    /**
     * 直接启动模式，和{@link #jnlpFileLaunchMode} 互斥，不能同时存在
     */
    private DirectLaunchMode directLaunchMode;

    public String getHostname() {
        return hostname;
    }

    public void setHostname(String hostname) {
        this.hostname = hostname;
    }

    public JnlpFileLaunchMode getJnlpFileLaunchMode() {
        return jnlpFileLaunchMode;
    }

    public void setJnlpFileLaunchMode(JnlpFileLaunchMode jnlpFileLaunchMode) {
        this.jnlpFileLaunchMode = jnlpFileLaunchMode;
    }

    public DirectLaunchMode getDirectLaunchMode() {
        return directLaunchMode;
    }

    public void setDirectLaunchMode(DirectLaunchMode directLaunchMode) {
        this.directLaunchMode = directLaunchMode;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", JenkinsAgentNode.class.getSimpleName() + "[", "]")
                .add("hostname='" + hostname + "'").add("jnlpFileLaunchMode=" + jnlpFileLaunchMode)
                .add("directLaunchMode=" + directLaunchMode).toString();
    }
}
