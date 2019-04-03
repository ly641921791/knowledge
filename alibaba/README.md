2018年阿里巴巴关于java重要开源项目汇总
-

1.分布式应用服务开发的一站式解决方案 Spring Cloud Alibaba

Spring Cloud Alibaba 致力于提供分布式应用服务开发的一站式解决方案。此项目包含开发分布式应用服务的必需组件，方便开发者通过 Spring Cloud 编程模型轻松使用这些组件来开发分布式应用服务。

依托 Spring Cloud Alibaba，您只需要添加一些注解和少量配置，就可以将 Spring Cloud 应用接入阿里分布式应用解决方案，通过阿里中间件来迅速搭建分布式应用系统。

地址：https://github.com/spring-cloud-incubator/spring-cloud-alibaba

2. JDBC 连接池、监控组件 Druid

Druid是一个 JDBC 组件。

1.监控数据库访问性能。

2.提供了一个高效、功能强大、可扩展性好的数据库连接池。

3.数据库密码加密。

4.SQL执行日志。

地址：https://github.com/alibaba/druid

3. Java 的 JSON 处理器 fastjson

fastjson 是一个性能很好的 Java 语言实现的 JSON 解析器和生成器，来自阿里巴巴的工程师开发。

主要特点：快速FAST (比其它任何基于Java的解析器和生成器更快，包括jackson）；强大（支持普通JDK类包括任意Java Bean Class、Collection、Map、Date或enum）；零依赖（没有依赖其它任何类库除了JDK）。

地址：https://github.com/alibaba/fastjson

4. 服务框架 Dubbo

Apache Dubbo (incubating) |是阿里巴巴的一款高性能、轻量级的开源Java RPC框架，它提供了三大核心能力：面向接口的远程方法调用，智能容错和负载均衡，以及服务自动注册和发现。

地址：https://github.com/alibaba/dubbo

5. 企业级流式计算引擎 JStorm

JStorm 是参考 Apache Storm 实现的实时流式计算框架，在网络IO、线程模型、资源调度、可用性及稳定性上做了持续改进，已被越来越多企业使用。JStorm 可以看作是 storm 的 java 增强版本，除了内核用纯java实现外，还包括了thrift、python、facet ui。从架构上看，其本质是一个基于 zk 的分布式调度系统。

地址：https://github.com/alibaba/jstorm

6. apns4j

apns4j 是 Apple Push Notification Service 的 Java 实现！

地址：https://github.com/teaey/apns4j

7. 分布式数据层 TDDL

TDDL 是一个基于集中式配置的 jdbc datasource实现，具有主备，读写分离，动态数据库配置等功能。

地址：https://github.com/alibaba/tb_tddl

8. 轻量级分布式数据访问层 CobarClient

Cobar Client是一个轻量级分布式数据访问层（DAL）基于iBatis(已更名为MyBatis)和Spring框架实现。

地址：https://github.com/alibaba/cobarclient

9. 淘宝定制 JVM：TaobaoJVM

TaobaoJVM 基于 OpenJDK HotSpot VM，是国内第一个优化、定制且开源的服务器版Java虚拟机。目前已经在淘宝、天猫上线，全部替换了Oracle官方JVM版本，在性能，功能上都初步体现了它的价值。

地址：http://jvm.taobao.org

10. Java 图片处理类库 SimpleImage

SimpleImage是阿里巴巴的一个Java图片处理的类库，可以实现图片缩略、水印等处理。

地址：https://github.com/alibaba/simpleimage

11. redis 的 java 客户端 Tedis

Tedis 是另一个 redis 的 java 客户端。Tedis 的目标是打造一个可在生产环境直接使用的高可用 Redis 解决方案。

地址：https://github.com/justified/tedis

12.开源 Java 诊断工具 Arthas

Arthas（阿尔萨斯）是阿里巴巴开源的 Java 诊断工具，深受开发者喜爱。

Arthas 采用命令行交互模式，同时提供丰富的 Tab 自动补全功能，进一步方便进行问题的定位和诊断。

地址：https://alibaba.github.io/arthas/

13.动态服务发现、配置和服务管理平台 Nacos



Nacos 致力于帮助您发现、配置和管理微服务。Nacos 提供了一组简单易用的特性集，帮助您实现动态服务发现、服务配置管理、服务及流量管理。

Nacos 帮助您更敏捷和容易地构建、交付和管理微服务平台。 Nacos 是构建以“服务”为中心的现代应用架构(例如微服务范式、云原生范式)的服务基础设施。

地址：https://nacos.io/en-us/

14.Java 解析 Excel 工具 easyexcel

Java 解析、生成 Excel 比较有名的框架有 Apache poi、jxl 。但他们都存在一个严重的问题就是非常的耗内存，poi 有一套 SAX 模式的 API 可以一定程度的解决一些内存溢出的问题，但 POI 还是有一些缺陷，比如 07 版 Excel 解压缩以及解压后存储都是在内存中完成的，内存消耗依然很大。easyexcel 重写了 poi 对 07 版 Excel 的解析，能够原本一个 3M 的 excel 用 POI sax 依然需要 100M 左右内存降低到 KB 级别，并且再大的 excel 不会出现内存溢出，03 版依赖 POI 的 sax 模式。在上层做了模型转换的封装，让使用者更加简单方便。

地址：https://github.com/alibaba/easyexcel

15.高可用流量管理框架 Sentinel

Sentinel 是面向微服务的轻量级流量控制框架，从流量控制、熔断降级、系统负载保护等多个维度保护服务的稳定性。

只要通过 Sentinel API 定义的代码，就是资源，能够被 Sentinel 保护起来。大部分情况下，可以使用方法签名，URL，甚至服务名称作为资源名来标示资源。

地址：https://github.com/alibaba/Sentinel

16.基于多维度 Metrics 的系统度量和监控中间件 SOFALookout

Lookout 是一个利用多维度的 metrics 对目标系统进行度量和监控的项目。Lookout 的多维度 metrics 参考 Metrics 2.0 标准。Lookout 项目分为客户端部分与服务器端部分。

客户端是一个 Java 的类库，可以将它植入您的应用代码中采集 metrics 信息，客户端更多详情。

服务端代码部分，将于下一版本提供。通过 LOOKOUT 的服务，可以对 metrics 数据进行收集、加工、存储和查询等处理，另外结合 grafana，可做数据可视化展示。

地址：https://github.com/alipay/sofa-lookout

17.基于 Spring Boot 的研发框架 SOFABoot

SOFABoot 是蚂蚁金服开源的基于 Spring Boot 的研发框架，它在 Spring Boot 的基础上，提供了诸如 Readiness Check，类隔离，日志空间隔离等等能力。在增强了 Spring Boot 的同时，SOFABoot 提供了让用户可以在 Spring Boot 中非常方便地使用 SOFAStack 相关中间件的能力。

地址：https://github.com/alipay/sofa-boot

18.轻量级 Java 类隔离容器 SOFAArk

SOFAArk 是一款基于 Java 实现的轻量级类隔离容器，由蚂蚁金服公司开源贡献；主要为应用程序提供类隔离和依赖包隔离的能力；基于 Fat Jar 技术，应用可以被打包成一个自包含可运行的 Fat Jar，应用既可以是简单的单模块 Java 应用也可以是 Spring Boot 应用。可访问网址进入快速开始并获取更多详细信息。

地址：https://alipay.github.io/sofastack.github.io/

19.分布式链路追踪中间件 SOFATracer

SOFATracer 是一个用于分布式系统调用跟踪的组件，通过统一的 traceId 将调用链路中的各种网络调用情况以日志的方式记录下来，以达到透视化网络调用的目的。这些日志可用于故障的快速发现，服务治理等。

地址：https://github.com/alipay/sofa-tracer

20.高性能 Java RPC 框架 SOFARPC

SOFARPC 是一个高可扩展性、高性能、生产级的 Java RPC 框架。在蚂蚁金服 SOFARPC 已经经历了十多年及五代版本的发展。SOFARPC 致力于简化应用之间的 RPC 调用，为应用提供方便透明、稳定高效的点对点远程服务调用方案。为了用户和开发者方便的进行功能扩展，SOFARPC 提供了丰富的模型抽象和可扩展接口，包括过滤器、路由、负载均衡等等。同时围绕 SOFARPC 框架及其周边组件提供丰富的微服务治理方案。

地址：https://github.com/alipay/sofa-rpc

21.基于 Netty 的网络通信框架 SOFABolt

SOFABolt 是蚂蚁金融服务集团开发的一套基于 Netty 实现的网络通信框架。

为了让 Java 程序员能将更多的精力放在基于网络通信的业务逻辑实现上，而不是过多的纠结于网络底层 NIO 的实现以及处理难以调试的网络问题，Netty 应运而生。

为了让中间件开发者能将更多的精力放在产品功能特性实现上，而不是重复地一遍遍制造通信框架的轮子，SOFABolt 应运而生。

地址：https://github.com/alipay/sofa-bolt

22.动态非侵入 AOP 解决方案 JVM-Sandbox

JVM-Sandbox，JVM 沙箱容器，一种基于 JVM 的非侵入式运行期 AOP 解决方案。

地址：https://github.com/alibaba/jvm-sandbox

23.面向云的分布式消息领域标准 OpenMessaging

OpenMessaging 是由阿里巴巴发起，与雅虎、滴滴出行、Streamlio 公司共同参与创立，旨在创立厂商无关、平台无关的分布式消息及流处理领域的应用开发标准。

地址：https://github.com/openmessaging/openmessaging-java

24.P2P 文件分发系统 Dragonfly

Dragonfly（蜻蜓）是阿里自研的 P2P 文件分发系统，用于解决大规模文件分发场景下分发耗时、成功率低、带宽浪费等难题。大幅提升发布部署、数据预热、大规模容器镜像分发等业务能力。

开源版的 Dragonfly 可用于 P2P 文件分发、容器镜像分发、局部限速、磁盘容量预检等。它支持多种容器技术，对容器本身无需做任何改造，镜像分发比 natvie 方式提速可高达 57 倍，Registry 网络出流量降低99.5%以上。

地址：https://github.com/alibaba/Dragonfly

25.LayoutManager 定制化布局方案 vlayout

VirtualLayout是一个针对RecyclerView的LayoutManager扩展, 主要提供一整套布局方案和布局间的组件复用的问题。

地址：https://github.com/alibaba/vlayout

26.Java 代码规约扫描插件 P3C

项目包含三部分：PMD 实现、IntelliJ IDEA 插件、Eclipse 插件

地址：https://github.com/alibaba/p3c

27.Android 容器化框架 Atlas

Atlas 是由阿里巴巴移动团队自研的手机淘宝安卓客户端容器化框架，以容器化思路解决大规模团队协作问题，实现并行开发、快速迭代和动态部署，适用于 Android 4.x 以上系统版本的大小型 App 开发。

地址：http://atlas.taobao.org/