import request from '@/utils/request'


export function getServerInfo(){
    return request({
        method:"get",
        url:'/server/monitor/getServerInfo',
    })
}