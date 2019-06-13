RestTemplate
-

GET请求带参数

```java
public class getWithParam {
    public static void main(String[] args){
        RestTemplate restTemplate = new RestTemplate();
        String response = restTemplate.getForObject(
                "http://www.baidu.com?p1={p1}&p2={p2}",
                String.class,
                "param1","param2"
        );
    }
}
```

POST请求带表单

```java
public class postWithForm {
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
        
        String response = restTemplate.postForObject("http://www.baidu.com",request,String.class);
    }
}
```