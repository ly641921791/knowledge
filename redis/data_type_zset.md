Sorted Set
-

有序集合，其中每个元素都有一个double类型分数，通过分数将元素从小到大排序

使用场景：排行榜

##### 主要命令

|命令|格式|说明|时间复杂度|类型|
|---|---|---|---|---|
|zadd|zadd key score1 member1 [score2 member2...]|添加/修改M个member，返回添加元素数|O(M * log(N))|添加元素|
|zrem|zrem key member1 member2...|删除M个member，返回删除元素数|O(M * log(N))|删除元素
|zcount|zcount key min max|返回score在[min,max]区间的元素数|O(log(N))|元素计数
|zcard|zcard key|返回全部元素数|O(1)|元素计数
|zscore|zscore key member|返回指定元素的score，元素不存在则返回nil|O(1)|查询分数
|zrank/zrevrank|zrank key member|按照分数升序/降序，返回元素索引，不存在则返回nil|O(log(N))|查询排名
|zincrby|zincrby key increment member|将member的score加上increment，元素不存在则添加元素，返回元素最新分数|O(log(N))|添加分数

###### 慎用命令

ZRANGE/ZREVRANGE：返回指定Sorted Set中指定排名范围内的所有member，ZRANGE为按score升序排序，ZREVRANGE为按score降序排序，时间复杂度O(log(N)+M)，M为本次返回的member数
ZRANGEBYSCORE/ZREVRANGEBYSCORE：返回指定Sorted Set中指定score范围内的所有member，返回结果以升序/降序排序，min和max可以指定为-inf和+inf，代表返回所有的member。时间复杂度O(log(N)+M)
ZREMRANGEBYRANK/ZREMRANGEBYSCORE：移除Sorted Set中指定排名范围/指定score范围内的所有member。时间复杂度O(log(N)+M)

上述几个命令，应尽量避免传递[0 -1]或[-inf +inf]这样的参数，来对Sorted Set做一次性的完整遍历，特别是在Sorted Set的尺寸不可预知的情况下。可以通过ZSCAN命令来进行游标式的遍历（具体请见 https://redis.io/commands/scan ），或通过LIMIT参数来限制返回member的数量（适用于ZRANGEBYSCORE和ZREVRANGEBYSCORE命令），以实现游标式的遍历。

##### 实现原理

Sorted Set通过ziplist和zset两种结构存储元素

###### ziplist

ziplist是一种特殊编码的双向链表，当元素数量小于128且每个元素都小于64字节时使用，通过修改配置可以改变限制条件

```conf
zset-max-ziplist-entries 128
zset-max-ziplist-value 64
```

###### zset

zset结构体通过hash table和skip list存储元素，实现有序，插入和移除操作时间复杂度为O(log(N))

hash table保存member到score的映射，保证查询member的score时间复杂度是O(1)

skip list按照score从小到大保存member，链表的一种

##### 参考网站

> 源码 http://download.redis.io/redis-stable/src/t_zset.c
>
> 实现原理 https://www.jianshu.com/p/cc379427ef9d
>
> 跳表实现原理 http://zhangtielei.com/posts/blog-redis-skiplist.html
