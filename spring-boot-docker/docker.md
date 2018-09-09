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

- 查看本地镜像 docker images

	查询结果为5列分别如下
	
	- REPOSITORY ：所属仓库名
	- TAG ：镜像标签，默认latest，表示最新
	- IMAGE ID ：镜像的唯一ID
	- CREATED ：创建时间 
	- SIZE ：镜像大小

- 删除 docker rmi java

	删除镜像 ：docker rmi java
	删除全部 ：docker rmi -f ${docker images} (-f ：强制)

### 3.2 容器命令

#### 3.2.1 新建并启动容器 docker run 

常见选项

- -d ：后台运行
- -P ：随机端口映射
- -p ：指定端口映射
	- ip:hostPort:containerPort
	- ip::containerPort
	- hostPort:containerPort
	- containerPort
- --network ：网络模式
	- network=bridge 默认，使用默认的网桥
	- network=host 使用宿主的网络
	- network=container:NAME_or_ID 使用已有容器的网络配置
	- network=none 不配置
- --name ：指定容器名
	
使用案例

- docker run -d -p 80:80 nginx	
	
#### 3.2.2 列出容器 docker ps

查询结果包含以下7列

- CONTAINER_ID ：容器ID
- IMAGE ：表示镜像名
- COMMAND ：启动时的命令
- CREATED ：创建时间
- STATUS ：运行状态（Up：运行中、Exited：已停止）
- PORTS ：对外端口号
- NAMES ：容器名

#### 3.2.3 启停容器

	启动容器 docker start [container_name|container_id]
	停止容器 docker stop [container_name|container_id]
	强制停止 docker kill [container_name|container_id]
	重启容器 docker restart [container_name|container_id]

重启容器命令实际是stop和start命令的组合

#### 3.2.4 进入容器

- docker attach [container_name|container_id]

	多个窗口同事使用attach进入容器，会同步显示控制台，若一个窗口阻塞，其他窗口无法操作

- nsenter

	p209
	
#### 3.2.5 删除容器

	删除停止容器 docker rm [container_name|container_id]
	强制删除容器 docker rm -f [container_name|container_id]
	删除全部容器 docker rm -f $(docker ps -a -q) 


















Dockerfile构建Docker镜像

	FROM nginx
	RUN echo '<h1>hello docker</h1>' > /usr/share/nginx/html/index.html

echo：输出指定文字
>：覆盖或创建指定文件
>>：创建或追加内容到指定文件


Docker简介
	
	