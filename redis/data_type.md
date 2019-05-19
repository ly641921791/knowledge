数据类型
-

它支持多种类型的数据结构，如字符串（String），散列（Hash），列表（List），集合（Set），有序集合（Sorted Set或者是ZSet）与范围查询，Bitmaps，Hyperloglogs 和地理空间（Geospatial）索引半径查询。其中常见的数据结构类型有：String、List、Set、Hash、ZSet这5种。

类型	简介	特性	场景
String(字符串)	二进制安全	可以包含任何数据,比如jpg图片或者序列化的对象,一个键最大能存储512M	---
Hash(字典)	键值对集合,即编程语言中的Map类型	适合存储对象,并且可以像数据库中update一个属性一样只修改某一项属性值(Memcached中需要取出整个字符串反序列化成对象修改完再序列化存回去)	存储、读取、修改用户属性
List(列表)	链表(双向链表)	增删快,提供了操作某一段元素的API	1,最新消息排行等功能(比如朋友圈的时间线) 2,消息队列
Set(集合)	哈希表实现,元素不重复	1、添加、删除,查找的复杂度都是O(1) 2、为集合提供了求交集、并集、差集等操作	1、共同好友 2、利用唯一性,统计访问网站的所有独立ip 3、好友推荐时,根据tag求交集,大于某个阈值就可以推荐
Sorted Set(有序集合)	将Set中的元素增加一个权重参数score,元素按score有序排列	数据插入集合时,已经进行天然排序	1、排行榜 2、带权重的消息队列


List

Redis的List是链表型的数据结构，可以使用LPUSH/RPUSH/LPOP/RPOP等命令在List的两端执行插入元素和弹出元素的操作。虽然List也支持在特定index上插入和读取元素的功能，但其时间复杂度较高（O(N)），应小心使用。

与List相关的常用命令：

LPUSH：向指定List的左侧（即头部）插入1个或多个元素，返回插入后的List长度。时间复杂度O(N)，N为插入元素的数量
RPUSH：同LPUSH，向指定List的右侧（即尾部）插入1或多个元素
LPOP：从指定List的左侧（即头部）移除一个元素并返回，时间复杂度O(1)
RPOP：同LPOP，从指定List的右侧（即尾部）移除1个元素并返回
LPUSHX/RPUSHX：与LPUSH/RPUSH类似，区别在于，LPUSHX/RPUSHX操作的key如果不存在，则不会进行任何操作
LLEN：返回指定List的长度，时间复杂度O(1)
LRANGE：返回指定List中指定范围的元素（双端包含，即LRANGE key 0 10会返回11个元素），时间复杂度O(N)。应尽可能控制一次获取的元素数量，一次获取过大范围的List元素会导致延迟，同时对长度不可预知的List，避免使用LRANGE key 0 -1这样的完整遍历操作。

应谨慎使用的List相关命令：

LINDEX：返回指定List指定index上的元素，如果index越界，返回nil。index数值是回环的，即-1代表List最后一个位置，-2代表List倒数第二个位置。时间复杂度O(N)
LSET：将指定List指定index上的元素设置为value，如果index越界则返回错误，时间复杂度O(N)，如果操作的是头/尾部的元素，则时间复杂度为O(1)
LINSERT：向指定List中指定元素之前/之后插入一个新元素，并返回操作后的List长度。如果指定的元素不存在，返回-1。如果指定key不存在，不会进行任何操作，时间复杂度O(N)

由于Redis的List是链表结构的，上述的三个命令的算法效率较低，需要对List进行遍历，命令的耗时无法预估，在List长度大的情况下耗时会明显增加，应谨慎使用。

换句话说，Redis的List实际是设计来用于实现队列，而不是用于实现类似ArrayList这样的列表的。如果你不是想要实现一个双端出入的队列，那么请尽量不要使用Redis的List数据结构。

为了更好支持队列的特性，Redis还提供了一系列阻塞式的操作命令，如BLPOP/BRPOP等，能够实现类似于BlockingQueue的能力，即在List为空时，阻塞该连接，直到List中有对象可以出队时再返回。针对阻塞类的命令，此处不做详细探讨，请参考官方文档（https://redis.io/topics/data-types-intro） 中”Blocking operations on lists”一节。

Hash

Hash即哈希表，Redis的Hash和传统的哈希表一样，是一种field-value型的数据结构，可以理解成将HashMap搬入Redis。

