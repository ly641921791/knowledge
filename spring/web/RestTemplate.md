RestTemplate
-

RestTemplate是Spring3.0引入的同步阻塞式HTTP客户端，Spring5引入了WebClient作为非阻塞式Reactive HTTP客户端

###### GET示例

```java
public class GetDemo {
    public static void main(String[] args){
        RestTemplate restTemplate = new RestTemplate();
        Object response = restTemplate.getForObject(
                "https://www.google.com?p1={p1}&p2={p2}",
                Object.class,
                "param1","param2"
        );
    }
}
```

###### POST请求提交表单

```java
public class PostWithForm {
    public static void main(String[] args){
        RestTemplate restTemplate = new RestTemplate();
        
        // 请求头
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        
        // 请求参数
        MultiValueMap<String,String> form = new LinkedMultiValueMap<>();
        form.add("p1","param1");
        form.add("p2","param2");
        
        // 请求对象
        HttpEntity request = new HttpEntity(form,headers);
        
        String response = restTemplate.postForObject("https://www.google.com",request,String.class);
    }
}
```

###### POST请求提交JSON

```java
public class PostWithJson {
    public static void main(String[] args){
        RestTemplate restTemplate = new RestTemplate();
        
        // 请求头
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
        
        // 请求参数 可以是任意数据类型
        Object body = new Object();
        
        // 请求对象
        HttpEntity request = new HttpEntity(body,headers);
 
        /*
            直接将body作为参数也可以，默认以application/json;charset=UTF-8形式提交
            String response = restTemplate.postForObject("https://www.google.com",body,String.class)
         */
        String response = restTemplate.postForObject("https://www.google.com",request,String.class);
    }
}
```

下载文件 https://mp.weixin.qq.com/s/Ksp1gYeZMNgSJwZW2AdOBA