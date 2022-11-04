import store from '@/store'

/**
 * 设置操作权限标识符
 */
 export default {
    inserted(el, binding, vnode) {
          //从binding中获取dom元素的value值
          const { value } = binding
          // 前端传入的某个按钮所需要的权限perm的数组/集合（也就是获取使用v-hasPerm="['sys:role:list:add']"传入的perm数组）
          const btnPermissionList = value;
      //     console.log(btnPermissionList)
          // 拿到用户所有权限perm的集合，然后通过前端传入的按钮权限进行匹配
          const userAllPermissionList = store.state.user.perm
      //     console.log(userAllPermissionList)
          // 利用some查询，只要有一个元素满足条件就返回true，全部不满足返回false 
          var hasPermissions = userAllPermissionList.some(permission => {
                  
                  //遍历userAllPermissionList拿到其中一个个元素permission
                  //调用btnPermissionList.includes(permission)，如果返回true说明有该按钮权限，那么这个按钮会显示出来，反之则隐藏。
                return btnPermissionList.includes(permission)
          })

          // 传入的按钮权限perm不存在集合里面就移除该节点（说白了就是没有权限的话就隐藏这个按钮）
          if (!hasPermissions) {
                el.parentNode && el.parentNode.removeChild(el)
          }
    }
}
