


@CacheEvent


[](https://www.jianshu.com/p/e53c1b60c6e1)





###### 解决方案：强转异常

使用热部署插件时，使用了不同的类加载器加载修改前后的代码，导致同一个类的类加载器不同，引起强转失败，关闭热部署插件