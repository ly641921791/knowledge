## Redis 防止表单重复提交

```java
public class Controller {
    public static void main(String[] args){
		Jedis jedis = new Jedis();
		
		// 锁名 'lock:'+方法名+用户id
		String key = "lock:" + Thread.currentThread().getStackTrace()[1].getMethodName();
		
		// 值 参数值hash
		String value = "param";
		
		// 加锁
		String result = jedis.set(key + crc32Long, "aa", "NX", "EX", 1000);
		
		// 加锁成功
		if (!"OK".equals(result)){
			// 报错：请勿重复提交
		}
		
		try{
		    // 业务处理
		}catch (Exception e){
		    
		}finally{
		    // 执行结束释放锁
		    jedis.del(key);
		}
    }
}
```

SpringBoot