约束
-

MySQL数据库包含下面几种约束：

- 主键约束
- 外键约束
- 非空约束
- 唯一约束
- 默认约束

###### 主键约束

主键约束（Primary Key Constraint）要求该列非空唯一，可唯一标识一条记录，可作为其他表的外键定义表关系，可加快数据查询速度。包含下面两种：

- 单列主键：由一个字段组成的主键，整型单列主键可设置自增
- 联合主键：由多个字段组成的主键

定义列的同时指定单列主键：`字段名 数据类型 PRIMARY KEY`

```mysql
CREATE TABLE tbl(
    id INT PRIMARY KEY,
    f1 INT
)
```

定义列完成后指定单列主键：`[CONSTRAINT <约束名>] PRIMARY KEY (字段名)`

```mysql
CREATE TABLE tbl(
    id INT,
    f1 INT,
    PRIMARY KEY (id)
)
```

定义列完成后指定联合主键：类似单列主键

```mysql
CREATE TABLE tbl(
    f1 INT,
    f2 INT,
    PRIMARY KEY (f1,f2)
)
```



定义所有列后指定索引

主键 唯一索引区别

- 主键是一种约束，唯一索引是一种索引
- 主键包含唯一索引的功能
- 主键非空，唯一索引可空
- 主键可作为其他表的外键，唯一索引不可以
- 主键最多一个，唯一索引可有多个
