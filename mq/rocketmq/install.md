RocketMQ 安装
-

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