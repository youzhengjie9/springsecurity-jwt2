import Vue from 'vue'
//导入vuex，实现数据（状态）共享
import Vuex from 'vuex'
//引入模块化的vuex
import user from './modules/user'
import menuTag from './modules/menu-tag'
import menu from './modules/menu'
import form from './modules/form'


//使用vuex插件
Vue.use(Vuex);

export default new Vuex.Store({

    //vuex模块化
    modules:{
        //注册模块
        user,
        menuTag,
        menu,
        form
    }

})


