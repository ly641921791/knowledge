@RequestMapping参数详解

value：请求路径，支持占位符
	value="/{id}" 此时id为占位符，可以作为参数使用，通过@PathVariable提取


params：请求参数列表，并支持表达式
	params="!id" 参数不能有id
	params="name!=ly" 参数中name不能等于ly