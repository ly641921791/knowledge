





## @Accessors

　　翻译是存取器。通过该注解可以控制getter和setter方法的形式。

- fluent 若为true，则getter和setter方法的方法名都是属性名，且setter方法返回当前对象。
```java
@Data
@Accessors(fluent = true)
class User {
	private Integer id;
	private String name;
	
	// 生成的getter和setter方法如下，方法体略
	public Integer id(){}
	public User id(Integer id){}
	public String name(){}
	public User name(String name){}
}
```
- chain 若为true，则setter方法返回当前对象
```java
@Data
@Accessors(chain = true)
class User {
	private Integer id;
	private String name;
	
	// 生成的setter方法如下，方法体略
	public User setId(Integer id){}
	public User setName(String name){}
}
```
- prefix 若为true，则getter和setter方法会忽视属性名的指定前缀（遵守驼峰命名）
```java
@Data
@Accessors(prefix = "f")
class User {
	private Integer fId;
	private String fName;
	
	// 生成的getter和setter方法如下，方法体略
	public Integer id(){}
	public void id(Integer id){}
	public String name(){}
	public void name(String name){}
}
```
