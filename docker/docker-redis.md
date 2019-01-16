[toc]

## 摘要

本文介绍通过Docker安装Redis的过程。

## 安装过程

将配置文件保存在本地，挂载到容器内部，方便修改配置。保存容器启动命令，作为备份。

1. /root下新增redis.conf文件，作为redis容器的配置文件。

2. /root下新增redis.sh文件，作为容器创建脚本，内容如下。

```shell
docker run \
	--name redis \
	--restart always \
	-v /root/redis.conf:/usr/local/etc/redis/redis.conf \
	-p 6379:6379 \
	-d redis:5 
```

3. 运行容器创建脚本，命令如下

```shell
sh redis.sh
```

## 相关参考

> Redis镜像官网地址 ：https://hub.docker.com/_/redis
> Redis官网配置文件 ：http://download.redis.io/redis-stable/redis.conf