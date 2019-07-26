RESTful
-

默认情况下，前端通过Ajax发送DELETE和PUT请求时，这两种请求参数处理与GET和POST不同的，这种情况会导致后端取不到参数。

解决方案1 将参数存入body，通过@RequestBody注解获得参数

解决方案2 HiddenHttpMethodFilter

对于DELETE和PUT请求，前端发送POST请求，通过添加参数"_method"传递真正的请求方式，如下

``` js
$.ajax({
    url:"",
    type:"POST",
    data:{
        "id" : id ,
        "_method" : "PUT"
    },
    success:function(result){
    }
});
```

后端通过添加过滤器HiddenHttpMethodFilter，通过过滤器处理真实的请求类型，如下

``` java
    @Bean
    public FilterRegistrationBean hiddenHttpMethodFilter() {
        FilterRegistrationBean registrationBean = new FilterRegistrationBean();
        registrationBean.setFilter(new HiddenHttpMethodFilter());
        return registrationBean;
    }
```

将HiddenHttpMethodFilter直接作为Bean放入容器也可以，通过FilterRegistrationBean可以更加灵活的配置过滤器的其他属性