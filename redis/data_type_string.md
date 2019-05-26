String
-






###### incr

命令：incr key

说明：将value的值加1，若key不存在，则初始化为0再加1。返回更新后的value

注意：若value不是数字则报错，value限制为64位有符号数字，即[-2^63,2^63]

###### incrby

命令：incrby key increment

说明：将value的值加increment，其他同incr命令

###### incrbyfloat

命令：incrbyfloat key increment

说明：将value的值加上increment，increment可以为浮点数，其他同incr命令

###### decr

###### decrby



