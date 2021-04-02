# customize-jenkins-agent-launcher

该工程是为了方便模板化部署 [jenkinsci/remoting](https://github.com/jenkinsci/remoting) 做的一层封装。

封装提供了以下能力：

- 通过yaml文件维护agent节点启动参数
- 屏蔽jnlp文件和直连模式启动差异

## jenkins agent节点启动参数

[agents.yaml](src/main/resources/agents.yaml) 文件中维护了jenkins全部agent节点的启动参数。

```yaml
nodes: # 根节点，无其他含义 
  # jnlp文件启动模式
  - hostname: agent部署机器的FQDN
    jnlpFileLaunchMode: # 下载jnlp文件启动模式
      jnlpUrl: controller节点jnlp文件下载url
      secret: 密钥文本或密钥存储文件路径
      workDir: 工作目录

  # 直连启动模式
  - hostname: agent部署机器的FQDN
    directLaunchMode: # 直连controller节点启动模式
      agentName: agent节点名称
      secretKey: 密钥文本
      rootUrl: controller节点根路径
      workDir: 工作目录
```

只在 [JenkinsAgentNode](src/main/java/com/larva/zhang/jenkins/agent/JenkinsAgentNode.java) 中定义了常用的参数，其他参数一律采用默认值。

## 使用方法

1. Jenkins Login后进入 `agent` 节点创建页面，`Manage Jenkins > Manage Nodes and Clouds > New Node` 。

2.  创建 `agent` 节点时 `Launch Method` 选择 `Launch agent by connecting it to the master` 。

3. Git Clone后根据创建 `agent` 节点时得到的参数，修改`agents.yml`。
   
4. 执行maven打包。
    ```shell
    mvn package
    ```

    打包后生成的`target` 目录结构如下：
    ```shell
    .
    ├── jenkins-agent-launcher.jar
    └── lib
        ├── remoting-4.7.jar
        └── snakeyaml-1.28.jar
    ```
    `lib` 目录为依赖包存放目录，已经在`MANIFEST.MF`中加入了Class-Path。

5. 启动服务。
    ```shell
    java -jar jenkins-agent-launcher.jar
    ```

## 扩展

这个工程仅是提供一个思路，相关的配置也全部维护在`agents.yml` 文件中，如果有配置中心，也可以接入配置中心来替代`agents.yml`。