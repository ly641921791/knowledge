Stream
-

### 介绍

Jdk1.8新增，方便处理集合的API。有以下特性：

- 无存储性。类似视图，不存储数据
- 无修改性。任何操作，不修改原集合
- 惰性执行。不会立即执行，需要结果时才执行
- 可消费性。只能遍历一次，再次遍历需要重新生成

使用流程：创建流 -> 操作流 -> 使用流

### 创建流

1. 通过集合创建流

```
Stream<String> stream = Arrays.asList("entity").stream(); 
```

2. 通过静态方法创建

```
Stream<String> stream = Stream.of("entity");
```

### 操作流

经过操作流的结果还是流，常见操作如下：

|api|说明|
|---|---|
|filter|过滤元素|
|map|将元素映射到对应结果|
|limit/skip|返回/丢弃指定数量元素|
|sorted|元素排序|
|distinct|去重|


```
// filter ：a1
Stream.of("a1","b1").filter(string -> string.startsWith("a")).forEach(System.out::println);

// map ：1 4 9
Stream.of(1,2,3).map(i -> i*i).forEach(System.out::println);

// limit ：1 2
Stream.of(1,2,3).limit(2).forEach(System.out::println);

// sorted ：1 2 3
Stream.of(3,2,1).sorted().forEach(System.out::println);

// distinct ：1
Stream.of(1,1).distinct().forEach(System.out::println);
```

### 使用流

使用流后，将不能再次使用或操作，否则抛出异常。常见使用如下：

|api|说明|
|---|---|
|forEach|遍历元素|
|count|统计元素数量|
|collect|返回集合|

```
// forEach ：1 2 3
Stream.of(1,2,3).forEach(System.out::println);

// count : 3
Stream.of(1,2,3).count();

// collect : [1,2,3]
List<Integr> list = Stream.of(1,2,3).collect(Collectors.toList()); 
```



