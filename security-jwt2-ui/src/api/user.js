import request from '../utils/request.js'

export function getCurrentUserInfo(){
    return request({
        method:'get',
        url:'/user/getCurrentUserInfo'
    })
}

export function selectAllUserByLimit(page,size){
    return request({
        method:'get',
        url:'/user/selectAllUserByLimit',
        params:{
            page:page,
            size:size
        }
    })
}

export function selectAllUserCount(){
    return request({
        method:'get',
        url:'/user/selectAllUserCount'
    })
}


export function addUser(operateForm){
    return request({
        method:'post',
        url:'/user/addUser',
        data:operateForm
    })
}

export function updateUser(operateForm){
    return request({
        method:'post',
        url:'/user/updateUser',
        data:operateForm
    })
}

export function deleteUser(userid){
    return request({
        method:'delete',
        url:'/user/deleteUser',
        params:{
            id:userid
        }
    })
}

export function assignRole(roles){
    return request({
        method:'post',
        url:'/user/assignRole',
        data: roles
    })
}

export function searchUserByUserNameAndLimit(userName, page, size){
    return request({
        method:'get',
        url:'/user/searchUserByUserNameAndLimit',
        params:{
            userName:userName,
            page: page,
            size:size
        }
    })
}

export function searchUserCountByUserName(userName){
    return request({
        method:'get',
        url:'/user/searchUserCountByUserName',
        params:{
            userName:userName
        }
    })
}