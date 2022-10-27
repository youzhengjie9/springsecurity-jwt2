
### 仓库介绍
- **该仓库是SpringBoot开发的后端项目**
- **与之一一对应的前端仓库地址为：https://gitee.com/youzhengjie/security-jwt2-vue-admin**  



用户登录流程


用户退出流程

- 由于Jwt token一旦生成就必须等到过期了才可以注销，所以我们需要提前注销这个jwt token的方案如下：
方案：将过期的token（accessToken和refreshToken）放到Redis中形成一个黑名单（为了减轻redis缓存压力，可以为这个token的redis key设置过期时间，时间可以设置为refreshToken的过期时间），
以后每次验证token就可以先判断这个token在不在redis的黑名单中，如果在则直接验证失败。



## 遇到的问题

- 1：mybatis-plus从数据库查询到某条记录的id为1580806331978260451（由雪花算法生成），由于太长了导致传到前端js时这个id就变成1580806331978260500（两个id不一致了），这就是js的长类型的精度丢失问题
  - 一开始以为是mybatis-plus查询的数据有误，找了很久才发现是雪花算法生成的id导致js精度丢失了。（js只保存16位long数据类型，默认将后三位进行四舍五入处理，将该数据返回给后端执行数据库crud影响行数为0；）
  - 解决方式：只需要在User实体类的id属性（因为这个id属性的值是通过雪花算法生成的，很长）上面加上@JsonSerialize(using = ToStringSerializer.class)注解即可。
- 2:mybatis的xml文件不能用<和<=，不然会报错。
  - 解决方式：使用<![CDATA[操作符]]>  , 例如:如果想用<=，则就替换成<![CDATA[<=]]>