package com.larva.zhang.jenkins.agent.mode;

import java.util.LinkedList;
import java.util.List;
import java.util.StringJoiner;

import hudson.remoting.jnlp.Main;
import io.jenkins.remoting.shaded.org.kohsuke.args4j.CmdLineException;
import io.jenkins.remoting.shaded.org.kohsuke.args4j.CmdLineParser;

/**
 * DirectLaunchMode
 *
 * Usage参考 <a href=
 * "https://github.com/jenkinsci/remoting/blob/master/docs/inbound-agent.md#connect-directly-to-tcp-port">Connect
 * directly to TCP port</a>，更多参数详见{@link hudson.remoting.jnlp.Main}
 *
 * @see hudson.remoting.jnlp.Main
 * @author zhanghan
 * @date 2021/3/31
 * @since 1.0
 */
public class DirectLaunchMode implements LaunchMode {

    /**
     * agent节点名称
     */
    private String agentName;

    /**
     * 密钥文本
     */
    private String secretKey;

    /**
     * Jenkins Controller节点根路径
     */
    private String rootUrl;

    /**
     * 工作目录
     */
    private String workDir;

    @Override
    public String[] genLaunchArgs() {
        List<String> args = new LinkedList<>();
        if (rootUrl != null) {
            args.add("-url");
            args.add(rootUrl);
        }
        if (workDir != null) {
            args.add("-workDir");
            args.add(workDir);
        }
        // 强制使用websocket
        args.add("-webSocket");
        // 强制使用无GUI模式
        args.add("-headless");
        if (secretKey != null) {
            args.add(secretKey);
        }
        if (agentName != null) {
            args.add(agentName);
        }
        return args.toArray(new String[0]);
    }

    @Override
    public void launch() throws Exception {
        Main.main(genLaunchArgs());
    }

    public String getAgentName() {
        return agentName;
    }

    public void setAgentName(String agentName) {
        this.agentName = agentName;
    }

    public String getSecretKey() {
        return secretKey;
    }

    public void setSecretKey(String secretKey) {
        this.secretKey = secretKey;
    }

    public String getRootUrl() {
        return rootUrl;
    }

    public void setRootUrl(String rootUrl) {
        this.rootUrl = rootUrl;
    }

    public String getWorkDir() {
        return workDir;
    }

    public void setWorkDir(String workDir) {
        this.workDir = workDir;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", DirectLaunchMode.class.getSimpleName() + "[", "]")
                .add("agentName='" + agentName + "'").add("secretKey='" + secretKey + "'")
                .add("rootUrl='" + rootUrl + "'").add("workDir='" + workDir + "'").toString();
    }
}
