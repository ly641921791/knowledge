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

3.1 避免配置localhost，容器属于隔离环境，无法通过localhost访问本地 