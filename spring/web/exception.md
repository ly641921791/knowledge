Spring Web Servlet 异常处理
-

局部异常 ：@Controller中的@ExceptionHandler方法用于处理当前类中的异常

全局异常 ：@ControllerAdvice中的@ExceptionHandler方法用于处理指定包下控制器的异常，默认全局。当与局部异常处理器同时存在，局部异常处理器优先生效

示例代码 ：https://github.com/ly641921791/knowledge-examples/tree/master/spring-web-servlet-example/spring-web-servlet-exception

> 参考文档 https://docs.spring.io/spring/docs/5.1.8.RELEASE/spring-framework-reference/web.html#mvc-ann-exceptionhandler