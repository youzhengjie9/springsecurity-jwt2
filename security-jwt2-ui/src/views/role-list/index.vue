<template>
  <div class="manage">
    <!-- 新增/修改角色的dialog -->
      <el-dialog
          :title="operateType === 'add' ? '新增角色' : '修改角色'"
          :visible.sync="isShow">

          <!-- dialog的内容 -->
          <role-form
              :formLabel="opertateFormLabel"
              :form="operateForm"
              ref="form"
          ></role-form>

          <!-- dialog的底部 -->
          <div slot="footer" class="dialog-footer">
              <el-button @click="isShow = false">取消</el-button>
              <el-button type="primary" @click="confirm">确定</el-button>
          </div>
      </el-dialog>


    <!-- 分配菜单dialog -->
    <el-dialog :title="assignMenuTitle" :visible.sync="assignMenuDialogShow">

        <!-- 树形控件 ，node-key指定的是后台查出来的每一行记录的id字段名-->
        <el-tree
            :data="menuData"
            show-checkbox
            default-expand-all
            node-key="id"
            ref="tree"
            highlight-current
            :default-checked-keys="assignMenuDefaultCheckedArray"
            :props="defaultProps">

        </el-tree>

        <!-- dialog的底部 -->
        <div slot="footer" class="dialog-footer">
              <el-button @click="assignMenuDialogShow = false">取消</el-button>
              <el-button type="primary" @click="confirmAssignMenu">确定</el-button>
        </div>

    </el-dialog>


    <!-- 表格的头部,例如新增、导入、搜索框。 -->
    <el-row :gutter="20">
        <el-col :span="6">
            <el-button v-hasPerm="['sys:role:list:add']" type="primary" @click="addRole">+ 新增</el-button>
          <el-button type="primary" @click="exportAllRole"> 导出</el-button> 
        </el-col>
         <!-- 搜索表单组件 -->
        <el-col :span="6" :offset="12">
          
            <search-form
              :formLabel="formLabel"
              :form="searchForm"
              :inline="true"
              ref="form">
              <el-button type="primary" @click="searchRole(searchForm.keyword)">搜索</el-button>
          </search-form>

        </el-col>
  </el-row>


      <!-- 角色管理的表格内容 -->
      <role-table
          :tableData="tableData"
          :config="config"
          @changePage="getList()"
          @edit="editRole"
          @assignMenu="assignMenu"
          @del="delRole"
      ></role-table>
  </div>
</template>
<script>
import RoleForm from '../../../src/components/role/role-form/index.vue'
import SearchForm from '@/components/common/SearchForm.vue'
import RoleTable from '../../../src/components/role/role-table/index.vue'
import { 
    selectAllRoleByLimit,
    selectAllRoleCount,
    addRole,
    updateRole,
    deleteRole,
    searchRoleByRoleNameAndLimit,
    searchRoleCountByRoleName
} from '../../api/role'

import{
    buildAssignMenuTree,
    assignMenu
} from '../../api/menu-tree'

import{
    selectRoleCheckedMenuByRoleId
}from '../../api/menu'

//要写成这种{}的形式导入export-excel，否则将导出不了
import{
    exportAllRole
}from '../../api/export-excel'

