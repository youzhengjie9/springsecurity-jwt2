import Vue from 'vue'
import App from './App.vue'
import router from './router'
import ElementUI from 'element-ui'
import 'element-ui/lib/theme-chalk/index.css'
import store from './store'
//引入时间格式化插件
import moment from 'moment'
//导入自定义指令directive目录
import directive from './directive'

Vue.config.productionTip = false

//使用vue-router插件
Vue.use(router)
//使用elementui插件
Vue.use(ElementUI)

//定义一个全局过滤器实现日期格式化
Vue.filter('dateformat',function (input,fmtstring) {//当input为时间戳时，需转为Number类型
  // 使用momentjs这个日期格式化类库实现日期的格式化功能
  return moment(input).format(fmtstring);
})

//使用自定义指令
Vue.use(directive)

new Vue({
  el:'#app',
  router,
  store,
  render: h => h(App)
})