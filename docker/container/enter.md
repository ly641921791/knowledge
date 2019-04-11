进入容器
-

- docker attach [container_name|container_id]

	多个窗口同事使用attach进入容器，会同步显示控制台，若一个窗口阻塞，其他窗口无法操作

- nsenter






- exec  

docker exec -it  569f05d5f4fc /bin/bash  
docker exec -it  569f05d5f4fc /bin/sh  
docker exec -it  569f05d5f4fc bash




基于*openjdk:8-jre-alpine*镜像构建的镜像，sudo docker exec -it  569f05d5f4fc /bin/sh


p209