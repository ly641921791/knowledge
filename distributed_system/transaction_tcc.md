柔性事务 - TCC
-

TCC ：Try-Confirm-Cancel

- Try 业务检查（一致性），预留资源（隔离性）
- Confirm 使用Try预留的执行处理业务，需要满足幂等性
- Cancel 释放预留资源，需要满足幂等性


> 开源实现 https://github.com/changmingxie/tcc-transaction