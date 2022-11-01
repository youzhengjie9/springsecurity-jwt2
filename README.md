
### 仓库介绍
- **该仓库是SpringBoot开发的后端项目**
- **与之一一对应的前端仓库地址为：https://gitee.com/youzhengjie/security-jwt2-vue-admin**  


### Docker部署后端项目
- 1：将security-jwt2整个项目进行maven打包，并且将security-jwt2-admin模块中的jar包和Dockerfile文件上传到服务器。
```shell
[root@centos7-sql backend]# pwd
/root/backend
[root@centos7-sql backend]# ls
Dockerfile  spring-security-jwt2.jar
[root@centos7-sql backend]# cat Dockerfile
# 基础镜像为openjdk:8
FROM openjdk:8
# 作者信息
MAINTAINER youzhengjie <1550324080@qq.com>
# 将jar包添加到镜像中
ADD spring-security-jwt2.jar backend.jar
# 执行java-jar命令
ENTRYPOINT ["java","-jar","backend.jar"]
```
- 2：使用docker构建项目镜像：
```shell
docker build -t backend:v1.0.0 .
```
- 3:后台运行项目镜像：

```shell
docker run -d -p 8188:8188 backend:v1.0.0
```


### 遇到的部分问题

- 1：mybatis-plus从数据库查询到某条记录的id为1580806331978260451（由雪花算法生成），由于太长了导致传到前端js时这个id就变成1580806331978260500（两个id不一致了），这就是js的长类型的精度丢失问题
  - 一开始以为是mybatis-plus查询的数据有误，找了很久才发现是雪花算法生成的id导致js精度丢失了。（js只保存16位long数据类型，默认将后三位进行四舍五入处理，将该数据返回给后端执行数据库crud影响行数为0；）
  - 解决方式：只需要在User实体类的id属性（因为这个id属性的值是通过雪花算法生成的，很长）上面加上@JsonSerialize(using = ToStringSerializer.class)注解即可。
- 2:mybatis的xml文件不能用<和<=，不然会报错。
  - 解决方式：使用<![CDATA[操作符]]>  , 例如:如果想用<=，则就替换成<![CDATA[<=]]>