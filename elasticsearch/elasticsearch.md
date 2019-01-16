


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







































