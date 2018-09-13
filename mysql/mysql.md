## UNION

用于合并两次或多次查询结果

UNION会消除重复值
UNION ALL不会消除重复值

若子句中存在ORDER或LIMIT等语句，需要使用（）括起来






## 索引

### 设置索引

- 主键索引 ALTER TABLE tb_name ADD PRIMARY KEY (col_name)
- 唯一索引 ALTER TABLE tb_name ADD UNIQUE (col_name)
- 普通索引 ALTER TABLE tb_name ADD INDEX (col_name)
- 全文索引 ALTER TABLE tb_name ADD FULLTEXT (col_name)
- 多列索引 ALTER TABLE tb_name ADD INDEX index_name (col1,col2...)

### 查看索引

SHOW INDEX FROM tb_name


## REPLACE INTO 用法

若插入列中包含主键、唯一索引且该数据已存在，则先删除后插入；否则直接插入。

- REPLACE INTO tb_name (col1...) VALUES (...)
- REPLACE INTO tb_name (col1...) SELECT ...
- REPLACE INTO tb_name SET col1=value1 , ....









MySql存储引擎
	InnoDB ：事物的ACID通过