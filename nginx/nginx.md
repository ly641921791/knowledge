

Nginx
	轻量级的Web/反向代理/电子邮件代理服务器
	特定：占用内存少，并发强
	服务器
	默认端口：80
	
	进入sbin目录
	启动 ./nginx
	其他
		./nginx -s stop		快速关闭
		./nginx -s quit		等worker线程处理完毕关闭
		./nginx -s reload	重新加载配置文件
		./nginx -s reopen	重新打开日志文件


	用于解决C10K问题：一个服务器同时连接10k客户端
	
	基本的nginx由master进程和worker进程组成。master读取配置文件，维护worker，worker对请求处理

	
	nginx无法访问
		1：可能是端口未开放：/sbin/iptables -I INPUT -p tcp --dport 80 -j ACCEPT
	
正向代理和反向代理的区别
	正向代理：客户端通过代理服务器向服务端发出请求，服务端并不知道请求的源头
	反向代理：客户端向服务器发出请求，服务器只是反向代理服务器，会将请求发送至真正的服务器，客户端并不知道请求最终的流向
	用途：
		正向代理典型用途就是跳过防火墙访问外网，如通过VPN翻墙
		反向代理典型用途就是负载均衡
	安全性：
		正向代理客户端可以通过它访问任意网站且隐藏自身，不安全，需要采取安全措施仅对授权的客户端提供服务
		反向代理对外透明，客户端并不知道自己访问的是代理服务器

CentOS7安装nginx服务器
1：编译环境安装
	安装 nginx 需要先将官网下载的源码进行编译，编译依赖 gcc 环境，如果没有 gcc 环境，则需要安装：
	yum install gcc-c++
2：pcre安装
	nginx 的 http 模块使用 pcre 来解析正则表达式，所以需要安装
	yum install -y pcre pcre-devel
3：zlib 安装
	zlib 库提供了很多种压缩和解压缩的方式， nginx 使用 zlib 对 http 包的内容进行 gzip ，所以需要在 Centos 上安装 zlib 库。
	yum install -y zlib zlib-devel
4：OpenSSL 安装
	OpenSSL 是一个强大的安全套接字层密码库，囊括主要的密码算法、常用的密钥和证书封装管理功能及 SSL 协议，并提供丰富的应用程序供测试或其它目的使用。
	nginx 不仅支持 http 协议，还支持 https（即在ssl协议上传输http），所以需要在 Centos 安装 OpenSSL 库。
	yum install -y openssl openssl-devel

5：下载nginx源码
	推荐使用命令下载
	wget -c https://nginx.org/download/nginx-1.10.1.tar.gz
6：解压并进入目录
	tar -zxvf nginx-1.10.1.tar.gz
	cd nginx-1.10.1
7：配置
	使用默认配置
	./configure
8：编译安装
	make
	make install
	






# Elasticsearch

> 电子书
> https://www.elastic.co/guide/cn/elasticsearch/guide/current/_finding_your_feet.html


## 实现：倒排索引

## 结构

Elasticsearch
	|-  Index                   类似数据库
		|-  Type                类似表
			|-  Document        表中一条记录

## 使用

### 插入记录

向megacorp库的employee表插入id=1的记录

```log
PUT /megacorp/employee/1
{
    "first_name" : "John",
    "last_name" :  "Smith",
    "age" :        25,
    "about" :      "I love to go rock climbing",
    "interests": [ "sports", "music" ]
}
```

### 查询记录

查询megacorp库的employee表中id=1的记录

```log
输入
GET /megacorp/employee/1
输出
{
  "_index" :   "megacorp",
  "_type" :    "employee",
  "_id" :      "1",
  "_version" : 1,
  "found" :    true,
  "_source" :  {
      "first_name" :  "John",
      "last_name" :   "Smith",
      "age" :         25,
      "about" :       "I love to go rock climbing",
      "interests":  [ "sports", "music" ]
  }
}
```

### 其他

DELETE  删除
HEAD    检查存在
PUT     更新

### 检索

#### 检索全部

