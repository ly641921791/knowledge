[TOC]

# Docker安装redis

> 相关地址 ：https://hub.docker.com/_/redis

## 准备redis容器启动命令

保存为redis.sh文件，作为备份，同时供日后修改。

```shell
docker run \
	--name redis \
	--restart always \
	-v /root/redis.conf:/usr/local/etc/redis/redis.conf \
	-p 6379:6379 \
	-d redis:5 
```

## 准备redis配置文件

> 配置文件地址 ： http://download.redis.io/redis-stable/redis.conf

将内容复制保存为redis.conf

## 执行命令启动

sh redis.sh