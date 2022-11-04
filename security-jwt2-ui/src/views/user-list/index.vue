<template>
  <div class="manage">
    <!-- 新增/修改用户的dialog -->
    <el-dialog
      :title="operateType === 'add' ? '新增用户' : '修改用户'"
      :visible.sync="isShow"
    >
      <!-- dialog的内容 -->
      <user-form
        :formLabel="opertateFormLabel"
        :form="operateForm"
        :operateType="operateType"
        :accesstoken="accesstoken"
        :imageUrl="formAvatar"
        ref="form"
      ></user-form>

      <!-- dialog的底部 -->
      <div slot="footer" class="dialog-footer">
        <el-button @click="isShow = false">取消</el-button>
        <el-button type="primary" @click="confirm">确定</el-button>
      </div>
    </el-dialog>

    <!-- 分配角色dialog -->
    <el-dialog :title="assignRoleTitle" :visible.sync="assignRoleDialogShow">
      <!-- 分配角色dialog的内容 -->

      <el-table
        ref="multipleTable"
        :data="assignRoleTableData"
        tooltip-effect="dark"
        style="width: 100%"
        @selection-change="handleSelectionChange"
      >
        <!-- 全选 -->
        <el-table-column type="selection" width="55"> </el-table-column>

        <el-table-column prop="id" label="id" width="120" show-overflow-tooltip>
        </el-table-column>
        <el-table-column
          prop="name"
          label="角色名称"
          width="120"
          show-overflow-tooltip
        >
        </el-table-column>
        <el-table-column
          prop="roleKey"
          label="角色关键字"
          width="120"
          show-overflow-tooltip
        >
        </el-table-column>

        <el-table-column
          prop="remark"
          label="备注"
          width="120"
          show-overflow-tooltip
        >
        </el-table-column>
      </el-table>

      <!--  -->

      <!-- dialog的底部 -->
      <div slot="footer" class="dialog-footer">
        <el-button @click="assignRoleDialogShow = false">取消</el-button>
        <el-button type="primary" @click="confirmAssignRole">确定</el-button>
      </div>
    </el-dialog>


  <!-- 表格的头部,例如新增、导入、搜索框。 -->
  <el-row :gutter="20">
        <el-col :span="6">
          <el-button v-hasPerm="['sys:user:list:add']" type="primary" @click="addUser">+ 新增</el-button> 
          <el-button type="primary" @click="exportAllUser"> 导出</el-button> 
        </el-col>
         <!-- 搜索表单组件 -->
        <el-col :span="6" :offset="12">
          
          <search-form
            :formLabel="formLabel"
            :form="searchForm"
            :inline="true"
            ref="form"
          >
            <el-button type="primary" @click="searchUser(searchForm.keyword)"
              >搜索</el-button
            >
          </search-form>
        </el-col>
  </el-row>


    <!-- 用户管理的表格内容 -->
    <user-table
      :tableData="tableData"
      :config="config"
      @changePage="getList()"
      @edit="editUser"
      @assignRole="assignRole"
      @del="delUser"
    ></user-table>
  </div>
</template>
<script>
import UserForm from "../../../src/components/user/user-form/index.vue";
import SearchForm from "@/components/common/SearchForm.vue";
import UserTable from "../../../src/components/user/user-table/index.vue";
import {
  selectAllUserByLimit,
  selectAllUserCount,
  addUser,
  updateUser,
  deleteUser,
  assignRole,
  searchUserByUserNameAndLimit,
  searchUserCountByUserName
} from "@/api/user";

import { selectAllRole, selectUserCheckedRoleByUserId } from "../../api/role";

//要写成这种{}的形式导入export-excel，否则将导出不了
import {
   exportAllUser 
} from "@/api/export-excel";

