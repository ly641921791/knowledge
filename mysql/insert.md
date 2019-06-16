INSERT
-

INSERT ... ON DUPLICATE KEY UPDATE ...

执行时，MySQL会检查插入数据是否存在重复KEY（主键、唯一索引等）错误，若存在，将数据加S锁，然后更新数据，加X锁，最后写入更新

注 ：若数据有多个唯一键，并发时可能产生死锁