export default {
  name: 'RoleList',
  components: {
      RoleForm,
      RoleTable,
      SearchForm
  },
  data () {
      return {
          //判断新增还是修改
          operateType: 'add',
          //true则打开新增或者修改的表单dialog，false则关闭
          isShow: false,
          //分配菜单dialog标题
          assignMenuTitle: "分配菜单",
          //true则打开分配菜单的表单dialog，false则关闭
          assignMenuDialogShow: false,
          //记录当前需要分配菜单的角色id
          currentAssignMenuRoleId:'',
          //当前模式：正常查询或者搜索（默认是查询）
          currentModel:'select',
          //当前搜索的关键字
          currentSearchKeyword:'',
          assignMenuDefaultCheckedArray:[],
          //动态生成新增或者修改的表单内容
          opertateFormLabel: [
              {
                  model: 'name',
                  label: '角色名称',
                  type: 'input'
              },
              {
                  model: 'roleKey',
                  label: '角色关键字',
                  type: 'input'
              },
              {
                  model: 'status',
                  label: '是否启用',
                  type: 'switch'
              },
              {
                  model: 'remark',
                  label: '备注',
                  type: 'input'
              }
          ],
          //当新增或者修改时，表单数据就会同步到这里
          operateForm: {
            name: '',
            roleKey: '',
            status: '',
            remark: ''
          },
          formLabel: [
              {
                  model: "keyword",
                  label: '',
                  type: 'input'
              }
          ],
          searchForm:{
              keyword: ''
          },
          //表格数据
          tableData: [],
          config: {
                page: 1,
                total: 1
           },
           //分配菜单dialog中的菜单数据
          menuData:[],
          //分配菜单dialog中的树形控件所需要的配置
          defaultProps: {
            //子节点数组名（我们后台查出来的子节点数组名为children）
            children: 'children',
            //分配菜单dialog中的树形控件每一行的展示的名字（我们后台查的每一项名字为menuName）
            label: 'menuName'
         }
      }
  },
  methods: {
      confirm() {

        if (this.operateType === 'add') {

            addRole(this.operateForm).then(res=>{

            this.$message({
                showClose: true,
                message: "添加成功",
                type: "success",
                duration: 1000,
            });

            this.isShow = false
            this.getList()
            }).catch(err=>{
                this.$message({
                    showClose: true,
                    message: "添加失败",
                    type: "error",
                    duration: 1000,
                });
            })

        }else{

            updateRole(this.operateForm).then(res=>{
                this.$message({
                    showClose: true,
                    message: "修改成功",
                    type: "success",
                    duration: 1000,
                });
            this.isShow = false
            this.getList()
            }).catch(err=>{
                this.$message({
                    showClose: true,
                    message: "修改失败",
                    type: "error",
                    duration: 1000,
                });
            })

        }
      },
      //新增角色
      addRole () {
          this.isShow = true
          this.operateType = 'add'
          this.operateForm = {
              name: '',
              roleKey: '',
              status: false,
              remark: ''
          }
      },
      //修改角色
      editRole(row) {
          this.operateType = 'edit'
          this.isShow = true
          this.operateForm = row
      },
      //删除角色
      delRole (row) {
          this.$confirm("是否删除该角色？", "提示", {
              confirmButtonText: "确认",
              cancelButtonText: "取消",
              type: "warning"
          }).then(() => {
              const id = row.id;
              deleteRole(id).then(res=>{
                this.$message({
                showClose: true,
                type: "success",
                message: "删除成功",
                });
                //刷新数据
                this.getList();
              }).catch(err=>{
                this.$message({
                showClose: true,
                type: "error",
                message: "删除失败",
                });
              })
          })
      },
      //获取列表
      getList() {
          this.config.loading = true
          
           //如果是默认的select模式：
            if(this.currentModel==='select' || this.currentSearchKeyword ===''){

            selectAllRoleByLimit(this.config.page,7).then(({ data: res }) => {

                for(let i=0;i<res.data.length;i++){
                    //status
                    if(res.data[i].status===0){
                        res.data[i].status=true;
                    }else{
                        res.data[i].status=false;
                    }
                }
                this.tableData=res.data
                
                this.config.loading = false
            })

        }else if(this.currentModel==='search'){
            this.searchRole(this.currentSearchKeyword);
        }
          

      },
      //搜索角色
      searchRole(keyword){
        //切换成搜索模式
        this.currentModel='search';

        //记录当前搜索的关键字
        this.currentSearchKeyword=keyword;

        //开始搜索
        searchRoleByRoleNameAndLimit(keyword,this.config.page, 7).then(({ data: res })=>{

            for(let i=0;i<res.data.length;i++){
                    //status
                    if(res.data[i].status===0){
                        res.data[i].status=true;
                    }else{
                        res.data[i].status=false;
                    }
            }

            //更新数据
            this.tableData = res.data;

            //更新分页
            this.getsearchRoleCount(keyword);



        })

      }
      ,
      //获取角色总数量
      getRoleCount(){
         selectAllRoleCount().then(res=>{
           this.config.total=res.data.data
        })
      },
      //获取搜索记录数
      getsearchRoleCount(keyword){
            searchRoleCountByRoleName(keyword).then(res=>{
                this.config.total = res.data.data;
            })
      },
      //当点击分配菜单选项时弹出dialog之前执行的方法
      assignMenu(row) {
        //记录当前需要分配菜单的角色id
        this.currentAssignMenuRoleId=row.id;
        //打开分配菜单dialog
        this.assignMenuDialogShow = true;

        //清空之前的数据
        this.menuData=[];
        this.assignMenuDefaultCheckedArray=[];

        //给menuData放数据
        buildAssignMenuTree().then(res=>{
            //把后台拿到的json数组转成Array
            this.menuData=JSON.parse(res.data.data)

            //设置默认选中
            selectRoleCheckedMenuByRoleId(this.currentAssignMenuRoleId).then(res=>{
                
                //后端穿过来对象数据，我们要对这个数据进行下面的一些改变
                let objectsData=res.data.data;
                //由于默认选中设置只认[3001,3003]格式，所以我们要对后端数据进行一个处理
                this.assignMenuDefaultCheckedArray=[];

                //如果这个数据有菜单，则把这个数据push到assignMenuDefaultCheckedArray数组中
                if(objectsData.length !== 0){
                    for(let i=0; i<objectsData.length;i++){
                        this.assignMenuDefaultCheckedArray.push(objectsData[i].id);
                    }
                }

            })
           
        })
        
      },
      //当点击确认分配菜单时回调的方法
      confirmAssignMenu(){

        let assignMenuDTO={
            menuList: this.$refs.tree.getCheckedKeys(),
            roleid: this.currentAssignMenuRoleId
        }
        
        assignMenu(assignMenuDTO).then(res=>{
                this.$message({
                showClose: true,
                message: "分配菜单成功",
                type: "success",
                duration: 1000,
              });
              this.assignMenuDialogShow=false;
        }).catch(err=>{
                this.$message({
                showClose: true,
                message: "分配菜单失败",
                type: "error",
                duration: 1000,
            });
        })

      },
      //导出所有角色信息
      exportAllRole(){

        this.$confirm('是否导出所有角色的数据?', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(() => {
          //调用导出所有用户信息api
          exportAllRole().then(response=>{
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
      this.getRoleCount();

  }
}
</script>
<style lang="less" scoped>

</style>
