Sorted Set
-

有序集合，其中每个元素都有一个double类型分数，通过分数将元素从小到大排序


###### 实现原理

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







Sorted Set

Redis Sorted Set是有序的、不可重复的String集合。Sorted Set中的每个元素都需要指派一个分数(score)，Sorted Set会根据score对元素进行升序排序。如果多个member拥有相同的score，则以字典序进行升序排序。

Sorted Set非常适合用于实现排名。

Sorted Set的主要命令：

ZADD：向指定Sorted Set中添加1个或多个member，时间复杂度O(Mlog(N))，M为添加的member数量，N为Sorted Set中的member数量
ZREM：从指定Sorted Set中删除1个或多个member，时间复杂度O(Mlog(N))，M为删除的member数量，N为Sorted Set中的member数量
ZCOUNT：返回指定Sorted Set中指定score范围内的member数量，时间复杂度：O(log(N))
ZCARD：返回指定Sorted Set中的member数量，时间复杂度O(1)
ZSCORE：返回指定Sorted Set中指定member的score，时间复杂度O(1)
ZRANK/ZREVRANK：返回指定member在Sorted Set中的排名，ZRANK返回按升序排序的排名，ZREVRANK则返回按降序排序的排名。时间复杂度O(log(N))
ZINCRBY：同INCRBY，对指定Sorted Set中的指定member的score进行自增，时间复杂度O(log(N))

慎用的Sorted Set相关命令：

ZRANGE/ZREVRANGE：返回指定Sorted Set中指定排名范围内的所有member，ZRANGE为按score升序排序，ZREVRANGE为按score降序排序，时间复杂度O(log(N)+M)，M为本次返回的member数
ZRANGEBYSCORE/ZREVRANGEBYSCORE：返回指定Sorted Set中指定score范围内的所有member，返回结果以升序/降序排序，min和max可以指定为-inf和+inf，代表返回所有的member。时间复杂度O(log(N)+M)
ZREMRANGEBYRANK/ZREMRANGEBYSCORE：移除Sorted Set中指定排名范围/指定score范围内的所有member。时间复杂度O(log(N)+M)

上述几个命令，应尽量避免传递[0 -1]或[-inf +inf]这样的参数，来对Sorted Set做一次性的完整遍历，特别是在Sorted Set的尺寸不可预知的情况下。可以通过ZSCAN命令来进行游标式的遍历（具体请见 https://redis.io/commands/scan ），或通过LIMIT参数来限制返回member的数量（适用于ZRANGEBYSCORE和ZREVRANGEBYSCORE命令），以实现游标式的遍历。


> 源码 http://download.redis.io/redis-stable/src/t_zset.c

> 实现原理 https://www.jianshu.com/p/cc379427ef9d

> 跳表实现原理 http://zhangtielei.com/posts/blog-redis-skiplist.html