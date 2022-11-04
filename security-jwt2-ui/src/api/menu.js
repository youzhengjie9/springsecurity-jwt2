import request from '@/utils/request'


export function selectRoleCheckedMenuByRoleId(roleid){
    return request({
        method:'get',
        url:'/menu/selectRoleCheckedMenuByRoleId',
        params:{
            id:roleid
        }
    })
}

export function addMenu(menuDTO){
    return request({
        method:'post',
        url:'/menu/addMenu',
        data:menuDTO
    })
}

export function updateMenu(menuDTO){
    return request({
        method:'post',
        url:'/menu/updateMenu',
        data:menuDTO
    })
}
export function deleteMenu(menuid){
    return request({
        method:'delete',
        url:'/menu/deleteMenu',
        params:{
            menuid:menuid
        }
    })
}

export function selectMenuNameByMenuId(menuid){
    return request({
        method:'get',
        url:'/menu/selectMenuNameByMenuId',
        params:{
            menuid:menuid
        }
    })
}