RocketMQ 安装
-

##### 下载 & 编译

进入官方[下载地址](https://rocketmq.apache.org/dowloading/releases/)，下载目标版本的二进制包或源码包，也可以
到[Github](https://github.com/apache/rocketmq)下载

> 教程地址 https://rocketmq.apache.org/docs/quick-start/

> 控制台地址 https://github.com/apache/rocketmq-externals/tree/master/rocketmq-console

##### Docker 安装

###### 启动NameServer和Broker

``` shell
docker run -d -p 9876:9876 -v `pwd`/data/namesrv/logs:/root/logs -v `pwd`/data/namesrv/store:/root/store --name rmqnamesrv -e "MAX_POSSIBLE_HEAP=100000000" rocketmqinc/rocketmq:4.3.0 sh mqnamesrv

docker run -d -p 10911:10911 -p 10909:10909 -v `pwd`/data/broker/logs:/root/logs -v `pwd`/data/broker/store:/root/store --name rmqbroker --link rmqnamesrv:namesrv -e "NAMESRV_ADDR=namesrv:9876" -e "MAX_POSSIBLE_HEAP=200000000" rocketmqinc/rocketmq:4.3.0 sh mqbroker
```

注 ：启动broker时，需要通过修改配置指定外网可以访问的brokerIp，否则会将容器内部ip注册到namesrv，导致客户端访问不到

> 官网文档 https://github.com/apache/rocketmq-externals/tree/master/rocketmq-docker 

###### 启动控制台

``` shell
docker run -e "JAVA_OPTS=-Drocketmq.namesrv.addr=192.168.0.1:9876 -Dcom.rocketmq.sendMessageWithVIPChannel=false" -p 8080:8080 -t styletang/rocketmq-console-ng:1.0.0
```

注：启动控制台时，需要将命令中namesrv地址修改为外网可以访问的地址

> 官网文档 https://github.com/apache/rocketmq-externals/tree/master/rocketmq-console