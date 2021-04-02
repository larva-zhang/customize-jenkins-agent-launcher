package com.larva.zhang.jenkins.agent;

import java.io.InputStream;
import java.net.InetAddress;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.logging.Logger;

import org.yaml.snakeyaml.TypeDescription;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;

/**
 * JenkinsAgentLauncher
 *
 * <p>
 * 由于JDK9 才提供了{@code @argument file}参数解析，所以为方便部署采用读取Yaml文件的形式获取agent节点启动参数。
 * </p>
 *
 * @author zhanghan
 * @date 2021/3/30
 * @see hudson.remoting.Launcher
 * @since 1.0
 */
public class JenkinsAgentLauncher {

    private static final Logger LOGGER = Logger.getLogger(JenkinsAgentLauncher.class.getName());

    private static final String YAML_FILENAME = "agents.yaml";

    public static void main(String[] args) throws Exception {
        JenkinsAgentNode jenkinsAgentNode;
        // 打包后包含在jar内，所以不能直接用new File的形式读取yaml文件
        try (InputStream inputStream =
                Thread.currentThread().getContextClassLoader().getResourceAsStream(YAML_FILENAME)) {
            if (Objects.isNull(inputStream)) {
                System.err.println("agents.yaml not found");
                System.exit(1);
                return;
            }
            Constructor agentsConstructor = new Constructor(JenkinsAgents.class);
            TypeDescription nodesTypeDescription = new TypeDescription(JenkinsAgents.class);
            nodesTypeDescription.addPropertyParameters("nodes", JenkinsAgentNode.class);
            agentsConstructor.addTypeDescription(nodesTypeDescription);
            Yaml yaml = new Yaml(agentsConstructor);
            JenkinsAgents jenkinsAgents = yaml.loadAs(inputStream, JenkinsAgents.class);
            if (Objects.isNull(jenkinsAgents)) {
                System.err.println("invalid agents.yaml");
                System.exit(1);
                return;
            }
            List<JenkinsAgentNode> nodes = jenkinsAgents.getNodes();
            if (Objects.isNull(nodes) || nodes.isEmpty()) {
                System.err.println("invalid agents.yaml, file is empty");
                System.exit(1);
                return;
            }
            // FQDN，当name service校验主机名不通过时，getCanonicalHostName返回主机ip地址，此时强制使用主机名
            String canonicalHostName = InetAddress.getLocalHost().getCanonicalHostName();
            String fqdn = Objects.equals(InetAddress.getLocalHost().getHostAddress(), canonicalHostName)
                    ? InetAddress.getLocalHost().getHostName()
                    : canonicalHostName;
            Optional<JenkinsAgentNode> nodeOptional =
                    nodes.stream().filter(node -> Objects.equals(node.getHostname(), fqdn)).findFirst();
            if (!nodeOptional.isPresent()) {
                System.err.println("no matching hostname node declared in agents.yaml, hostname is " + fqdn);
                System.exit(1);
                return;
            }
            jenkinsAgentNode = nodeOptional.get();
        } catch (Exception e) {
            System.err.println("parse agents.yaml error, exception : " + e.getMessage());
            System.exit(1);
            return;
        }
        LOGGER.info("Launch Agent: " + jenkinsAgentNode);
        if (jenkinsAgentNode.getDirectLaunchMode() == null && jenkinsAgentNode.getJnlpFileLaunchMode() == null) {
            System.err.println("launch mode undefined, jenkinsAgentNode : " + jenkinsAgentNode);
            System.exit(1);
            return;
        }
        if (jenkinsAgentNode.getJnlpFileLaunchMode() != null && jenkinsAgentNode.getDirectLaunchMode() != null) {
            System.err.println("launch mode conflict, jenkinsAgentNode : " + jenkinsAgentNode);
            System.exit(1);
            return;
        }
        if (jenkinsAgentNode.getJnlpFileLaunchMode() != null) {
            jenkinsAgentNode.getJnlpFileLaunchMode().launch();
        } else {
            jenkinsAgentNode.getDirectLaunchMode().launch();
        }
    }
}
