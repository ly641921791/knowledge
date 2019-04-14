docker run 



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
	