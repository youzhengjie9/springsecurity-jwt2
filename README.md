
### 项目介绍
- security-jwt2-admin是后端模块
- security-jwt2-ui是前端模块


## 后端模块

### Docker部署后端项目
- 1：将security-jwt2整个项目进行maven打包，并且将security-jwt2-admin模块中的jar包和Dockerfile文件上传到服务器。（项目jar包和Dockerfile文件必须在同一个目录下）

![2.png](https://gitee.com/youzhengjie/springsecurity-jwt2/raw/main/security-jwt2-doc/images/2.png)


- 2：使用docker构建项目镜像：
```shell
docker build -t backend:v1.0.0 .
```
- 3:后台运行项目镜像：

```shell
docker run -d -p 8188:8188 backend:v1.0.0
```



### 项目难点和遇到的问题



#### 性能优化⭐



##### MySQL将13w条数据迁移到ElasticSearch优化过程



###### 版本1(平均耗时:15140ms)

- **从mysql中导入操作日志到es。版本1:直接从数据库拉取全部数据，使用es的bulk一直一次性导入进去：**
    - **第一次耗时：17940ms**
    - **第二次耗时：14261ms**
    - **第三次耗时：13221ms**
    - **平均耗时：15140ms**

```java
@Test
    public void importOperationLog2Es01() throws IOException {

        long start = System.currentTimeMillis();

        BulkRequest bulkRequest = new BulkRequest();

        //查询所有操作日志记录
        List<OperationLog> operationLogList = operationLogService.lambdaQuery().list();

        for (OperationLog operationLog : operationLogList) {

            IndexRequest indexRequest = new IndexRequest(OPER_LOG_INDEX);
            indexRequest.id(operationLog.getId().toString());

            Map<String, Object> sources = new ConcurrentHashMap<>();
            sources.put("username", operationLog.getUsername());
            sources.put("type", operationLog.getType());
            sources.put("uri", operationLog.getUri());
            sources.put("time", operationLog.getTime());
            sources.put("ip", operationLog.getIp());
            sources.put("address",operationLog.getAddress());
            sources.put("browser", operationLog.getBrowser());
            sources.put("os", operationLog.getOs());
            sources.put("operTime", operationLog.getOperTime());
            sources.put("delFlag", operationLog.getDelFlag());
            indexRequest.source(sources);

            bulkRequest.add(indexRequest);
        }

        restHighLevelClient.bulk(bulkRequest,RequestOptions.DEFAULT);

        long end = System.currentTimeMillis();
        System.out.println((end-start)+"ms");
    }
```



###### 版本2(平均耗时：10340ms)

- **版本2：采用线程池（core=10，max=20的配置）+分页（大小1000）**
    - **第一次耗时：10334ms**
    - **第二次耗时：10034ms**
    - **第三次耗时：10653ms**
    - **平均耗时：10340ms**

```java
@Test
    public void importOperationLog2Es02() throws IOException, InterruptedException {

        long start = System.currentTimeMillis();

        ThreadPoolExecutor threadPoolExecutor
                = new ThreadPoolExecutor(10,
                20,
                2L,
                TimeUnit.SECONDS,
                new LinkedBlockingDeque<>(),
                Executors.defaultThreadFactory(),
                new ThreadPoolExecutor.AbortPolicy());

        //数据总数
        long count = operationLogService.count();
        //每一页大小。
        int size= 1000;
        //循环次数。如果count=132760（也就是说不是整数，则循环次数+1）
        long circle=(count%size==0)?count/size : (count/size)+1;

        //JUC包下的倒计时器
        CountDownLatch countDownLatch = new CountDownLatch((int)circle);

        for (long i = 1; i < circle+1; i++) {

            int page= (int) ((i-1)*size);

            threadPoolExecutor.submit(()->{

                try {

                    List<OperationLog> operationLogList = operationLogMapper.selectAllOperationLogByLimit(page, size);

                    BulkRequest bulkRequest = new BulkRequest();
                    for (OperationLog operationLog : operationLogList) {

                        IndexRequest indexRequest = new IndexRequest(OPER_LOG_INDEX);
                        indexRequest.id(operationLog.getId().toString());

                        Map<String, Object> sources = new ConcurrentHashMap<>();
                        sources.put("username", operationLog.getUsername());
                        sources.put("type", operationLog.getType());
                        sources.put("uri", operationLog.getUri());
                        sources.put("time", operationLog.getTime());
                        sources.put("ip", operationLog.getIp());
                        sources.put("address",operationLog.getAddress());
                        sources.put("browser", operationLog.getBrowser());
                        sources.put("os", operationLog.getOs());
                        sources.put("operTime", operationLog.getOperTime());
                        sources.put("delFlag", operationLog.getDelFlag());

                        indexRequest.source(sources);

                        bulkRequest.add(indexRequest);
                    }

                    restHighLevelClient.bulk(bulkRequest,RequestOptions.DEFAULT);

                }catch (Exception e){
                    e.printStackTrace();
                }
                finally {
                    //计数器 -1
                    countDownLatch.countDown();
                }

            });

        }

        //阻塞线程，只有当计数器=0时才会解除阻塞。防止主线程执行完了，子线程还没有执行完就终止了该程序的运行。
        countDownLatch.await();
        //终止时间
        long end = System.currentTimeMillis();

        System.out.println((end-start)+"ms");

    }
```





##### MySQL的分页查询越到后面性能越低⭐

- 分页参数为：LIMIT 130000,7

    - **优化之前：**

  ```sql
  	SELECT id, username, type, uri, time, ip, address, browser, os, oper_time,del_flag
          FROM sys_oper_log
          WHERE del_flag = 0
          ORDER BY id DESC
          LIMIT 130000,7
  ```

    - **优化之后：**

  ```sql
  SELECT id, username, type, uri, time, ip, address, browser, os, oper_time,del_flag
          FROM sys_oper_log
          WHERE id <![CDATA[<=]]> (SELECT id FROM sys_oper_log ORDER BY id DESC LIMIT 130000,1)
          AND del_flag = 0
          ORDER BY id DESC
          LIMIT 7
  ```



- **在分页参数都相同的情况下，上面两个SQL的性能差距十分大。原因是后者的子查询利用了主键索引提高性能，避免了常规分页查询的弊端（也就是从头到尾的全表扫描），如果在数据量更大的情况下，上面的两个SQL性能差距会拉的十分大。**





###### 分页查询&&按照id倒序排序（注意）⭐

- **增强版分页并倒序查询，下面一定要有WHERE id <=（倒序子查询）+后面的ORDER BY id DESC，不然会分页不一致**

```sql
SELECT id, username, type, uri, time, ip, address, browser, os, oper_time,del_flag
        FROM sys_oper_log
        WHERE id <![CDATA[<=]]> (SELECT id FROM sys_oper_log ORDER BY id DESC LIMIT 130000,1)
        AND del_flag = 0
        ORDER BY id DESC
        LIMIT 7
```



###### 分页查询&&按照id升序排序

- 如果想要增强版分页并顺序查询，就要改成WHERE id >=（顺序子查询）+后面的ORDER BY id ASC

```sql
SELECT
        sys_user.id,
        sys_user.user_name,
        sys_user.nick_name,
        sys_user.status,
        sys_user.avatar,
        sys_user.email,
        sys_user.phone,
        sys_user.sex,
        sys_user.create_time,
        sys_user.update_time,
        sys_user.del_flag
        FROM sys_user
        WHERE id >= (select id from sys_user ORDER BY id limit 100,1)
        AND del_flag = 0
        ORDER BY id ASC   
        limit 7
```





#### Bug修复⭐



##### ElasticSearch分页查询第10000条以上的记录报错问题



- 报错内容：

```java
Caused by: ElasticsearchException[Elasticsearch exception [type=illegal_argument_exception, reason=Result window is too large, from + size must be less than or equal to: [10000] but was [132734]. See the scroll api for a more efficient way to request large data sets. This limit can be set by changing the [index.max_result_window] index level setting.]]; nested: ElasticsearchException[Elasticsearch exception [type=illegal_argument_exception, reason=Result window is too large, from + size must be less than or equal to: [10000] but was [132734]. See the scroll api for a more efficient way to request large data sets. This limit can be set by changing the [index.max_result_window] index level setting.]];
```



- ElasticSearch的分页查询默认只能查看到第10000条以内的记录，因为分页查询越到后面越吃性能，所以我们可以在linux系统上依次执行下面的命令（**记得把http://192.168.184.123:9200/operation-log-index/_settings）修改成你自己的es的ip地址、端口号、索引名称**。

```shell
curl -H "Content-Type: application/json" -XPUT http://192.168.184.123:9200/operation-log-index/_settings -d '{"index.blocks": {"read_only_allow_delete": null}}'
```

```shell
curl -H "Content-Type: application/json" -XPUT http://192.168.184.123:9200/operation-log-index/_settings -d '{"index.max_result_window":"50000000"}'
```





##### springboot+canal监听实体类的日期格式报错

- **出现下面这个异常：**

```java
Can not set java.time.LocalDateTime field com.boot.entity.OperationLog.operTime to null value
```

- **原因：（这是由于canal+SpringBoot中的StringConvertUtil类的源码的问题）：**
    - 可以看到下面的源码没有对LocalDateTime转LocalDatetime进行处理，而是把LocalDateTime转为String类型进行返回（问题出在`type.equals(java.sql.Date.class) ? parseDate(columnValue) : columnValue`）,而columnValue是String类型，所以我们可以用String类型去接收LocalDateTime类型。



```java
static Object convertType(Class<?> type, String columnValue) {
        if (columnValue == null) {
            return null;
        } else if (type.equals(Integer.class)) {
            return Integer.parseInt(columnValue);
        } else if (type.equals(Long.class)) {
            return Long.parseLong(columnValue);
        } else if (type.equals(Boolean.class)) {
            return convertToBoolean(columnValue);
        } else if (type.equals(BigDecimal.class)) {
            return new BigDecimal(columnValue);
        } else if (type.equals(Double.class)) {
            return Double.parseDouble(columnValue);
        } else if (type.equals(Float.class)) {
            return Float.parseFloat(columnValue);
        } else if (type.equals(Date.class)) {
            return parseDate(columnValue);
        } else {
            return type.equals(java.sql.Date.class) ? parseDate(columnValue) : columnValue;
        }
    }
```



- **解决办法有两个：**

- **（1）第一种办法：**



- 1：创建一个中转类（也就是说我们报错的原因是OperationLog类中的LocalDateTime属性，这个时候我们可以创建一个新的类，其他字段不变，把这个LocalDateTime日期类变成String就可以解决这个bug）

```java
package com.boot.entity;

import com.alibaba.excel.annotation.ExcelProperty;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.boot.converter.DelFlagConverter;
import com.boot.converter.LocalDateTimeConverter;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import java.io.Serializable;
import java.time.LocalDateTime;


/**
 * 操作日志canal中转类。解决OperationLog类中的LocalDatetime类型的字段无法被canal接收导致报错
 *
 * @author youzhengjie
 * @date 2022/10/30 21:16:54
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@Builder
public class OperationLogCanal implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;

    private String username;

    private String type;

    private String uri;

    private String time;

    private String ip;

    private String address;

    private String browser;

    private String os;

    //canal+springboot当属性名和数据库字段不一致时，要用@Column去指定数据库字段名，否则会接收不到canal数据
    @Column(name = "oper_time")
    private String operTime;

    //canal+springboot当属性名和数据库字段不一致时，要用@Column去指定数据库字段名，否则会接收不到canal数据
    @Column(name = "del_flag")
    private Integer delFlag;

}
```

- 2：OperationLogService

```java
package com.boot.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.boot.entity.OperationLog;

import java.util.List;

/**
 * 操作日志服务
 *
 * @author youzhengjie
 * @date 2022/10/21 23:32:14
 */
public interface OperationLogService extends IService<OperationLog> {

   
    long selectAllOperationLogCount();

    /**
     * 添加操作日志到elasticsearch
     *
     * @param operationLog 操作日志
     * @return boolean
     */
    boolean addOperationLogToEs(OperationLog operationLog);

    /**
     * 根据id删除elasticsearch中的操作日志
     *
     * @param id id
     * @return boolean
     */
    boolean deleteOperationLogToEs(Long id);


    /**
     * 更新elasticsearch中的操作日志
     *
     * @param operationLog 操作日志
     * @return boolean
     */
    boolean updateOperationLogToEs(OperationLog operationLog);

}
```

- 3：OperationLogServiceImpl

```java
package com.boot.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.boot.entity.OperationLog;
import com.boot.mapper.OperationLogMapper;
import com.boot.service.OperationLogService;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.core.CountRequest;
import org.elasticsearch.client.core.CountResponse;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 操作日志服务impl
 *
 * @author youzhengjie
 * @date 2022/10/21 23:42:49
 */
@Service
@Slf4j
public class OperationLogServiceImpl extends ServiceImpl<OperationLogMapper, OperationLog> implements OperationLogService {

    @Autowired
    private OperationLogMapper operationLogMapper;

    @Autowired
    private RestHighLevelClient restHighLevelClient;

    /**
     * 操作日志的es索引
     */
    private static final String OPER_LOG_INDEX="operation-log-index";

    @Override
    public boolean addOperationLogToEs(OperationLog operationLog) {

        try {
            IndexRequest indexRequest = new IndexRequest(OPER_LOG_INDEX);
            indexRequest.id(operationLog.getId().toString());

            Map<String, Object> sources = new ConcurrentHashMap<>();
            sources.put("username", operationLog.getUsername());
            sources.put("type", operationLog.getType());
            sources.put("uri", operationLog.getUri());
            sources.put("time", operationLog.getTime());
            sources.put("ip", operationLog.getIp());
            sources.put("address",operationLog.getAddress());
            sources.put("browser", operationLog.getBrowser());
            sources.put("os", operationLog.getOs());
            sources.put("operTime", operationLog.getOperTime());
            sources.put("delFlag", operationLog.getDelFlag());

            indexRequest.source(sources);

            restHighLevelClient.index(indexRequest,RequestOptions.DEFAULT);
            return true;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }

    }

    @Override
    public boolean deleteOperationLogToEs(Long id) {

        try {
            DeleteRequest deleteRequest = new DeleteRequest(OPER_LOG_INDEX);
            deleteRequest.id(id.toString());
            restHighLevelClient.delete(deleteRequest,RequestOptions.DEFAULT);
            return true;
        }catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean updateOperationLogToEs(OperationLog operationLog) {

        try {
            //将operationLog封装成Map
            Map<String,Object> operationLogMap=new ConcurrentHashMap<>();
            //将operationLog拷贝到Map中
            BeanUtil.copyProperties(operationLog,operationLogMap);
            //把map中的id去掉
            operationLogMap.remove("id");

            String idStr = operationLog.getId().toString();
            UpdateRequest updateRequest = new UpdateRequest(OPER_LOG_INDEX,idStr);
            updateRequest.doc(operationLogMap);
            restHighLevelClient.update(updateRequest,RequestOptions.DEFAULT);
            return true;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }

    }

}
```



- **4：创建一个canal监听类（非常核心），指定监听一个表（这里我监听的是sys_oper_log表）：**

```java
package com.boot.canal;

import cn.hutool.core.bean.BeanUtil;
import com.boot.entity.OperationLog;
import com.boot.entity.OperationLogCanal;
import com.boot.service.OperationLogService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import top.javatool.canal.client.annotation.CanalTable;
import top.javatool.canal.client.handler.EntryHandler;

/**
 * 操作日志canal处理器
 *
 * @author youzhengjie
 * @date 2022/10/30 15:19:09
 */
@CanalTable("sys_oper_log") //@CanalTable("sys_oper_log")：指定canal监听的表名为sys_oper_log
@Component
@Slf4j
public class OperationLogCanalHandle implements EntryHandler<OperationLogCanal> {

    @Autowired
    private OperationLogService operationLogService;

    @Override
    public void insert(OperationLogCanal operationLogCanal) {

        //编写mysql和缓存同步的逻辑（例如JVM本地缓存、Redis分布式缓存、es等）
        OperationLog operationLog = new OperationLog();
        //bean拷贝
        BeanUtil.copyProperties(operationLogCanal,operationLog);
        //同步到es中
        operationLogService.addOperationLogToEs(operationLog);
        log.warn("OperationLogCanalHandle->insert->开始同步->"+operationLog);

    }

    /**
     * 更新
     *
     * @param before 之前
     * @param after  之后
     */
    @Override
    public void update(OperationLogCanal before, OperationLogCanal after) {

        //编写mysql和缓存同步的逻辑（例如JVM本地缓存、Redis分布式缓存、es等）
        OperationLog operationLog = new OperationLog();
        //注意：要拷贝after对象，这个对象是修改之后的对象
        BeanUtil.copyProperties(after,operationLog);
        //同步es
        operationLogService.updateOperationLogToEs(operationLog);
        log.warn("OperationLogCanalHandle->update->开始同步->"+operationLog);

    }

    @Override
    public void delete(OperationLogCanal operationLogCanal) {
        //编写mysql和缓存同步的逻辑（例如JVM本地缓存、Redis分布式缓存、es等）
        Long id = operationLogCanal.getId();
        //同步es
        operationLogService.deleteOperationLogToEs(id);
        log.warn("OperationLogCanalHandle->delete->开始同步->"+id);
    }
}
```





- **（2）第二种办法：（省略）**
    - 修改上面StringConvertUtil类中的convertType方法，这种办法就是通过修改核心源码进而修复这个bug。





##### 雪花算法id引起js精度丢失问题

- **mybatis-plus从数据库查询到某条记录的id为1580806331978260451（由雪花算法生成），由于太长了导致传到前端js时这个id就变成1580806331978260500（两个id不一致了），这就是js的长类型的精度丢失问题**
    - 一开始以为是mybatis-plus查询的数据有误，找了很久才发现是雪花算法生成的id导致js精度丢失了。（js只保存16位long数据类型，默认将后三位进行四舍五入处理，将该数据返回给后端执行数据库crud影响行数为0；）
    - **解决方式：只需要在User实体类的id属性（因为这个id属性的值是通过雪花算法生成的，很长）上面加上@JsonSerialize(using = ToStringSerializer.class)注解即可。**



##### mybatis的xml文件不能用<和<=

- mybatis的xml文件不能用<和<=，不然会报错。
    - 解决方式：使用<![CDATA[操作符]]>  , 例如:如果想用<=，则就替换成<![CDATA[<=]]>





#### 部分较难的业务逻辑⭐



##### 根据用户的id动态构建后台管理系统侧边栏菜单⭐



```java
@Override
    public String buildTreeByUserId(long userid){
        try {
            //查询所有菜单
            List<Menu> allMenu = menuService.getMenuListByUserId(userid);
            //根节点
            List<Menu> rootMenu = new ArrayList<Menu>();
            for (Menu nav : allMenu) {
                if(nav.getParentId()==0){//父节点是0的，为根节点。
                    rootMenu.add(nav);
                }
            }
            /* 根据Menu类的order排序 */
            Collections.sort(rootMenu);

            //为根菜单设置子菜单，getClild是递归调用的
            for (Menu nav : rootMenu) {
                /* 获取根节点下的所有子节点 使用getChild方法*/
                List<Menu> childList = getChild(nav.getId(), allMenu);
                nav.setChildren(childList);//给根节点设置子节点
            }
            Menu dashboardMenu = Menu.builder()
                    .id(-66L)
                    .menuName("仪表盘")
                    .path("/dashboard")
                    .icon("el-icon-s-home")
                    .component("../views/dashboard/index")
                    .children(new ArrayList<>())
                    .build();
            rootMenu.add(0,dashboardMenu);

            return JSON.toJSONString(rootMenu);
        } catch (Exception e) {
            return null;
        }
    }

private List<Menu> getChild(long id,List<Menu> allMenu){
        //子菜单
        List<Menu> childList = new ArrayList<Menu>();
        for (Menu nav : allMenu) {
            // 遍历所有节点，将所有菜单的父id与传过来的根节点的id比较
            //相等说明：为该根节点的子节点。
            if(nav.getParentId().equals(id)){
                childList.add(nav);
            }
        }
        //递归
        for (Menu nav : childList) {
            nav.setChildren(getChild(nav.getId(), allMenu));
        }
        Collections.sort(childList);//排序

        //如果节点下没有子节点，返回一个空List（递归退出）
        if(childList.size() == 0){
            return new ArrayList<Menu>();
        }
        return childList;
    }
```


## 前端模块


### 精选遇到的bug的解决方案

- 1：menu-list/index.vue中的所属菜单input框使用v-model="this.selectNode.menuName"进行绑定，但是用axios请求后端拿到数据返回给前端，并且更新this.selectNode.menuName，但是不会即可生效，需要退出这个dialog才能生效。（也就是说v-model并不是真正意义上的双向绑定，并不是实时同步数据的）

错误案例：使用这种方法不会让v-model实时更新
this.selectNode.menuName=res.data.data
正确案例：
this.selectNode.menuName=res.data.data
this.$forceUpdate() //强制刷新v-model

- 2：我在permission.js中需要用到vuex数据，我使用的是this.$store.state.user.dynamicRouter获取，发现一直获取不到、一直报错。原因是只有vue文件才可以使用这种方式获取。

解决方法：
使用store._modules.root._children.user.state.dynamicRouter获取dynamicRouter。


- 3：permission.js中使用动态路由component:()=>import('..'+menu.component)报错vue-router.esm.js?3423:2316 Error: Cannot find module '../views/role-list/index'。原因是webpack4不支持变量方式的动态 import 引用，新版本的使用require() 。

解决方法：（注意：下面的常量前缀必须是'../views'，不能把/view写到component里面，不然一样会找不到模块）
component:resolve => require(['../views'+menu.component],resolve)

- 4：动态路由刷新丢失出现404页面问题：
  解决办法：在router中router.beforeEach方法，如果store.state.user.dynamicRouter.length === 0，则执行initDynamicRouter()添加动态路由方法，最重要的是后面一定要加上next({ ...to, replace: true }),确保addRoutes()时动态添加的路由已经被完全加载上去。


## 基本插件安装

### 安装vue-router
```
npm i vue-router@3
```

### 安装vuex
```
npm i vuex@3
```

### 安装axios和vue-axios
```
npm install --save axios vue-axios
```

### 安装element-ui
```
npm i element-ui -S
```

### 安装less
```
npm i less less-loader@7
```

### 安装js-cookie
```
npm install js-cookie --save
```

### 安装vue-fragment
```
npm install vue-fragment --save
```


### 安装nprogress进度条的插件
```
npm i -S nprogress
```

### 安装echarts
```
npm install echarts --save
```


## Project setup
```
npm install
```

### Compiles and hot-reloads for development
```
npm run serve
```

### Compiles and minifies for production
```
npm run build
```

### Lints and fixes files
```
npm run lint
```

### Customize configuration
See [Configuration Reference](https://cli.vuejs.org/config/).




