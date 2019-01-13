[TOC]

# Dockerfile学习

## 案例学习

首先拿出Nginx Dockerfile官网案例作为参考，了解一下Dockerfile

```log
# 使用FROM指令指定构建本镜像使用的基础镜像。
FROM alpine:3.8

# 使用LABEL指令指定镜像的标签。作用未知。
LABEL maintainer="NGINX Docker Maintainers <docker-maint@nginx.com>"

# 使用ENV指令设置环境变量。这里定义了使用的nginx版本，通过$使用
ENV NGINX_VERSION 1.15.8

# 使用RUN指令设置执行的命令。nginx这里比较长，省略一波，主要是环境设置，nginx下载安装等过程
RUN GPG_KEYS=B0F4253373F8F6F510D42178520A9993A1C052F8 \
	... \
	&& curl -fSL https://nginx.org/download/nginx-$NGINX_VERSION.tar.gz -o nginx.tar.gz \
	&& curl -fSL https://nginx.org/download/nginx-$NGINX_VERSION.tar.gz.asc  -o nginx.tar.gz.asc \
	... \
	&& ln -sf /dev/stderr /var/log/nginx/error.log

# 使用COPY指令复制本地的文件到到容器中。
COPY nginx.conf /etc/nginx/nginx.conf
COPY nginx.vh.default.conf /etc/nginx/conf.d/default.conf

# 使用EXPOSE指令声明暴露的端口。
EXPOSE 80

# 使用STOPSIGNAL指令声明容器退出时，发送给PID=1的进程的指令。
STOPSIGNAL SIGTERM

# 使用CMD指令声明容器启动时执行的命令。
CMD ["nginx", "-g", "daemon off;"]
```

> 案例地址 ：https://github.com/nginxinc/docker-nginx/blob/master/mainline/alpine/Dockerfile

## 编写并使用

1. 新建文件Dockerfile，内容如下：

```log
FROM nginx
RUN echo '<h1>Hello Dockerfile</h1>' > /usr/share/nginx/html/index.html
```

2. 构建镜像，在Dockerfile所在目录执行：

docker build -t nginx:test .

构建成功后，本地会多出一个nginx:test的镜像。可以通过run命令启动该容器，并访问nginx首页查看效果。

## 常用指令

### ADD

复制文件