```log
输入
GET /megacorp/employee/_search
输出
{
   "took":      6,
   "timed_out": false,
   "_shards": { ... },
   "hits": {
      "total":      1,
      "max_score":  1,
      "hits": [
         {
            "_index":         "megacorp",
            "_type":          "employee",
            "_id":            "3",
            "_score":         1,
            "_source": {
               "first_name":  "Douglas",
               "last_name":   "Fir",
               "age":         35,
               "about":       "I like to build cabinets",
               "interests": [ "forestry" ]
            }
         }
      ]
   }
}
```

#### 基本检索

查询姓为Smith的人

```log
输入 query-string
GET /megacorp/employee/_search?q=last_name:Smith
输入 DSL表达式
GET /megacorp/employee/_search
{
    "query" : {
        "match" : {
            "last_name" : "Smith"
        }
    }
}
```

查询姓为Smith且年龄大于30的人

```log
GET /megacorp/employee/_search
{
    "query" : {
        "bool": {
            "must": {
                "match" : {
                    "last_name" : "smith" 
                }
            },
            "filter": {
                "range" : {
                    "age" : { "gt" : 30 } 
                }
            }
        }
    }
}
```

#### 高级检索

查询喜欢攀援的人（会查询出部分匹配的记录）

```log
GET /megacorp/employee/_search
{
    "query" : {
        "match" : {
            "about" : "rock climbing"
        }
    }
}
```

短语搜索：精确匹配
```log
GET /megacorp/employee/_search
{
    "query" : {
        "match_phrase" : {
            "about" : "rock climbing"
        }
    }
}
```

高亮搜索：结果会增加匹配的高亮处理

```log
GET /megacorp/employee/_search
{
    "query" : {
        "match_phrase" : {
            "about" : "rock climbing"
        }
    },
    "highlight": {
        "fields" : {
            "about" : {}
        }
    }
}
```

分析：类似group，强大的分析能力




> 官网文档 ： https://www.elastic.co/guide/en/elasticsearch/reference/current/docs-index_.html



## 类型

### 增

在index库的type表中新增或修改id=1的记录，字段如下

```log
PUT index/type/1
{
	"user" : "kimchy",
	"post_date" : "2009-11-15T14:12:12",
	"message" : "trying out Elasticsearch"
}
```


curl -X PUT "http://47.95.252.113:8798/hotcar/user/3" -H 'Content-Type: application/json' -d'
{
    "userId" : 1111
}
';


### 改

同增

### 查

查询index库下type表中id=1的记录

```log
GET index/type/1
```

### 搜



## 补全功能

使用该功能，需要为字段指定类型（completion）。

### 创建mappings

创建一个user索引，字段userName类型为completion

```log
PUT user
{
	"mappings":{
		"_doc":{
			"properties":{
				"userName":{
					"type":"completion"
				}
			}
		}
	}
}
```

支持以下参数

analyzer

	默认simple

search_analyzer

	默认analyzer

preserve_separators

	保留分隔符，默认true。官网文档的解释是，若设置false，输入`foof`，会得到类似`Foo Fighters`的结果。

preserve_position_increments

	不懂

max_input_length

	最大输入长度，默认50。

### 插入记录

> 插入记录并设置排序权重

```log
PUT user/_doc/1
{
	"userName":{
		"inout":["ly","ly2"],
		"weight":34
	}
}
```

input
	
	自动补全候选内容，字符串或字符串数组
	
weight

	权重值，影响排序
	
> 插入记录并设置不同的权重

```log
PUT user/_doc/1
{
	"userName":[
		{
			"input":"ly",
			"weight":34
		},{
			"input":"ly",
			"weight":35
		}
	]
}
```

> 插入记录不设置权重

```log
PUT user/_doc/1
{
	"userName":"ly"
}
```

### 补全搜索

查询user的userName字段以l开始的补全建议，userNameComp为属性名。

```log
POST user/_search
{
	"suggest":{
		"userNameComp":{
			"prefix":"l",
			"completion":{
				"field":"userName"
			}
		}
	}
}
```



看到这里了 https://www.elastic.co/guide/en/elasticsearch/reference/current/search-suggesters-completion.html#querying








































