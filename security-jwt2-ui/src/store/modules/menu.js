
const state = {

    //通过菜单的index（我们的index是路由路径）来控制当前elementui侧边导航高亮index
    currentNavMenuActiveIndex: '/dashboard'

}

const mutations = {

    //将currentNavMenuActiveIndex替换成新的侧边导航高亮的菜单index（我们的index是路由路径）
    SET_CURRENT_NAVMENU_ACTIVE_INDEX(state,newCurrentNavMenuActiveIndex){
        state.currentNavMenuActiveIndex=newCurrentNavMenuActiveIndex;
    }

}

const actions = {

    //将currentNavMenuActiveIndex替换成新的侧边导航高亮的菜单index（我们的index是路由路径）
    setCurrentNavMenuActiveIndex(context,newCurrentNavMenuActiveIndex){
        context.commit('SET_CURRENT_NAVMENU_ACTIVE_INDEX',newCurrentNavMenuActiveIndex);
    }

}


export default {

    state,
    mutations,
    actions

}