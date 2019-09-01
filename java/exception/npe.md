防止空指针异常
	https://mp.weixin.qq.com/s/SQwt4mQmwcqtgwqQqx8nFg

为什么MySQL尽量避免列为NULL？

	《高性能MySQL》4.1
	
	1 可为NULL的列使得索引、索引统计、值比较更复杂。
	2 可为NULL的列使用更多存储空间，可为NULL的列被索引时，每个记录需要一个额外的字节，在MyISAM里，会导致固定大小的索引变成可变大小的索引

	主要目的：便于代码的可读性和可维护性

	1 NOT IN、!= 查询时，NULL所在列会返回
	2 CONCAT 连接字符串时，存在NULL会使结果为NULL
	3 COUNT 统计时，NULL列不计数
	4 查询空需要 ='' 和 IS NULL 两个条件
	5 explain SELECT * from t2 where name = '张三' 解析SQL会发现，NULL索引列长度比非NULL索引列多1字节