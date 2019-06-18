SQL优化 - 分页查询
-

LIMIT语法

LIMIT [offset ,] size ：从offset+1行开始取，一共取size条数据。offset默认0

LIMIT 10 ：取前10行
LIMIT 10,10 ： 取11-20行

大数据量下的优化

方法1 

SELECT * FROM table 
WHERE id >= (SELECT id FROM table LIMIT 10,1)
LIMIT 10

先通过子查询获得第11条记录的id，然后从id开始取10条

方法2

SELECT * FROM (SELECT id FROM table LIMIT 10,10) AS s
INNER JOIN table AS p ON (s.id = p.id)

先通过子查询得到11-20条记录的id，然后小表驱动大表，得到11-20条记录的全部信息