
const state= {

    //表单上传头像地址
    uploadAvatar:'',
    //表单（是否启用）开关，默认是true打开
    curSwitchStatus:true

}

const mutations = {

    SET_UPLOAD_AVATAR(state,avatarPath){
        state.uploadAvatar=avatarPath
    }
    ,
    SET_CURSWITCHSTATUS(state,curSwitchStatus){
        state.curSwitchStatus=curSwitchStatus
    }


}

const actions={

    setUploadAvatar(context,avatarPath){
        context.commit('SET_UPLOAD_AVATAR',avatarPath)
    }
    ,
    setCurSwitchStatus(context,curSwitchStatus){
        context.commit('SET_CURSWITCHSTATUS',curSwitchStatus)
    }

}


export default {
    state,
    mutations,
    actions
}