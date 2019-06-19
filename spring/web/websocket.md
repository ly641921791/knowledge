# Websocket

spring-websocket底层通过tomcat实现，性能不足

netty-websocket-spring-boot-starter通过netty实现，具有极高的性能。

boot 2.0.0+

```xml
<dependency>
    <groupId>org.yeauty</groupId>
    <artifactId>netty-websocket-spring-boot-starter</artifactId>
    <version>0.6.3</version>
</dependency>
```

第一步 配置类

```java
@Configuration
public class WebSocketConfig {
    @Bean
    public ServerEndpointExporter serverEndpointExporter() {
        return new ServerEndpointExporter();
    }
}
```

第二步 在端点类上加上@ServerEndpoint、@Component注解，并在相应的方法上加上@OnOpen、@OnClose、@OnError、@OnMessage注解（不想关注某个事件可不添加对应的注解）：

```java
@ServerEndpoint
@Component
public class MyWebSocket {

    @OnOpen
    public void onOpen(Session session, HttpHeaders headers) throws IOException {
        System.out.println("new connection");
    }

    @OnClose
    public void onClose(Session session) throws IOException {
       System.out.println("one connection closed");
    }

    @OnError
    public void onError(Session session, Throwable throwable) {
        throwable.printStackTrace();
    }

    @OnMessage
    public void OnMessage(Session session, String message) {
        System.out.println(message);
        session.sendText("Hello Netty!");
    }
}
```

第三步 测试页

```html
<!DOCTYPE html>
<html lang="en">
<body>
<div id="msg"></div>
<input type="text" id="text">
<input type="submit" value="send" onclick="send()">
</body>
<script>
    var msg = document.getElementById("msg");
    var wsServer = 'ws://127.0.0.1:80';
    var websocket = new WebSocket(wsServer);
    //监听连接打开
    websocket.onopen = function (evt) {
        msg.innerHTML = "The connection is open";
    };

    //监听服务器数据推送
    websocket.onmessage = function (evt) {
        msg.innerHTML += "<br>" + evt.data;
    };

    //监听连接关闭
    websocket.onclose = function (evt) {
        alert("连接关闭");
    };

    function send() {
        var text = document.getElementById("text").value
        websocket.send(text);
    }
</script>
</html>
```