import Vue from 'vue'
//导入路由插件
import VueRouter from 'vue-router'
//导入进度条nprogress
import NProgress from 'nprogress'
//导入进度条nprogress样式
import 'nprogress/nprogress.css'
//导入vuex
import store from '../store'
//导入layout公共布局页面
import Layout from '../layout/index.vue'
import {
  initDynamicRouter
 } from '../utils/permission'


//解决Vue路由重复跳转报错,要放到Vue.use(VueRouter)之前
const routerPush = VueRouter.prototype.push;
VueRouter.prototype.push = function (location) {
    return routerPush.call(this, location).catch(err => {})
};

//安装路由插件
Vue.use(VueRouter);

//配置动态路由，这里存放的路由都是静态路由，也就是不管什么用户都一定会有的路由
const router = new VueRouter({
  mode:'history',
  routes: [
    {
      path:'/',
      redirect:'/dashboard', //访问“/”路径时自动重定向到“/dashboard路径”
      // name:'dashboard',
      //Layout我们后台系统的整体布局组件
      component: Layout,
      //dashboard默认路由被我放到后端进行生成
      //解决登陆时，前端因为有些用户没有菜单/路由，而后端getRouterByUserId方法查不到数据导致一直死循环的bug
      children:[]
    },
    {
      path:'/login',
      component: () =>import('../views/login/index.vue')
    },
    {
      path:'/register',
      component: () =>import('../views/register/index.vue')
    },
    {
      path:'/403',
      component: () =>import('../views/error-page/403.vue')
    },
    {
      //配置404未找到页面路由
      path:'*',
      component: () => import('../views/error-page/404.vue')
    }    
  ]
});



//配置全局路由守卫（进入路由前自动执行）
router.beforeEach((to, from, next) => {
  
  //当进入路由前进度条开启
  NProgress.start()
  //如果有accessToken
  if(store.state.user.accessToken){
    //并且请求路由路径是登录页则跳转到首页（防止重复登录）
    if (to.path === '/login') {
      next({path: '/'})
    }
    //如果不是登录页
    else {
      //因为vuex的state会随着页面刷新导致丢失（也就是说vuex数据不会持久化），所以存储在vuex的用户信息会丢失
      //所以当每次路由跳转（刷新页面）同时localstorage又存在着accessToken，就要去后端查询当前用户信息并保存到vuex中
      if (store.state.user.userName.length === 0 || store.state.user.dynamicRouter.length === 0) {
        
        store.dispatch('getCurrentUserInfo').then(res => {
          //初始化动态路由，防止刷新丢失
          initDynamicRouter();
          //成功回调
          // next()
          //确保addRoutes()时动态添加的路由已经被完全加载上去。
          next({ ...to, replace: true })
        }).catch(() => {
          
          next({path: '/login'})
        })
      }
      else {
        // //初始化动态路由，防止刷新丢失
        // console.log('vue router initDynamicRouter2222')
        // initDynamicRouter();

        next();
      }

    }
  }
  //如果没有accessToken
  else{
    //同时如果去的页面不是登录或者注册的话，则打回到登录页面，不允许进入后台系统
    if(to.path !== '/login' && to.path !== '/register'){
      next({path: '/login'})
    }else{ //如果去的页面是登录或者注册页面的话，则放行
      next();
    }
  }

})
//配置全局路由守卫（退出路由后自动执行）
router.afterEach(() => {
  //当退出路由后进度条关闭
  NProgress.done()
})

//导出路由
export default router
