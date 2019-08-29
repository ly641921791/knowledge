Regex
-

###### 或 |

v|n 匹配 v或n

lo(v|n)e 匹配 love或lone

测试匹配，'love'是否匹配'lo(v|n)e'

``` java
	Pattern pattern = Pattern.compile("lo(v|n)e");
	Matcher matcher = pattern.matcher("love");
	// true
	System.out.println(matcher.matches());
```

测试是否有给定关键字，'love'中是否有lo或ln

``` java
	Pattern pattern = Pattern.compile("lo|ln");
	Matcher matcher = pattern.matcher("love");
	// true
	System.out.println(matcher.find());
```

Matcher.group  https://www.cnblogs.com/jiafuwei/p/6080984.html