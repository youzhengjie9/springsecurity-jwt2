import request from '../utils/request'

export function logout(accessToken,refreshToken){
    return request({
        method:'post',
        url:'/user/logout',
        headers:{
            accessToken:accessToken,
            refreshToken:refreshToken
        }
    })
}