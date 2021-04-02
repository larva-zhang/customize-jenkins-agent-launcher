package com.larva.zhang.jenkins.agent.mode;

/**
 * LaunchMode
 *
 * @author zhanghan
 * @date 2021/3/31
 * @since 1.0
 */
public interface LaunchMode {

    /**
     * 生成启动参数
     *
     * @return {@link String[]}
     */
    String[] genLaunchArgs();

    /**
     * 启动服务
     *
     * @throws Exception 启动失败时会抛出异常
     */
    void launch() throws Exception;
}
