<template>
    <el-form ref="form" label-width="100px" :model="form">
        <el-form-item v-for="item in formLabel" :key="item.label" :label="item.label">
            <!-- 如果指定的类型是input -->
            <el-input
                v-if="item.type === 'input'"
                :placeholder="'请输入' + item.label"
                v-model="form[item.model]"
            ></el-input>
            <!-- 如果指定的类型是password -->
            <el-input
                v-if="item.type === 'password' && operateType==='add'"
                :placeholder="'请输入' + item.label"
                v-model="form[item.model]"
                show-password
            ></el-input>
            <el-input
                v-if="item.type === 'password' && operateType==='edit'"
                :placeholder="'请输入' + item.label+'（如果不输入密码则认为不修改密码）'"
                v-model="form[item.model]"
                show-password
            ></el-input>
            <!-- 如果指定的类型是switch -->
            <!-- <el-switch v-if="item.type === 'switch' && form[item.model]===0"  v-model="T">
            </el-switch>
            <el-switch v-if="item.type === 'switch' && form[item.model]===1"  v-model="F">
            </el-switch>
            <el-switch v-if="item.type === 'switch' && form[item.model]!==0 && form[item.model]!==1"  v-model="T">
            </el-switch> -->

            <el-switch v-if="item.type === 'switch'" v-model="form[item.model]">
            </el-switch>

            <!-- 如果指定的类型是date -->
            <el-date-picker
                v-if="item.type === 'date'"
                type="date"
                value-format="yyyy-MM-dd"
                placeholder="选择日期"
                v-model="form[item.model]"
            ></el-date-picker>
            <!-- 如果指定的类型是datetime -->
            <el-date-picker
                v-if="item.type === 'datetime'"
                type="datetime"
                value-format="yyyy-MM-dd HH:mm:ss"
                placeholder="选择日期时间"
                v-model="form[item.model]"
            ></el-date-picker>
            <!-- 如果指定的类型是select -->
            <el-select
                v-if="item.type === 'select'"
                placeholder="请选择"
                v-model="form[item.model]">
                <!-- 选择框（select下面的opts数组） -->
                <el-option
                    v-for="item in item.opts"
                    :key="item.value"
                    :label="item.label"
                    :value="item.value"
                ></el-option>
            </el-select>

            

            <!-- 如果指定的类型是singleUpload,单个图片上传，例如头像 -->
            <el-upload
                v-if="item.type === 'singleUpload'"
                class="avatar-uploader"
                action="http://localhost:8188/upload/avatar"
                :name="avatarFile"
                :show-file-list="false"
                :headers="accesstoken"
                :on-success="handleAvatarSuccess"
                :before-upload="beforeAvatarUpload">
                <!-- 头像展示区域 -->
                <img v-if="imageUrl" :src="imageUrl" class="avatar">

                <i v-else class="el-icon-plus avatar-uploader-icon"></i>

            </el-upload>

        </el-form-item>
        <el-form-item><slot></slot></el-form-item>
    </el-form>
</template>
<script>


export default {
    name: 'RoleForm',
    props: {
        formLabel: Array,
        form: Object,
        inline: Boolean,
        operateType: String,
        //请求头accesstoken，因为我们头像上传需要token，但是又没有使用到axios所以需要手动往header添加token
        accesstoken: Object,
        imageUrl: {
            type: String,
            default:''
        }
    },
    data () {
        return {
            T:true,
            F:false,
            curSex: '男',
            //后端接收文件的参数名（如果不设置默认为file）
            avatarFile:'avatarFile'
        }
    },
    methods:{
      //图片上传成功回调函数
      handleAvatarSuccess(res, file) {
        
        this.imageUrl=res.data;
        
      },
      //上传头像之前，对头像文件进行校验（比如大小进行校验）
      beforeAvatarUpload(file) {

        const isImageType = file.type === 'image/jpeg' || file.type === 'image/jpg' ||file.type === 'image/png'
                      ||file.type === 'image/JPEG' || file.type === 'image/JPG' ||file.type === 'image/PNG'
        const isLt3M = file.size / 1024 / 1024 < 3;

        //对文件格式进行校验
        if (!isImageType) {
          this.$message.error('上传头像图片只能是jpeg/jpg/png格式!');
        }
        if (!isLt3M) {
          this.$message.error('上传头像图片大小不能超过 3MB!');
        }
        return isImageType && isLt3M;
      }
    }
}
</script>

<style scoped>
  .avatar-uploader .el-upload {
    border: 1px dashed #d9d9d9;
    border-radius: 6px;
    cursor: pointer;
    position: relative;
    overflow: hidden;
  }

  .avatar-uploader-icon:hover{
    border:1px dashed #409EFF
  }
  .avatar-uploader-icon {
    font-size: 28px;
    color: #8c939d;
    width: 178px;
    height: 178px;
    line-height: 178px;
    text-align: center;
  }
  .avatar {
    width: 178px;
    height: 178px;
    display: block;
  }
</style>