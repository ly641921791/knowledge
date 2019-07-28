Web攻击及解决方案
-

###### SQL注入

预编译

###### XSS（cross-site scripting 跨域脚本攻击）

特点：在目标网站执行非目标网站脚本

Reflected XSS 反射型

特点：在URL地址或参数中加入特定的<script>脚本，服务器处理后，<script>脚本出现在浏览器，被浏览器执行

Stored XSS 持久型

特点：XSS攻击代码存储在服务器，每一个浏览特定网页的人都被攻击。常见于留言、评论、博客等内容类应用

DOM-base XSS 基于DOM的XSS

特点：基于DOM的XSS，可能是反射型也可能是持久型，通过在DOM树下生成节点引起攻击；还有wifi，拦截用户请求，得到用户数据，或修改响应页面，注入脚本

解决：前后端将内容中的标签和属性都进行转义；展示时，前端对特殊字段进行HTML编码；开启https解决wifi流量劫持

###### CSRF（cross site request forgery 跨域请求伪造）

用户本地存储了用户Cookie，在攻击者伪造的页面利用cookie跳过了身份验证

特点：在攻击者提供的页面，利用本地的cookie执行了请求引起的攻击

解决：使用token认真、服务端检查请求头Referer

###### DDOS

大量请求攻击导致服务器瘫痪


https://blog.tonyseek.com/post/introduce-to-xss-and-csrf/#id4
https://www.ibm.com/developerworks/cn/web/1102_niugang_csrf/index.html


> 《白帽子讲Web安全》
>
> http://www.cnblogs.com/digdeep/p/4695348.html
>
> https://www.cnblogs.com/lovesong/p/5223989.html
