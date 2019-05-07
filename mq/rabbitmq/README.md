RabbitMQ
-

docker安装

```shell
docker run 
	-d --name rabbitmq 
	--publish 5671:5671 
	--publish 5672:5672 
	--publish 4369:4369 
	--publish 25672:25672 
	--publish 15671:15671 
	--publish 8797:15672 
	rabbitmq:management
```

> 参考 https://blog.csdn.net/liyuejin/article/details/78410586