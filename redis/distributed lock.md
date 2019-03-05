## Redis 实现分布式锁

本文介绍如何使用Redis实现分布式锁

Jedis提供了public String set(final String key, final String value, final String nxxx, final String expx, final long time)
方法，可以仅在key不存在时插入一个带有过期时间的数据，利用该方法可以实现分布式锁，下面以SpringBoot应用为例

引入依赖

```xml
<dependency>
	<groupId>org.springframework.boot</groupId>
	<artifactId>spring-boot-starter-data-redis</artifactId>
	<exclusions>
        <exclusion>
            <groupId>io.lettuce</groupId>
            <artifactId>lettuce-core</artifactId>
        </exclusion>
    </exclusions>
</dependency>
<dependency>
	<groupId>redis.clients</groupId>
	<artifactId>jedis</artifactId>
</dependency>
```

处理流程如下

```java
public class Controller {
    
    @Autowired
    private JedisConnectionFactory redisConnectionFactory;
    
    public void postForm(Long userId,String formData){
		// 获得Redis客户端
        RedisConnection conn = RedisConnectionUtils.getConnection(factory);
        Jedis jedis = ((JedisConnection) conn).getJedis();
        
        // 设置锁名 lock + 方法名 + 用户名 + formData（经过处理，此处略）
        String redisKey = "lock:" + Thread.currentThread().getStackTrace()[1].getMethodName() + ":" + userId + ":" + formData;
        try {
            // 加锁
            String result = jedis.set(redisKey,"lock","NX","EX",1000);
            
            // 判断加锁结果
            if (!"OK".equals(result)){
                // 加锁失败
                throw new RuntimeException("重复提交");
            }
            
            // 业务处理 略
            
        } finally {
            // 解锁并释放资源
            if (jedis!=null){
                jedis.del(redisKey);
            }
            RedisConnectionUtils.releaseConnection(conn, factory);
        }
    }
}
```

使用RedisTemplate的execute方法也可以实现该功能，相关代码如下

```java
public class Demo {
    
    private static final RedisScript<String> SCRIPT_LOCK = new DefaultRedisScript<>("return redis.call('set',KEYS[1],ARGV[1],'NX','PX',ARGV[2])", String.class);
    private static final RedisScript<String> SCRIPT_UNLOCK = new DefaultRedisScript<>("if redis.call('get',KEYS[1]) == ARGV[1] then return tostring(redis.call('del', KEYS[1])==1) else return 'false' end", String.class);
    
    @Autowired
    private RedisTemplate redisTemplate;
 
    @Override
    public LockInfo tryLock(String key, long expire, long timeout) throws Exception {
        Assert.isTrue(timeout > 0, "tryTimeout must more than 0");
        long start = System.currentTimeMillis();
        int tryCount = 0;
        String lockId = PROCESS_ID + Thread.currentThread().getId();
        while (System.currentTimeMillis() - start < timeout) {
            Object lockResult = redisTemplate.execute(SCRIPT_LOCK,
                    redisTemplate.getStringSerializer(),
                    redisTemplate.getStringSerializer(),
                    Collections.singletonList(key),
                    lockId, String.valueOf(expire));
            tryCount++;
            if (LOCK_SUCCESS.equals(lockResult)) {
                return new LockInfo(lockId, key, expire, timeout, tryCount);
            }
            Thread.sleep(50);
        }
        log.info("lock failed, try {} times", tryCount);
        return null;
    }

    @Override
    public boolean releaseLock(LockInfo lockInfo) {
        Object releaseResult = redisTemplate.execute(SCRIPT_UNLOCK,
                redisTemplate.getStringSerializer(),
                redisTemplate.getStringSerializer(),
                Collections.singletonList(lockInfo.getKey()),
                lockInfo.getLockId());
        return Boolean.valueOf(releaseResult.toString());
    }
    
}
```