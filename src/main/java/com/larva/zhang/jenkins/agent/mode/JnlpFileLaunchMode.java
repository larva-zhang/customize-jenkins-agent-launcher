package com.larva.zhang.jenkins.agent.mode;

import java.util.LinkedList;
import java.util.List;
import java.util.StringJoiner;

import hudson.remoting.Launcher;

/**
 * JnlpFileLaunchMode
 *
 * Usage参考 <a href="https://github.com/jenkinsci/remoting/blob/master/docs/inbound-agent.md#download-jnlp-file">Download
 * JNLP file</a>，更多参数详见{@link hudson.remoting.Launcher}
 *
 * @see hudson.remoting.Launcher
 * @author zhanghan
 * @date 2021/3/31
 * @since 1.0
 */
public class JnlpFileLaunchMode implements LaunchMode {

    /**
     * jnlp文件下载 url
     */
    private String jnlpUrl;

    /**
     * 密钥文本 or 存放密钥文本的文件路径
     */
    private String secret;

    /**
     * 指定agent的工作目录
     */
    private String workDir;

    public String getJnlpUrl() {
        return jnlpUrl;
    }

    public void setJnlpUrl(String jnlpUrl) {
        this.jnlpUrl = jnlpUrl;
    }

    public String getSecret() {
        return secret;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }

    public String getWorkDir() {
        return workDir;
    }

    public void setWorkDir(String workDir) {
        this.workDir = workDir;
    }

    @Override
    public String[] genLaunchArgs() {
        List<String> args = new LinkedList<>();
        if (jnlpUrl != null) {
            args.add("-jnlpUrl");
            args.add(getJnlpUrl());
        }
        if (secret != null) {
            args.add("-secret");
            args.add(secret);
        }
        if (workDir != null) {
            args.add("-workDir");
            args.add(workDir);
        }
        return args.toArray(new String[0]);
    }

    @Override
    public void launch() throws Exception {
        Launcher.main(genLaunchArgs());
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", JnlpFileLaunchMode.class.getSimpleName() + "[", "]")
                .add("jnlpUrl='" + jnlpUrl + "'").add("secret='" + secret + "'").add("workDir='" + workDir + "'")
                .toString();
    }
}
