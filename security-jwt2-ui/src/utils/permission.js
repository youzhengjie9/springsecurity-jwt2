//导入路由
import router from "@/router";
//导入layout公共布局页面
import Layout from '../layout/index.vue'
import store from '../store/index'

//初始化动态路由方法(重复的路由会被覆盖)
export function initDynamicRouter(){
    //根路由，通过这个root路由对象找到router/index.js的对应路由并添加上去
    let root={
        path:'/',
        redirect:'/dashboard', //访问“/”路径时自动重定向到“/dashboard路径”
        //Layout我们后台系统的整体布局组件
        component: Layout,
        children:[]
      }


      //注意：只有vue文件才可以使用this.$store.state.user.dynamicMenu获取vuex数据
      //普通的js文件可以使用store._modules.root._children.xxx模块.state.属性名获取
      
      let dynamicRouterList=store._modules.root._children.user.state.dynamicRouter

      
      //数据库中查到该用户有动态路由才进行遍历
      if(dynamicRouterList.length>0){
        
        dynamicRouterList.forEach(menu => {
          //封装动态路由对象
          let routerObj={
            path:menu.path,
            //巨坑：这里的常量前缀必须是'../views'，不能把/view写到component里面，不然一样会找不到模块
            component:resolve => require(['../views'+menu.component],resolve)
          }
          //把这个路由放到root中
          root.children.push(routerObj);
  
        });
        
      }

      //---最后使用动态路由最核心的方法。直接把root路由放进去即可---
      router.addRoute(root);

}