export default {
  name: "UserList",
  //注册组件
  components: {
    UserForm,
    UserTable,
    SearchForm,
  },
  data() {
    return {
      //判断新增还是修改
      operateType: "add",
      //分配角色dialog标题
      assignRoleTitle: "分配角色",
      //true则打开新增或者修改的表单dialog，false则关闭
      isShow: false,
      //true则打开分配角色的表单dialog，false则关闭
      assignRoleDialogShow: false,
      //记录当前需要分配角色的用户id
      currentAssignRoleUserId:'',
      //当前模式：正常查询或者搜索（默认是查询）
      currentModel:'select',
      //当前搜索的关键字
      currentSearchKeyword:'',
      //请求头accesstoken，因为我们头像上传需要token，但是又没有使用到axios所以需要手动往header添加token
      accesstoken: {
        //请求头的kv键值对
        accessToken: localStorage.getItem("accessToken"),
      },
      //传递表单头像
      formAvatar: "",
      //动态生成新增或者修改的表单内容
      opertateFormLabel: [
        {
          model: "userName",
          label: "用户名",
          type: "input",
        },
        {
          model: "password",
          label: "密码",
          type: "password",
        },
        {
          model: "nickName",
          label: "昵称",
          type: "input",
        },
        {
          model: "sex",
          label: "性别",
          type: "select",
          opts: [
            {
              label: "男",
              value: 0,
            },
            {
              label: "女",
              value: 1,
            },
            {
              label: "未知",
              value: 2,
            },
          ],
        },
        {
          model: "phone",
          label: "手机号",
          type: "input",
        },
        {
          model: "email",
          label: "邮箱",
          type: "input",
        },
        {
          model: "status",
          label: "是否启用",
          type: "switch",
        },
        {
          model: "avatar",
          label: "头像",
          type: "singleUpload",
        },
      ],
      //当新增或者修改时，表单数据就会同步到这里
      operateForm: {
        userName: "",
        password: "",
        nickName: "",
        sex: "",
        phone: "",
        email: "",
        status: "",
        avatar: "",
      },
      //后端所有可供选择的角色数据都要存到这(这部分内容可以从后端数据库获取)
      assignRoleTableData: [],

      assignRoleSelectedList: [],
      //搜索框
      formLabel: [
        {
          model: "keyword",
          label: "用户名",
          type: "input",
        },
      ],
      searchForm: {
        keyword: "",
      },
      //表格数据
      tableData: [],
      config: {
        page: 1,
        total: 1
      },
    };
  },
  methods: {
    confirm() {
      //从vuex中获取表单提交过来的新/旧头像地址
      this.operateForm.avatar = this.$store.state.form.uploadAvatar;
      //从vuex中获取表单提交过来的是否启用开关
      this.operateForm.status = this.$store.state.form.curSwitchStatus;


       //sex
       if (this.operateForm.sex === 0 || this.operateForm.sex === '男') {
              this.operateForm.sex = "男";
          } else if (this.operateForm.sex === 1 || this.operateForm.sex === '女') {
            this.operateForm.sex = "女";
          } else if(this.operateForm.sex ===2 || this.operateForm.sex === '未知'){
            this.operateForm.sex = "未知";
          }

      //如果是添加操作
      if (this.operateType === "add") {
        addUser(this.operateForm)
          .then((res) => {
            this.$message({
              showClose: true,
              message: "添加成功",
              type: "success",
              duration: 1000,
            });
            this.isShow = false;

            //成功之后将表单vuex数据还原
            this.$store.dispatch("setCurSwitchStatus", true);
            this.$store.dispatch("setUploadAvatar", "");

            this.getList();
          })
          .catch((err) => {
            this.$message({
              showClose: true,
              message: "添加失败",
              type: "error",
              duration: 1000,
            });
          });
      }
      //反之则是修改操作
      else {
       
        updateUser(this.operateForm)
          .then((res) => {
            this.$message({
              showClose: true,
              message: "修改成功",
              type: "success",
              duration: 1000,
            });
            this.isShow = false;

            //成功之后将表单vuex数据还原
            this.$store.dispatch("setCurSwitchStatus", true);
            this.$store.dispatch("setUploadAvatar", "");

            this.getList();
          })
          .catch((err) => {
            this.$message({
              showClose: true,
              message: "修改失败",
              type: "error",
              duration: 1000,
            });
          });
      }
    },
    //新增用户
    addUser() {
      this.isShow = true;
      this.operateType = "add";
      this.operateForm = {
        //给新增用户表单设置默认值
        userName: "",
        password: "",
        nickName: "",
        sex: "未知",
        phone: "",
        email: "",
        status: true,
        avatar: "",
      };
      //添加操作时要把表单头像清空
      this.formAvatar = "";
    },
    //修改用户
    editUser(row) {
      this.operateType = "edit";
      this.isShow = true;
      this.operateForm = row;
      //修改操作时要把表单头像更新为当前点击的那个记录的头像
      this.formAvatar = "";
      this.formAvatar = row.avatar;
    },
    //删除用户
    delUser(row) {
      this.$confirm("是否删除该用户？", "提示", {
        confirmButtonText: "确认",
        cancelButtonText: "取消",
        type: "warning",
      }).then(() => {
        //id
        const id = row.id;
        deleteUser(id)
          .then((res) => {
            this.$message({
              showClose: true,
              type: "success",
              message: "删除成功",
            });
            //刷新数据
            this.getList();
          })
          .catch((err) => {
            this.$message({
              showClose: true,
              type: "error",
              message: "删除失败",
            });
          });
      });
    },
    //获取列表
    getList() {
      this.config.loading = true;
      
      //如果是默认的select模式：
      if(this.currentModel==='select' || this.currentSearchKeyword ===''){
        selectAllUserByLimit(this.config.page, 7).then(({ data: res }) => {
        for (let i = 0; i < res.data.length; i++) {
          //status
          if (res.data[i].status === 0) {
            res.data[i].status = true;
          } else {
            res.data[i].status = false;
          }

          //sex
          if (res.data[i].sex === 0) {
            res.data[i].sex = "男";
          } else if (res.data[i].sex === 1) {
            res.data[i].sex = "女";
          } else {
            res.data[i].sex = "未知";
          }
        }

        this.tableData = res.data;

        this.config.loading = false;
      });
      }else if(this.currentModel==='search'){
        this.searchUser(this.currentSearchKeyword);
      }
      
    },
    //搜索,keywork就是搜索的关键字
    searchUser(keyword) {
      this.config.loading = true;
      //切换成搜索模式
      this.currentModel='search';
      //记录当前搜索的关键字
      this.currentSearchKeyword=keyword;

      searchUserByUserNameAndLimit(keyword,this.config.page, 7).then(({ data: res })=>{
        for (let i = 0; i < res.data.length; i++) {
          //status
          if (res.data[i].status === 0) {
            res.data[i].status = true;
          } else {
            res.data[i].status = false;
          }

          //sex
          if (res.data[i].sex === 0) {
            res.data[i].sex = "男";
          } else if (res.data[i].sex === 1) {
            res.data[i].sex = "女";
          } else {
            res.data[i].sex = "未知";
          }
        }

        //更新数据
        this.tableData = res.data;
        
        //更新分页
        this.getsearchUserCount(keyword);


        this.config.loading = false;
      })

    },
    //获取用户总数量
    getUserCount() {
      selectAllUserCount().then((res) => {
        this.config.total = res.data.data;
      });
    },
    //获取搜索记录数
    getsearchUserCount(keyword){
      searchUserCountByUserName(keyword).then(res=>{
        this.config.total = res.data.data;
      })
    },
    //当点击分配角色选项时弹出dialog之前执行的方法
    assignRole(row) {
      //记录当前需要分配角色的用户id
      this.currentAssignRoleUserId=row.id;
      //打开分配角色dialog
      this.assignRoleDialogShow = true;
      //初始化角色列表数据，后端所有可选择的角色数据都要存到这(这部分内容可以从后端数据库获取)
      selectAllRole().then((res1) => {
        this.assignRoleTableData = res1.data.data;

        //当分配角色时，表单数据就会同步到这里，这个数据后面要提交给后端，也就是用户更新后的角色数据
        //数据格式为[{id:'2002'}]
        selectUserCheckedRoleByUserId(row.id).then((res2) => {

          this.assignRoleSelectedList = res2.data.data;


          this.assignRoleSelectedList.forEach(row => {
                this.$refs.multipleTable.toggleRowSelection(
                    this.assignRoleTableData.find(item => { return row.id == item.id;}
                ),true);
            });
           
        });
      });
    },
    //当点击确认分配角色时回调的方法
    confirmAssignRole() {

      let jsonData={
        roles:this.assignRoleSelectedList,
        userid:this.currentAssignRoleUserId
      }
      assignRole(jsonData).then(res=>{
            this.$message({
            showClose: true,
            message: "分配角色成功",
            type: "success",
            duration: 1000,
          });
          //关闭dialog
          this.assignRoleDialogShow = false;
          //将currentAssignRoleUserId还原
          this.currentAssignRoleUserId='';
          // this.getList();
      }).catch(err=>{
          this.$message({
            showClose: true,
            message: "分配角色失败",
            type: "error",
            duration: 1000,
          });
      })
      
    },
    //处理分配角色dialog的选中
    handleSelectionChange(val) {

      this.assignRoleSelectedList = val;
    },
    //导出所有用户信息
    exportAllUser(){

      this.$confirm('是否导出所有用户的数据?', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(() => {
          //调用导出所有用户信息api
          exportAllUser().then(response=>{
            // -----导出excel固定写法，直接复制即可-----
            let disposition = response.headers['content-disposition'];
            let filename = disposition?disposition.substring(disposition.indexOf('=')+1,disposition.indexOf('.')):"下载文件";
            let newName = decodeURI(escape(filename))
            let extName =disposition.substring(disposition.indexOf('.')+1)
            let blob = new Blob([response.data],{type: 'application/vnd.ms-excel'});
            let link = document.createElement("a");
            let evt = document.createEvent("HTMLEvents");
            evt.initEvent("click", false, false);
            link.href = URL.createObjectURL(blob);
            link.download = newName+"."+extName;
            link.style.display = "none";
            document.body.appendChild(link);
            link.click();
            window.URL.revokeObjectURL(link.href);
          }).catch(err=>{
              this.$message({
                showClose: true,
                message: "导出excel失败",
                type: "error",
                duration: 1000,
              });
          })
        }).catch(err=>{
          
        })

     
    }
  },
  created() {
    this.getList();
    this.getUserCount();
  },
};
</script>
<style lang="less" scoped>

</style>

