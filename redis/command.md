Redis Command
-

Redis允许的最大Key和Value都是512MB

避免使用过长的key，会消耗更多的内存并导致查询效率低下

Key命名有一定的可读性，例如：object-type:id:attribute规范，"user:1"或"user:1:followers"

### String

String是Redis的基础数据类型，Int、Float、Boolean都以String体现

常用命令：

- SET

设置一个key持有一个value

SET key value [EX seconds|PX milliseconds] [NX|XX]

EX ：过期时间，单位秒
PX ：过期时间，单位毫秒
NX ：仅当key不存在时设置
XX ：仅当key存在时设置

- GET

获得key持有的value

GET key

- GETSET

设置key持有的value，并返回旧的value

GETSET key value

- MSET

设置多个key持有的value

MSET key value [key value ...]

- MGET

获得多个key持有的value

MGET key [key ...]




SET：为一个key设置value，可以配合EX/PX参数指定key的有效期，通过NX/XX参数针对key是否存在的情况进行区别操作，时间复杂度O(1)
GET：获取某个key对应的value，时间复杂度O(1)
GETSET：为一个key设置value，并返回该key的原value，时间复杂度O(1)
MSET：为多个key设置value，时间复杂度O(N)
MSETNX：同MSET，如果指定的key中有任意一个已存在，则不进行任何操作，时间复杂度O(N)
MGET：获取多个key对应的value，时间复杂度O(N)

上文提到过，Redis的基本数据类型只有String，但Redis可以把String作为整型或浮点型数字来使用，主要体现在INCR、DECR类的命令上：

INCR：将key对应的value值自增1，并返回自增后的值。只对可以转换为整型的String数据起作用。时间复杂度O(1)
INCRBY：将key对应的value值自增指定的整型数值，并返回自增后的值。只对可以转换为整型的String数据起作用。时间复杂度O(1)
DECR/DECRBY：同INCR/INCRBY，自增改为自减。

INCR/DECR系列命令要求操作的value类型为String，并可以转换为64位带符号的整型数字，否则会返回错误。

也就是说，进行INCR/DECR系列命令的value，必须在[-2^63 ~ 2^63 – 1]范围内。
前文提到过，Redis采用单线程模型，天然是线程安全的，这使得INCR/DECR命令可以非常便利的实现高并发场景下的精确控制。

例1：库存控制

在高并发场景下实现库存余量的精准校验，确保不出现超卖的情况。
设置库存总量：

SET inv:remain "100"
 
库存扣减+余量校验：

DECR inv:remain
 
当DECR命令返回值大于等于0时，说明库存余量校验通过，如果返回小于0的值，则说明库存已耗尽。

假设同时有300个并发请求进行库存扣减，Redis能够确保这300个请求分别得到99到-200的返回值，每个请求得到的返回值都是唯一的，绝对不会找出现两个请求得到一样的返回值的情况。

例2：自增序列生成

实现类似于RDBMS的Sequence功能，生成一系列唯一的序列号

设置序列起始值：

SET sequence "10000"
 
获取一个序列值：

INCR sequence
 
直接将返回值作为序列使用即可。

获取一批（如100个）序列值：

INCRBY sequence 100
 
假设返回值为N，那么[N – 99 ~ N]的数值都是可用的序列值。

当多个客户端同时向Redis申请自增序列时，Redis能够确保每个客户端得到的序列值或序列范围都是全局唯一的，绝对不会出现不同客户端得到了重复的序列值的情况。



### HyperLogLog

2.8.9版本

HyperLogLog是用来做基数统计的算法，优点是所需要的空间特别小，不会存储元素本身，常用来统计访问量、阅读量等可以存在一定的误差的数字

- PFADD key v1 [v2 ...]

将制定元素添加到key中

- PFCOUNT key1 [key2 ...]

当只有一个参数，返回近似基础

当多个参数，返回并集的近似基数

- PFMERGE destKey sourceKey [sourceKey ...]

将sourceKey合并成destKey