# 基础镜像为openjdk:8
FROM openjdk:8
# 作者信息
MAINTAINER youzhengjie <1550324080@qq.com>
# 将jar包添加到镜像中
ADD spring-security-jwt2.jar backend.jar
# 执行java-jar命令
ENTRYPOINT ["java","-jar","backend.jar"]