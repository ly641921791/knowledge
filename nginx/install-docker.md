# Docker 安装 Nginx

本文介绍如何通过Docker安装Nginx

为了方便修改配置，将本地配置文件挂着到容器

#### 1. 准备配置文件

执行下面命令

```shell
wget -P /etc/nginx https://trac.nginx.org/nginx/browser/nginx/conf/nginx.conf
```

执行命令后，在/etc/nginx目录下出现nginx.conf文件，说明文件下载成功

#### 2. 安装Nginx

执行下面命令

```shell
docker run \
	--name nginx \
	--restart always \
	-v /etc/nginx/nginx.conf:/etc/nginx/nginx.conf:ro \
	-p 80:80 \
	-d nginx:1.15-alpine
```

#### 3. 注意事项

3.1 IP映射

容器属于隔离环境，无法通过localhost访问本地，必须配置IP，通过link连接的容器个人没有试过

3.2 端口映射

例如配置8080:80，配置文件应该监听80端口，监听8080访问失败