锁
- 

### 语言层面的锁

Java的synchronized、Lock都是单机锁，集群环境下失效

### 分布式锁

#### 基于Mysql的分布式锁

方案一 唯一性约束

通过锁名的唯一性约束，保证同时只有一个操作可以成功

```mysql
-- 创建锁表
CREATE TABLE locks (
  id INT NOT NULL AUTO_INCREMENT,
  lock_name VARBINARY(64) NOT NULL COMMENT '锁名',
  other_column VARBINARY(64) COMMENT '其他字段',
  PRIMARY KEY (id),
  UNIQUE KEY (lock_name)
);
-- 加锁
INSERT INTO locks VALUES ('lock_name');
-- 解锁
DELETE FROM locks WHERE lock_name = 'lock_name';
```

缺点：重量级锁，加锁需要重复执行插入，不能自动释放锁

方案二 行锁

通过`行锁`解决方案一加锁需要重复执行插入的问题，当提交或回滚锁自动释放

```mysql
-- 在数据库中预先插入锁
INSERT INTO locks VALUES ('lock_name');
-- 加锁
SELECT * FROM locks WHERE lock_name = 'lock_name' FOR UPDATE;
```

缺点：重量级锁，一个线程加锁时间过长，会导致其他线程阻塞

方案三 CAS

CAS（Compare And Swap）实现加锁

下面是扣减库存操作的伪代码，更新失败则重复执行`查询-扣减-更新`操作直到库存不足

```
// 商品id 扣减数量
int productId = 1 , saleNum = 10;
// 查询库存
int oldNum = execute("SELECT num FROM stock WHERE product_id = #{productId}");
// 扣减库存，校验略过
int newNum = oldNum - saleNum;
// 更新库存
int updateCount = execute("UPDATE stock SET num = #{newNum} WHERE product_id = #{productId} AND num = #{oldNum}");
```

缺点：并发高的情况下，执行SQL的次数过多

#### 基于Redis的分布式锁

与MySQL锁同理，但是操作内存更快，且可以设置锁的过期时间

