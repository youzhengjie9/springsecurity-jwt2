import Cookie from 'js-cookie'


const state= {

    isCollapse: false,
        //菜单标签集合。也就是保存面包屑和菜单标签所需要的数据
    MenuTagList: [
            { //dashboard是面包屑和菜单标签默认的数据
                path: '/dashboard',
                component: '../views/dashboard/index',
                menuName: '仪表盘',
                icon: 'el-icon-s-home'
            }
    ],
        //当前高亮的菜单标签,为null表示默认的tag（仪表盘）高亮
    currentMenuTag: null

}

const mutations = {

    // 控制伸缩菜单栏
    COLLAPSE_MENU(state) {
        state.isCollapse = !state.isCollapse
    },
    //添加指定菜单标签
    ADD_MENU_TAG(state, val) {
        //如果添加的菜单标签的组件名不为../views/dashboard/index，则可以添加
        if (val.component !== '../views/dashboard/index') {
            //将高亮显示的标签更新为新添加进来的标签
            state.currentMenuTag = val
            //通过findIndex方法可以判断这个标签存不存在于MenuTagList，只有不存在才可以添加
            const result = state.MenuTagList.findIndex(item => item.component === val.component)
            if (result === -1) {
                //添加菜单标签
                state.MenuTagList.push(val)
            }
        }
        //如果添加的菜单标签名字为dashboard，则不可以添加
         else {
            state.currentMenuTag = null
        }
    },
    //关闭指定菜单标签
    CLOSE_MENU(state, val) {
        //通过findIndex方法可以判断这个标签存不存在于MenuTagList，如果存在则删除该菜单标签
        const result = state.MenuTagList.findIndex(item => item.component === val.component)
        state.MenuTagList.splice(result, 1)
    }

}

const actions={

    collapseMenu(context){

        context.commit('COLLAPSE_MENU');

    },
    addMenuTag(context,val){

        context.commit('ADD_MENU_TAG',val);

    },
    closeMenu(context,val){
        context.commit('CLOSE_MENU',val)
    }

}


export default {
    state,
    mutations,
    actions
}