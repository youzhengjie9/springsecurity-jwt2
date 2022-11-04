<template>
  <div class="main">
    <div class="backImg"></div>
    <div class="login">
      <div class="title">
        <span>系统注册</span>
      </div>
      <el-form
        :model="form"
        status-icon
        :rules="rules"
        ref="form"
        label-width="100px"
        class="login-container"
      >
        <!-- 帐号 -->
        <div class="list">

          <el-form-item label="帐号" label-width="80px" prop="username" class="username">
            <el-input
              type="input"
              v-model="form.username"
              autocomplete="off"
              placeholder="请输入账号"
            ></el-input>
          </el-form-item>

        </div>

        <!-- 密码 -->
        <div class="list">
          
          <el-form-item label="密码" label-width="80px" prop="password">
            <el-input
              type="password"
              v-model="form.password"
              autocomplete="off"
              placeholder="请输入密码"
            ></el-input>
          </el-form-item>
        </div>

        <!-- 确认密码 -->
        <div class="list">
          
          <el-form-item label="确认密码" label-width="80px" prop="confirmPassword">
            <el-input
              type="password"
              v-model="form.confirmPassword"
              autocomplete="off"
              placeholder="请再次输入密码"
            ></el-input>
          </el-form-item>
        </div>


        <!-- 邮箱 -->
        <div class="list">

            <el-form-item label="邮箱" label-width="80px" prop="email">
                <el-input
                    type="input"
                    v-model="form.email"
                    autocomplete="off"
                    placeholder="请输入邮箱"
                ></el-input>
            </el-form-item>

        </div>


        <!-- 手机号 -->
        <div class="list">

            <el-form-item label="手机号" label-width="70px" prop="phone">
                <el-input
                    type="input"
                    v-model="form.phone"
                    autocomplete="off"
                    placeholder="请输入手机号"
                    style="width:150px"
                ></el-input>
                <!-- 发送验证码 -->
                <el-button round size="medium" @click="clickSendCode" :disabled="disable">
                  {{ buttonName }}
                </el-button>
            </el-form-item>

        </div>
        

        <!-- 验证码 -->
        <div class="list">

          <el-form-item label="验证码" label-width="80px" prop="code" v-show="codeShow">

              <el-input
                type="input"
                v-model="form.code"
                autocomplete="off"
                placeholder="请输入验证码">
              </el-input>

          </el-form-item>
        </div>

        <div class="btn">
          <el-form-item class="login_submit">
            <el-button
              type="primary"
              @click="register('form')"
              class="login_submit"
              style="width: 330px"
              >注册</el-button
            >
          </el-form-item>
        </div>
      </el-form>
    </div>
  </div>
</template>
  
  <script>
//引入api中的方法
import { register,sendCode } from '../../api/register'
import { initDynamicRouter } from "../../utils/permission";

export default {
  data() {
    return {
      //form表单数据
      form: {
        username: "",
        password: "",
        confirmPassword: "",
        email: "",
        phone: "",
        code: "",
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
        //配置confirmPassword校验规则
        confirmPassword: [
          {
            required: true, //必填项
            message: "请再次输入密码",
            trigger: "blur",
          },
          {
            min: 5, //长度不能小于5位
            max: 20, //长度不能大于20位
            message: "确认密码长度要在5-20位之间",
            trigger: "blur",
          },
        ],
        //配置email校验规则
        email: [
          {
            required: true, //必填项
            message: "请输入邮箱",
            trigger: "blur",
          },
        ],
        //配置phone校验规则
        phone: [
          {
            required: true, //必填项
            message: "请输入手机号",
            trigger: "blur",
          },
          {
            min: 11,
            max: 11,
            message: "请输入11位数字的手机号",
            trigger: "blur",
          },
        ],
        //配置验证码规则
        code: [
          {
            required: true, //必填项
            message: "请输入手机验证码",
            trigger: "blur",
          },
          {
            min: 6,
            max: 6,
            message: "手机验证码长度只能为6位",
            trigger: "blur",
          },
        ],
      },
      //验证码图片的base64编码数据
      imageBase64: "",
      //验证码输入框是否展示，默认是不展示，直到我们输入手机号才展示
      codeShow:false,
      buttonName: "获取验证码",
      //验证码倒计时
      count: 60,
      disable: false
    };
  },
  methods: {
    //点击注册逻辑
    register(form) {
      this.$refs[form].validate((valid) => {
        //如果前端校验通过，则进入这里
        if (valid) {
          const newFormData = {
            userName: this.form.username,
            password: this.form.password,
            confirmPassword:this.form.confirmPassword,
            email:this.form.email,
            phone:this.form.phone,
            code: this.form.code, 
          };
          //调用注册api
          register(newFormData).then(res=>{
              let data=res.data;
                //说明注册成功
              if(data.code === 1100){

                  this.$message({
                      showClose: true,
                      message: data.msg,
                      type: 'success',
                      duration:1000
                  });
                  //注册成功后跳转到登录页
                  this.$router.replace({
                    path:'/login'
                  })
              }else{
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
                    message: '注册失败,请检查是否输入正确',
                    type: 'error',
                    duration:1000
              });
        })

           
        } else {
          return false;
        }
      });
    },
    //发送验证码
    sendCode(phone,timeout){
        //调用发送验证码api
        sendCode(phone).then(res=>{
          let data=res.data;
          //发送验证码成功
          if(data.code === 1102){
                this.$message({
                      showClose: true,
                      message: data.msg,
                      type: 'success',
                      duration:1000
                  });
          }else{
              //发送验证码失败后摇重置按钮
              this.disable = false;
              this.buttonName = "获取验证码";
              //重置倒计时
              this.count = 60;
              clearInterval(timeout);
               //发送验证码失败
                this.$message({
                      showClose: true,
                      message: data.msg,
                      type: 'error',
                      duration:1000
                  });
          }
        }).catch((err)=>{
              //发送验证码失败后摇重置按钮
              this.disable = false;
              this.buttonName = "获取验证码";
              //重置倒计时
              this.count = 60;
              clearInterval(timeout);
                this.$message({
                    showClose: true,
                    message: '后端接口出错了',
                    type: 'error',
                    duration:1000
              });
        })
    },
    //点击发送手机验证码按钮回调
    clickSendCode() {

      //校验手机号长度是否为11位，如果不为11位则不发送验证码
      if(this.form.phone.length !== 11){
            this.$message({
                      showClose: true,
                      message: '请检查手机号是否输入正确',
                      type: 'error',
                      duration:1300
              });
      }else{

        //点击发送验证码自动显示验证码框
        if(this.codeShow==false){
          this.codeShow=true;
        }


        
      var timeout= setInterval(() => {

        //恢复可以点击验证码
        if (this.count < 1) {
          this.disable = false;
          this.buttonName = "获取验证码";
          //重置倒计时
          this.count = 60;
          clearInterval(timeout);
        }
        else {
          this.disable = true;
          this.buttonName = this.count-- + "s后重发";
        }
      }, 1000);

      //点击发送验证码逻辑。（只有获取验证码按钮的disable为false才会执行到这一步），下面的setInterval会自动的修改按钮状态
      //调用发送验证码方法,并且把计时器对象（timeout传给sendCode方法），方便停止计时
      this.sendCode(this.form.phone,timeout);

    }
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