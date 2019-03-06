RocketMQ 安装
-

### Docker 安装

#### 启动NameServer和Broker

```
docker run -d -p 9876:9876 -v `pwd`/data/namesrv/logs:/root/logs -v `pwd`/data/namesrv/store:/root/store --name rmqnamesrv -e "MAX_POSSIBLE_HEAP=100000000" rocketmqinc/rocketmq:4.3.0 sh mqnamesrv

docker run -d -p 10911:10911 -p 10909:10909 -v `pwd`/data/broker/logs:/root/logs -v `pwd`/data/broker/store:/root/store --name rmqbroker --link rmqnamesrv:namesrv -e "NAMESRV_ADDR=namesrv:9876" -e "MAX_POSSIBLE_HEAP=200000000" rocketmqinc/rocketmq:4.3.0 sh mqbroker
```

使用时可能会出现brokerIp不正确的问题，通过修改配置强制指定brokerIP即可

更多参数查阅 https://github.com/apache/rocketmq-externals/tree/master/rocketmq-docker 

#### 启动控制台

```
docker run -e "JAVA_OPTS=-Drocketmq.namesrv.addr=192.168.1.104:9876 -Dcom.rocketmq.sendMessageWithVIPChannel=false" -p 8080:8080 -t styletang/rocketmq-console-ng
```

更多参数查阅 https://github.com/apache/rocketmq-externals/tree/master/rocketmq-console