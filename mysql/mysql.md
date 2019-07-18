## UNION

用于合并两次或多次查询结果

UNION会消除重复值
UNION ALL不会消除重复值

若子句中存在ORDER或LIMIT等语句，需要使用（）括起来








## REPLACE INTO 用法

若插入列中包含主键、唯一索引且该数据已存在，则先删除后插入；否则直接插入。

- REPLACE INTO tb_name (col1...) VALUES (...)
- REPLACE INTO tb_name (col1...) SELECT ...
- REPLACE INTO tb_name SET col1=value1 , ....









MySql存储引擎
	InnoDB ：事物的ACID通过






## 优化步骤

### 一、语句分析

1. WHERE条件。避免 IN OR LIKE 关键字，CASE...END 函数
2. SQL中的 * 避免。

### 二、优化设计

1. 临时表扫描代替全表扫描
2. EXISTS 和 NOT EXISTS 代替 IN 和 NOT IN。例如 ：SELECT * FROM user AS u WHERE EXISTS （SELECT id FROM user WHERE id = u.id）。EXISTS返回的是否有记录
3. 若模糊查询不必要，则去掉 LIKE
4. 建立合适的索引
5. 避免 *
6. 避免 WHERE 中函数操作
7. 实时性要求不高的表，允许脏读。mysql没有脏读语法糖，暂不写示例。



