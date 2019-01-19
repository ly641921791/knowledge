


开发模式

```shell
docker run \
	--name elasticsearch \
	-d \
	-p 9200：9200 \
	-p 9300：9300 \
	-e "discovery.type=single-node" \
	docker.elastic.co/elasticsearch/elasticsearch:6.5.4
```

生产模式

1. 检查vm.max_map_count，最少262144。

	查看vm.max_map_count，`sysctl vm.max_map_count`
	设置vm.max_map_count，`sysctl -w vm.max_map_count=262144`
	
	或
	
	打开 ：vi /etc/sysctl.conf
	
	写入 ：vm.max_map_count=655360
	
	执行 ：sysctl -p

2. 执行

```
docker run \
	-it \
	-d \
	-p 9200:9200 -p 9300:9300 \
	-v /opt/data/elasticsearch/logs:/usr/share/elasticsearch/logs \
	-v /opt/data/elasticsearch/data:/usr/share/elasticsearch/data \
	--name mylasticsearch -e "discovery.type=single-node" elasticsearch
```

elasticsearch 报错 ERROR: bootstrap checks failed max virtual memory areas vm.max_map_count [65530] 
解决方案：生产模式 1


> Elasticsearch官方文档 ：https://www.elastic.co/guide/en/elasticsearch/reference/6.5/docker.html
> Elasticsearch官方Docker ：https://hub.docker.com/_/elasticsearch/