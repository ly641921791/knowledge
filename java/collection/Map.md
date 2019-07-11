Map
-

###### 无序Map

HashMap

###### 有序Map

TreeMap ：实现了SortMap接口，基于红黑树根据key排序

LinkedHashMap ：继承HashMap，在HashMap.Node基础上加了before、after属性，基于链表根据插入顺序排序，当accessOrder属性为true时，会将访问过的数据放到最后

###### [HashMap](HashMap.md)

###### ConcurrentHashMap

通过分段锁、CAS实现并发高性能

ABA问题，变量增加版本号，每次修改版本号 + 1


 
