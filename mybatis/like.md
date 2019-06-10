MyBatis 模糊查询LIKE传参方式
-

### $

参数 keyword = "keyword"

SELECT id FROM user WHERE name LIKE '%${keyword}%'

### #

参数 keyword = "keyword"，mybatis会在参数外面加上''，因为%需要使用""

SELECT id FROM user WHERE name LIKE "%"#{keyword}"%"

参数 keyword = "keyword"

SELECT id FROM user WHERE name LIKE CONCAT('%',#{keyword},'%')


参数 keyword = "%keyword%"，提前加上百分号

SELECT id FROM user WHERE name LIKE #{keyword}

### bind

```xml
<select>
	<bind name="search" value="'%' + keyword + '%'" />
	SELECT id FROM user WHERE name LIKE #{search}
</select>
```


LIKE子句如何传参
https://blog.csdn.net/why15732625998/article/details/79081146