
### 仓库介绍
- **该仓库是SpringBoot开发的后端项目**
- **与之一一对应的前端仓库地址为：https://gitee.com/youzhengjie/security-jwt2-vue-admin**  



用户登录流程


用户退出流程

- 由于Jwt token一旦生成就必须等到过期了才可以注销，所以我们需要提前注销这个jwt token的方案如下：
方案：将过期的token（accessToken和refreshToken）放到Redis中形成一个黑名单（为了减轻redis缓存压力，可以为这个token的redis key设置过期时间，时间可以设置为refreshToken的过期时间），
以后每次验证token就可以先判断这个token在不在redis的黑名单中，如果在则直接验证失败。