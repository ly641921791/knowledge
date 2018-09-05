Dockerfile构建Docker镜像

FROM nginx
RUN echo '<h1>hello docker</h1>' > /usr/share/nginx/html/index.html

echo：输出指定文字
>：覆盖或创建指定文件
>>：创建或追加内容到指定文件


