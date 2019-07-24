RandomAccess
-

标记接口，用来标记一个List支持随机访问，是否支持随机访问会影响遍历等操作的效率

支持随机访问的List，for遍历效率高于iterator遍历，如：ArrayList

不支持随机访问的List，for遍历低于iterator遍历，如：LinkedList