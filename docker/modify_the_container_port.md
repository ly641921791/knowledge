Docker 修改容器端口
-

本文介绍如何修改容器端口

对于已经创建的容器，可以通过下面的方法修改端口：

- 将容器提交为镜像，重新运行
- 修改容器配置文件

### 将容器提交为镜像，重新运行

略

### 修改容器配置文件

修改前需要关闭docker，否则镜像重启后，配置文件还原导致修改失败

#### 1. 进入容器配置目录

`cd /var/lib/docker/containers/{container_id}`

#### 2. 修改hostconfig.json

`vim hostconfig.json`

找到PortBindings节点，结构如下

```json
{
    "PortBindings": {
    	"80/tcp": [
	        {
				"HostIp": "",
				"HostPort": "81"
			}
    	]
    }
}
```

这个节点的含义是将服务器的81端口映射到容器的80端口

#### 3. config.v2.json文件

里面的ExposedPorts也有端口信息，目前不知道有什么用

> 参考
> https://stackoverflow.com/questions/19335444/how-do-i-assign-a-port-mapping-to-an-existing-docker-container