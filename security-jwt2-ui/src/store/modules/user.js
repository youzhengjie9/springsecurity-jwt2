import Vue from 'vue'
import {getCurrentUserInfo} from '../../api/user'
import{
    refreshToken
} from '../../api/refresh-token'

const state = {
    userName:'', //用户帐号
    nickName: '', //用户昵称
    avatar: '',//当前用户头像地址
    accessToken: localStorage.getItem('accessToken'),
    refreshToken: localStorage.getItem('refreshToken'),
    dynamicMenu:[], //动态菜单（权限）数组
    dynamicRouter:[],//动态路由
    perm:[] //用户权限perm（包括菜单、按钮）
}

const mutations = {
    SET_USERNAME:(state,userName) => {
        state.userName=userName;
    },
    SET_NICKNAME: (state, nickName) => {
        state.nickName = nickName
    },
    SET_AVATAR: (state, avatar) => {
        state.avatar = avatar
    },
    SET_ACCESSTOKEN: (state, accessToken) => {
        state.accessToken = accessToken
    },
    SET_REFRESHTOKEN: (state, refreshToken) => {
        state.refreshToken = refreshToken
    },
    SET_DYNAMIC_MENU: (state,dynamicMenu)=>{
        state.dynamicMenu=dynamicMenu
    },
    SET_DYNAMIC_ROUTER: (state,dynamicRouter)=>{
        state.dynamicRouter=dynamicRouter
    },
    SET_PERM: (state,perm)=>{
        state.perm=perm;
    }
}

