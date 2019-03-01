
## 整数类型

|类型|长度（字节）|容量|
|---|----|---|
|TINYINT|1 |2^8|
|SMALLINT|2|2^16|
|MEDIUMINT|3|2^24|
|INT、INTEGER|4|2^32|
|BIGINT|8|2^64|

假设声明一个字段`year INT(4) UNSIGNED AUTO_INCREMENT`

4表示查询时显示的长度，不影响实际容量，一般不设置

UNSIGNED表示该字段无符号

AUTO_INCREMENT表示该字段自增

## 小数类型

|类型|长度（长度）|
|---|---|
|FLOAT|4|
|DOUBLE|8|
|DEC、DECIMAL(M,N)|M+2|

小数类型都可以声明(M,N)，M表示精度、总位数，N表示小数位数。FLOAT和DOUBLE默认精度由硬件和系统决定，DECIMAL默认(10,0)

超出精度则四舍五入处理

DECIMAL以字符串形式处理，精度要求高的字段使用，浮点数数据范围更大，但会引起精度问题

小数类型也支持UNSIGNED属性

## 时间类型

|类型|格式|长度（字节）|范围（年）|
|---|---|---|---|
|YEAR|YYYY|1|1901-2155|
|DATE|YYYY-MM-DD|3|1000-9999|
|TIME|HH:MM:SS|3||
|DATETIME|YYYY-MM-DD HH:MM:SS|8|1000-9999|
|TIMESTAMP|YYYY-MM-DD HH:MM:SS|4|1970-2038|

## 字符类型

### 枚举类型

ENUM类型在建表时，规定列允许的值，语法如下：

`字段名 ENUM('v1','v2',...'vn')`

内部通过整数实现，每个枚举值有一个索引编号，最多65535个




