[TOC]

## 1 Docker架构

- Docker daemon ：Docker守护进程
- Client ：Docker客户端
- Images ：Docker镜像，用于创建Docker容器
- Container ：Docker容器
- Registry ：

## 2 安装及启停Docker

### 2.1 系统要求

- CentOS 7
- 64位

### 2.2 安装及卸载

	安装 sudo yum -y install docker-engine
	卸载 sudo yum -y remove docker-engine
	删除残余文件（镜像、容器、配置等） sudo rm -rf /var/lib/docker

### 2.3 启动

	sudo systemctl start docker
	
## 3 常用命令

	查看版本 docker version
	其他命令 https://docs.docker.com/engine/reference/commandline/docker/#child-commands
	
### 3.1 镜像命令

- 查询 docker search java

	查询结果为5列分别如下
	
	- NAME ：镜像名
	- DESCRIPTION ：镜像描述
	- STARS ：收藏数
	- OFFICAL ：官方认可，标记为[OK]则为官方镜像
	- AUTOMATED ：自动化的，是否自动构建的镜像
	
- 下载 docker pull java

- 查看已下载镜像 docker images








- 删除 docker rmi java













Dockerfile构建Docker镜像

	FROM nginx
	RUN echo '<h1>hello docker</h1>' > /usr/share/nginx/html/index.html

echo：输出指定文字
>：覆盖或创建指定文件
>>：创建或追加内容到指定文件


Docker简介
	
	