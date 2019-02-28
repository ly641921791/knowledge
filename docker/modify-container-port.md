# Docker 修改容器端口

本文介绍如何修改容器端口

### 方案1 将容器提交为镜像重新运行

通过commit命令提交

### 方案2 修改容器配置文件

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

该配置说明，将容器的80映射到本地的81端口

#### 3. config.v2.json文件

里面的ExposedPorts也有端口信息，目前不知道有什么用

#### 4. 重启

> 参考
> https://stackoverflow.com/questions/19335444/how-do-i-assign-a-port-mapping-to-an-existing-docker-container