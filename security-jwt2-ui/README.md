
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
