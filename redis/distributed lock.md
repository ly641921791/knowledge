## Redis 实现分布式锁

本文介绍如何使用Redis实现分布式锁

对于每个锁，最好有一个唯一id，保证不会错误解锁。（例如 ：A锁与B锁的key相同，在A锁过期的一瞬间，B锁进行解锁，若不校验锁id，会导致A锁被解锁）

Redis提供了[SETNX](https://redis.io/commands/setnx)（set if not exists），仅在key不存在时插入value

Redis在2.6.12版本提供了[SET](https://redis.io/commands/set)函数的重载，支持仅在key不存在时插入带有过期时间的value

虽然Redis没有提供仅在value相同时删除的命令，但是在2.6.0版本提供了[EXAL](https://redis.io/commands/eval)用于执行脚本，通过该脚本可以
仅在value相同时删除这一功能

下面介绍SpringBoot实现分布式锁

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
```

锁实现过程

```java
public class Lock {
    
    /**
    * 由于RedisTemplate没有提供SET命令的重载，需要通过执行脚本使用该命令
    * 
    * 也可以使用Jedis客户端方法Redis，该客户端提供了SET命令的重载
	*/
    private static final RedisScript<String> SCRIPT_LOCK = new DefaultRedisScript<>("return redis.call('set',KEYS[1],ARGV[1],'NX','PX',ARGV[2])", String.class);
    private static final RedisScript<String> SCRIPT_UNLOCK = new DefaultRedisScript<>("if redis.call('get',KEYS[1]) == ARGV[1] then return tostring(redis.call('del', KEYS[1])==1) else return 'false' end", String.class);
    
    @Autowired
    private RedisTemplate redisTemplate;
 
    /**
    * 尝试加锁
	* @param key 锁key   
	* @param expire 锁存在时间
	* @param timeout 加锁超时时间
	* @return 锁信息
	*/
    @Override
    public LockInfo tryLock(String key, long expire, long timeout) throws Exception {
        long start = System.currentTimeMillis();
        int tryCount = 0;
        String lockId = UUID.randomUUID().toString();
        while (System.currentTimeMillis() - start < timeout) {
            Object lockResult = redisTemplate.execute(SCRIPT_LOCK,
                    redisTemplate.getStringSerializer(),
                    redisTemplate.getStringSerializer(),
                    Collections.singletonList(key),
                    lockId, String.valueOf(expire));
            tryCount++;
            if ("OK".equals(lockResult)) {
                return new LockInfo(lockId, key, expire, timeout, tryCount);
            }
            Thread.sleep(50);
        }
        log.info("lock failed, try {} times", tryCount);
        return null;
    }

	/**
	* 解锁
	* @param lockInfo 锁信息
	* @return 解锁结果
	*/
    @Override
    public boolean unLock(LockInfo lockInfo) {
        Object releaseResult = redisTemplate.execute(SCRIPT_UNLOCK,
                redisTemplate.getStringSerializer(),
                redisTemplate.getStringSerializer(),
                Collections.singletonList(lockInfo.getKey()),
                lockInfo.getLockId());
        return Boolean.valueOf(releaseResult.toString());
    }
    
}
```

锁使用过程

```java
public class Controller {
    
    @Autowired
    private Lock lock;
    
    public void postForm(Object formData){
        LockInfo lockInfo = null;
        try{
			lockInfo = lock.tryLock("key",3000L,3000L);
			if (lockInfo != null){
				// 业务处理 略
			}
        }finally{
            if (lockInfo != null){
                lock.unlock(lockInfo);
            }
        }
    }
}
```
