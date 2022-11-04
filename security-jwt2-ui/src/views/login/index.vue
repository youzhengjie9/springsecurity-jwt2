<template>
  <div class="main">
    <div class="backImg"></div>
    <div class="login">
      <div class="title">
        <span>系统登录</span>
      </div>
      <el-form
        :model="form"
        status-icon
        :rules="rules"
        ref="form"
        label-width="100px"
        class="login-container"
      >
        <div class="list">
          <!-- 帐号 -->
          <el-form-item
            label="帐号"
            label-width="65px"
            prop="username"
            class="username"
          >
            <el-input
              type="input"
              v-model="form.username"
              autocomplete="off"
              placeholder="请输入账号"
            ></el-input>
          </el-form-item>
        </div>
        <div class="list">
          <!-- 密码 -->
          <el-form-item label="密码" label-width="65px" prop="password">
            <el-input
              type="password"
              v-model="form.password"
              autocomplete="off"
              placeholder="请输入密码"
            ></el-input>
          </el-form-item>
        </div>
        <div class="list">
          <!-- 验证码 -->
          <el-form-item label="验证码" label-width="65px"  prop="code">
            <el-col :span="12">
              <el-input
                type="input"
                style="width: 140px"
                v-model="form.code"
                autocomplete="off"
                placeholder="请输入验证码"
              >
              </el-input>
            </el-col>

            <img
              :src="imageBase64"
              style="width: 130px"
              @click="refreshCaptcha"
            />
          </el-form-item>
        </div>

        <div class="btn">
          <el-form-item class="login_submit">
            <el-button
              type="primary"
              @click="login('form')"
              class="login_submit"
              style="width:330px"
              >登录</el-button
            >
          </el-form-item>
        </div>
      </el-form>
    </div>
  </div>
</template>

<script>
//引入api中的方法
import { getCaptcha,userLogin } from "../../api/login";
import{
  initDynamicRouter
}from '../../utils/permission'

export default {
  data() {
    return {
      //form表单数据
      form: {
        username:'',
        password:'',
        code:'', //前端输入的验证码
        codeKey:'' //存储在redis中的正确的验证码的key，通过这个key能找到正确的验证码
      },
      //配置前端表单校验规则
      rules: {
        //配置username校验规则
        username: [
          {
            required: true, //必填项
            message: "请输入帐号",
            trigger: "blur",
          },
          {
            min: 3, //长度不能小于3位
            max: 15, //长度不能大于15位
            message: "帐号长度要在3-15位之间",
            trigger: "blur",
          },
        ],
        //配置password校验规则
        password: [
          {
            required: true, //必填项
            message: "请输入密码",
            trigger: "blur",
          },
          {
            min: 5, //长度不能小于5位
            max: 20, //长度不能大于20位
            message: "密码长度要在5-20位之间",
            trigger: "blur",
          },
        ],
        //配置验证码规则
        code: [
          {
            required: true, //必填项
            message: "请输入验证码",
            trigger: "blur",
          },
          {
            min: 5,
            max: 5,
            message: "验证码长度只能为5位",
            trigger: "blur",
          },
        ],
      },
      //验证码图片的base64编码数据
      imageBase64: "",
    };
  },
  mounted() {
    //一进入login页面自动刷新验证码
    this.refreshCaptcha();
  },
  methods: {
    //点击登录逻辑
    login(form) {
      this.$refs[form].validate((valid) => {
        //如果前端校验通过，则进入这里
        if (valid) {
          const newFormData={
            username:this.form.username,
            password:this.form.password,
            code:this.form.code, //前端输入的验证码
            codeKey:this.form.codeKey //存储在redis中的正确的验证码的key，通过这个key能找到正确的验证码
          }
          
          //调用userLogin的api方法
          userLogin(newFormData).then((res) => {
            let data=res.data;
            
            //用户登录成功
            if(data.code===600)
            {
              
                this.$store.dispatch('loginSuccess',data);
                this.$message({
                    showClose: true,
                    message: data.msg,
                    type: 'success',
                    duration:1000
                });
                //登陆成功后就可以为这个用户生成动态路由（调用permission.js的初始化动态路由方法）
                initDynamicRouter();

              
                //登录成功后跳转到首页
                this.$router.push({
                  path:'/'
                })
            }
            else if(data.code===601 || data.code===602){
              this.$message({
                    showClose: true,
                    message: data.msg,
                    type: 'error',
                    duration:1000
                });
            }
            
        }).catch((err)=>{
                this.$message({
                    showClose: true,
                    message: '登录失败,请检查是否输入正确',
                    type: 'error',
                    duration:1000
              });
        })
          
     
        } else {
          return false;
        }
      });
    },
    //调用刷新验证码api接口
    refreshCaptcha() {
      getCaptcha()
        .then((res) => {
          //把验证码的key存储到表单对象中，请求登录接口时方便通过携带这个key从redis中找到正确的验证码
          this.form.codeKey=res.data.data.codeKey;
          //存储验证码图片base64
          this.imageBase64 = res.data.data.imageBase64;
        })
        .catch((err) => {});
    },
  },
};
</script>

<style scoped>

.backImg {
  background: url("../../assets/login-page.jpg");
  background-size: 100% 100%;
  position: fixed;
  width: 100%;
  height: 100%;
  left: 0;
  top: 0;
}
.login {
  position: absolute;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
  border: 1px solid #ccc;
  background: #fff;
  width: 22%;
  padding: 20px 20px 25px 20px;
}
.login .title {
  text-align: center;
  padding-bottom: 5px;
}
.login .title span {
  font-size: 30px;
  color: #000;
}

.login .list {
  display: flex;
  align-items: center;
  padding: 2px 0;
}
.login .list input {
  border-radius: 3px;
  border: none;
  outline: none;
  color: #999;
  border: 1px solid #bdbdbd;
  font-size: 14px;
  line-height: 35px;
  padding: 0 10px;
  display: block;
  box-sizing: border-box;
  flex: 7;
}

.login .list .getCode span {
  font-size: 20px;
  background: #f5f7fa;
  color: #777;
  border-radius: 4px;
  line-height: 38px;
  border: 1px solid #ccc;
  display: inline-block;
  margin-left: 10px;
  width: 80px;
  text-align: center;
  user-select: none;
  cursor: pointer;
}

.btn {
  display: flex;
  justify-content: flex-end;
  padding-top: 5px;
}
.btn button {
  font-size: 13px;
  color: #fff;
  background: #46b5ff;
  outline: none;
  border: none;
  line-height: 35px;
  padding: 0 20px;
  display: inline-block;
  flex: 1;
  cursor: pointer;
}

</style>