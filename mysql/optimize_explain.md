Explain
-

Explain命令用于分析SQL的瓶颈

在SQL前面加上explain执行就可以输出SQL的执行计划，简单SQL输出一行，复制SQL输出多行

例如：执行 `EXPLAIN SELECT id FROM user WHERE id = 1`

输出：

|id|select_type|table|partitions|type|possible_keys|key|key_len|ref|rows|filtered|Extra|
|---|---|---|---|---|---|---|---|---|---|---|---|
|1|	SIMPLE|	user	|(NULL)|	const|	PRIMARY	|PRIMARY	|8	|const|	1	|100.00|	Using index|

说明：

|row|mean|
|---|---|
|id|查询语句的标识，越大越先执行|
|select_type|查询类型|
|table|当前行查询的表|
|partitions|匹配的分区|
|type|访问类型|
|possible_keys|可能用到的索引|
|key|使用的索引名，null表示未使用，可以强制索引|
|key_len|索引长度|
|rows|预估扫描行数|
|filtered|查询的表行占表的百分比|
|extra|额外说明<br>避免Using filesort, Using temporary|


select_type
- simple ：简单的查询
- primary ：最外层查询
- union ：
- dependent union ：
- union result ：union结果
- subselect ：
- dependent subselect ：
- derived ：派生表，子查询产生的临时表，from语句中的子查询
- uncacheable subselect ：
- uncacheable union ：

type 效果从上到下依次降低
- system ：const的特例，表中只有一行数据
- const ：最多只有一个记录匹配，通常为主键
- eq_ref ：最多只有一条记录匹配，通常在关联字段时
- ref ：通过索引匹配到多个记录时
- fulltext ：全文索引
- ref_or_null ：ref类似
- index_merge ：索引合并优化
- unique_subquery ：
- range ：索引范围扫描
- index ：类似全表扫描，使用索引避免了排序
- all ：全表扫描