const actions = {

    //用户登录成功action
    loginSuccess(context,data){
        //将用户基本信息存储到vuex中，这部分的数据会随着刷新页面而丢失
        context.commit('SET_USERNAME',data.data.userName)
        context.commit('SET_NICKNAME',data.data.nickName);
        context.commit('SET_AVATAR',data.data.avatar);
        //用户的动态菜单
        context.commit('SET_DYNAMIC_MENU',JSON.parse(data.data.dynamicMenu))
        if(data.data.dynamicRouter.length!==0){
            //用户的动态路由
            context.commit('SET_DYNAMIC_ROUTER',JSON.parse(data.data.dynamicRouter))
        }
        if(data.data.perm.length!==0){
            //用户权限perm
            context.commit('SET_PERM',JSON.parse(data.data.perm))
        }
        //将token到localstorage中进行持久化，因为vuex的数据是没有持久化效果的，刷新页面就会丢失，所以要放到localstorage中
        localStorage.setItem('accessToken',data.data.accessToken)
        localStorage.setItem('refreshToken',data.data.refreshToken)

        //点击登录后还需要执行一次手动把token放到vuex中（这步操作只在login操作执行一次即可）
        context.commit('SET_ACCESSTOKEN',data.data.accessToken);
        context.commit('SET_REFRESHTOKEN',data.data.refreshToken);   
    },
    getCurrentUserInfo(context){
        return new Promise((resolve,reject)=>{
            getCurrentUserInfo().then((res)=>{
                if(res.data.code===200){
                    context.commit('SET_USERNAME',res.data.data.userName);
                    context.commit('SET_NICKNAME',res.data.data.nickName);
                    context.commit('SET_AVATAR',res.data.data.avatar);
                    //用户的动态菜单
                    context.commit('SET_DYNAMIC_MENU',JSON.parse(res.data.data.dynamicMenu))
                    //用户的动态路由
                    if(res.data.data.dynamicRouter.length!==0){
                        context.commit('SET_DYNAMIC_ROUTER',JSON.parse(res.data.data.dynamicRouter))
                    }
                    if(res.data.data.perm.length!==0){
                        //用户权限perm
                        context.commit('SET_PERM',JSON.parse(res.data.data.perm))
                    }
                    resolve(res);
                }else if(res.data.code === 301){
                    //这里不做任何事，为了就是防止下一个else将token全部清掉，因为code=301是刷新token的编码，而不需要被清空数据
                }
                else{
                    //防止accessToken和refreshToken过期了
                    //而这两个token会一直存在localstorage，需要客户手动删除才可以进入/login页面
                    //（因为我们设置了路由守卫，一旦有token在localstorage中就无法访问login页面的配置）
                    //所以当获取不到用户信息则要清空这些数据（特别是两个token数据）
                    //清空VUEX内容

                    context.commit('SET_USERNAME','')
                    context.commit('SET_NICKNAME','');
                    context.commit('SET_AVATAR','');
                    context.commit('SET_DYNAMIC_MENU',[])
                    context.commit('SET_DYNAMIC_ROUTER',[])
                    context.commit('SET_PERM',[])
                    context.commit('SET_ACCESSTOKEN','');
                    context.commit('SET_REFRESHTOKEN','');
                    //清空localstorage的accessToken和refreshToken
                    localStorage.removeItem('accessToken')
                    localStorage.removeItem('refreshToken')
                    resolve(res);
                }
                
            }).catch((err)=>{
                //防止accessToken和refreshToken过期了
                //而这两个token会一直存在localstorage，需要客户手动删除才可以进入/login页面
                //（因为我们设置了路由守卫，一旦有token在localstorage中就无法访问login页面的配置）
                //所以当获取不到用户信息则要清空这些数据（特别是两个token数据）
                //清空VUEX内容
                context.commit('SET_USERNAME','')
                context.commit('SET_NICKNAME','');
                context.commit('SET_AVATAR','');
                context.commit('SET_DYNAMIC_MENU',[])
                context.commit('SET_DYNAMIC_ROUTER',[])
                context.commit('SET_PERM',[])
                context.commit('SET_ACCESSTOKEN','');
                context.commit('SET_REFRESHTOKEN','');
                //清空localstorage的accessToken和refreshToken
                localStorage.removeItem('accessToken')
                localStorage.removeItem('refreshToken')
                reject(err);
            })
        })
    },
    //退出成功
    logoutSuccess(context){
        //此时，后端已经成功将用户退出，前端只需要把token和用户信息清空即可（localstorage内容也要清空）
        //清空VUEX内容
        context.commit('SET_USERNAME','')
        context.commit('SET_NICKNAME','');
        context.commit('SET_AVATAR','');
        context.commit('SET_DYNAMIC_MENU',[])
        context.commit('SET_DYNAMIC_ROUTER',[])
        context.commit('SET_PERM',[])
        context.commit('SET_ACCESSTOKEN','');
        context.commit('SET_REFRESHTOKEN','');
        //清空localstorage的accessToken和refreshToken
        localStorage.removeItem('accessToken')
        localStorage.removeItem('refreshToken')
    
    },
    //去刷新token
    toRefreshToken(context){

        refreshToken().then(res=>{
            //将token到localstorage中进行持久化，因为vuex的数据是没有持久化效果的，刷新页面就会丢失，所以要放到localstorage中
            localStorage.setItem('accessToken',res.data.data.accessToken)
            localStorage.setItem('refreshToken',res.data.data.refreshToken)

            //点击登录后还需要执行一次手动把token放到vuex中（这步操作只在login操作执行一次即可）
            context.commit('SET_ACCESSTOKEN',res.data.data.accessToken);
            context.commit('SET_REFRESHTOKEN',res.data.data.refreshToken); 
        }).catch(err=>{ //如果刷新token失败，则清空所有数据
                    context.commit('SET_USERNAME','')
                    context.commit('SET_NICKNAME','');
                    context.commit('SET_AVATAR','');
                    context.commit('SET_DYNAMIC_MENU',[])
                    context.commit('SET_DYNAMIC_ROUTER',[])
                    context.commit('SET_PERM',[])
                    context.commit('SET_ACCESSTOKEN','');
                    context.commit('SET_REFRESHTOKEN','');
                    //清空localstorage的accessToken和refreshToken
                    localStorage.removeItem('accessToken')
                    localStorage.removeItem('refreshToken')
        })

         

    }
}


export default {
    state,
    mutations,
    actions
}