Hash非常适合用于表现对象类型的数据，用Hash中的field对应对象的field即可。

Hash的优点包括：

可以实现二元查找，如”查找ID为1000的用户的年龄”
比起将整个对象序列化后作为String存储的方法，Hash能够有效地减少网络传输的消耗
当使用Hash维护一个集合时，提供了比List效率高得多的随机访问命令

与Hash相关的常用命令：

HSET：将key对应的Hash中的field设置为value。如果该Hash不存在，会自动创建一个。时间复杂度O(1)
HGET：返回指定Hash中field字段的值，时间复杂度O(1)
HMSET/HMGET：同HSET和HGET，可以批量操作同一个key下的多个field，时间复杂度：O(N)，N为一次操作的field数量
HSETNX：同HSET，但如field已经存在，HSETNX不会进行任何操作，时间复杂度O(1)
HEXISTS：判断指定Hash中field是否存在，存在返回1，不存在返回0，时间复杂度O(1)
HDEL：删除指定Hash中的field（1个或多个），时间复杂度：O(N)，N为操作的field数量
HINCRBY：同INCRBY命令，对指定Hash中的一个field进行INCRBY，时间复杂度O(1)

应谨慎使用的Hash相关命令：

HGETALL：返回指定Hash中所有的field-value对。返回结果为数组，数组中field和value交替出现。时间复杂度O(N)
HKEYS/HVALS：返回指定Hash中所有的field/value，时间复杂度O(N)

上述三个命令都会对Hash进行完整遍历，Hash中的field数量与命令的耗时线性相关，对于尺寸不可预知的Hash，应严格避免使用上面三个命令，而改为使用HSCAN命令进行游标式的遍历，具体请见 
https://redis.io/commands/scan
Set

Redis Set是无序的，不可重复的String集合。

与Set相关的常用命令：

SADD：向指定Set中添加1个或多个member，如果指定Set不存在，会自动创建一个。时间复杂度O(N)，N为添加的member个数
SREM：从指定Set中移除1个或多个member，时间复杂度O(N)，N为移除的member个数
SRANDMEMBER：从指定Set中随机返回1个或多个member，时间复杂度O(N)，N为返回的member个数
SPOP：从指定Set中随机移除并返回count个member，时间复杂度O(N)，N为移除的member个数
SCARD：返回指定Set中的member个数，时间复杂度O(1)
SISMEMBER：判断指定的value是否存在于指定Set中，时间复杂度O(1)
SMOVE：将指定member从一个Set移至另一个Set

慎用的Set相关命令：

SMEMBERS：返回指定Hash中所有的member，时间复杂度O(N)
SUNION/SUNIONSTORE：计算多个Set的并集并返回/存储至另一个Set中，时间复杂度O(N)，N为参与计算的所有集合的总member数
SINTER/SINTERSTORE：计算多个Set的交集并返回/存储至另一个Set中，时间复杂度O(N)，N为参与计算的所有集合的总member数
SDIFF/SDIFFSTORE：计算1个Set与1或多个Set的差集并返回/存储至另一个Set中，时间复杂度O(N)，N为参与计算的所有集合的总member数

上述几个命令涉及的计算量大，应谨慎使用，特别是在参与计算的Set尺寸不可知的情况下，应严格避免使用。可以考虑通过SSCAN命令遍历获取相关Set的全部member（具体请见 https://redis.io/commands/scan ），如果需要做并集/交集/差集计算，可以在客户端进行，或在不服务实时查询请求的Slave上进行。

Bitmap和HyperLogLog

Redis的这两种数据结构相较之前的并不常用，在本文中只做简要介绍，如想要详细了解这两种数据结构与其相关的命令，请参考官方文档
https://redis.io/topics/data-types-intro 中的相关章节

Bitmap在Redis中不是一种实际的数据类型，而是一种将String作为Bitmap使用的方法。可以理解为将String转换为bit数组。使用Bitmap来存储true/false类型的简单数据极为节省空间。

HyperLogLogs是一种主要用于数量统计的数据结构，它和Set类似，维护一个不可重复的String集合，但是HyperLogLogs并不维护具体的member内容，只维护member的个数。也就是说，HyperLogLogs只能用于计算一个集合中不重复的元素数量，所以它比Set要节省很多内存空间。
