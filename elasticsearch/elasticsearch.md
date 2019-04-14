


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






































