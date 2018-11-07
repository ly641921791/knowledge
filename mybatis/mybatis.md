



## 获取自增主键

	@Options(userGenerationKey=true,keyProperty="user.id")
	
	
	
## @Insert、@Delete、@Update、@Select value属性是数组

针对SQL数组，会将其拼接执行。

对于@Select，SQL拼接执行的同时，会将第一条SQL的结果返回。