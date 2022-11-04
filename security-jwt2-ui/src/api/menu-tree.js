import request from '../utils/request'

export function buildTreeByUserId(userid){
    return request({
        method:'get',
        url:'/menutree/buildTreeByUserId',
        params:{
            userid:userid
        }
    })
}

export function buildAllMenuPermissionTree(){
    return request({
        method:'get',
        url:'/menutree/buildAllMenuPermissionTree'
    })
}

export function buildAssignMenuTree(){
    return request({
        method:'get',
        url:'/menutree/buildAssignMenuTree'
    })
}

export function assignMenu(assignMenu){
    return request({
        method:'post',
        url:'/role/assignMenu',
        data: assignMenu
    })
}

export function buildCanChooseMenuTreeByNewMenuType(type){
    return request({
        method:'get',
        url:'/menutree/buildCanChooseMenuTreeByNewMenuType',
        params:{
            type:type
        }
    })
}