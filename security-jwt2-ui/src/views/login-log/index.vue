<template>

<div>

    <!-- 表格的头部,例如导入、搜索框。 -->
    <el-row :gutter="20">
        <el-col :span="6">
          <el-button type="primary" @click="exportAllLoginLog"> 导出</el-button> 
        </el-col>
         <!-- 搜索表单组件 -->
        <el-col :span="6" :offset="12">
          
            <search-form
              :formLabel="formLabel"
              :form="searchForm"
              :inline="true"
              ref="form">
              <el-button type="primary" @click="searchLoginLog(searchForm.keyword)">搜索</el-button>
          </search-form>

          

        </el-col>
   </el-row>

   <!-- 表格内容 -->
    <login-log-table
          :tableData="tableData"
          :config="config"
          @changePage="getList()"
          @del="deleteLoginLog">

    </login-log-table>

</div>

    

</template>
<script>
import{
    selectAllLoginLogByLimit,
    selectAllLoginLogCount,
    deleteLoginLog,
    searchLoginLogByUserNameAndLimit,
    searchLoginLogCountByUserName
}from '../../api/login-log'

//要写成这种{}的形式导入export-excel，否则将导出不了
import{
    exportAllLoginLog
}from '../../api/export-excel'

import SearchForm from '@/components/common/SearchForm.vue'
import LoginLogTable from '../../components/login-log/LoginLogTable.vue'

export default {
    name:'LoginLog',
    components:{
        SearchForm,
        LoginLogTable
    },
    data(){
        return{
            
            tableData:[],
            multipleSelection: [],
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
            //当前模式：正常查询或者搜索（默认是查询）
            currentModel:'select',
            //当前搜索的关键字
            currentSearchKeyword:'',
            config: {
                page: 1,
                total: 1
           },
        }
    },
    methods:{
        getList(){
            
            //如果是默认的select模式：
            if(this.currentModel==='select' || this.currentSearchKeyword ===''){

                selectAllLoginLogByLimit(this.config.page,7).then(({ data: res }) => {

                    this.tableData=res.data
                })

            }else{
                this.searchLoginLog(this.currentSearchKeyword);
            }

        },

        //搜索
        searchLoginLog(keyword){
            //切换成搜索模式
            this.currentModel='search';

            //记录当前搜索的关键字
            this.currentSearchKeyword=keyword;

            searchLoginLogByUserNameAndLimit(keyword,this.config.page, 7).then(({ data: res })=>{


                //更新数据
                this.tableData = res.data;
        
                //更新分页
                this.getsearchLoginLogCount(keyword);
                
            })



        },
         //获取总数量
        getLoginLogCount(){
            selectAllLoginLogCount().then(res=>{
                this.config.total=res.data.data
            })
        },
        //获取搜索记录数
        getsearchLoginLogCount(keyword){

            searchLoginLogCountByUserName(keyword).then(res=>{
                this.config.total = res.data.data;
            })
        },
        //导出所有登录日志
        exportAllLoginLog(){

            this.$confirm('是否导出所有登录日志的数据?', '提示', {
                confirmButtonText: '确定',
                cancelButtonText: '取消',
                type: 'warning'
            }).then(() => {
               
            //调用导出所有登录日志信息api
            exportAllLoginLog().then(response=>{
               
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
        },
        deleteLoginLog(row){

            this.$confirm("是否删除该条登录日志？", "提示", {
                confirmButtonText: "确认",
                cancelButtonText: "取消",
                type: "warning",
            }).then(() => {
                //id
                const id = row.id;
                deleteLoginLog(id)
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

        }
    },
    created(){
        this.getList();
        this.getLoginLogCount();
    }
}
</script>

