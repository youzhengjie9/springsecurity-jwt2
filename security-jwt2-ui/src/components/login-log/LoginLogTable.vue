<template>
  <div class="common-table">
        <el-table
            border
            stripe
            ref="multipleTable"
            :data="tableData"
            tooltip-effect="dark"
            style="width: 100%"
            @selection-change="handleSelectionChange"
            >

            <el-table-column
                type="selection"
                width="55">
            </el-table-column>

            <el-table-column prop="id" label="id" width="130" show-overflow-tooltip sortable>

            </el-table-column>

            <el-table-column prop="username" label="用户名" width="135" show-overflow-tooltip>
                
            </el-table-column>

            <el-table-column prop="ip" label="ip" width="150" show-overflow-tooltip>
                
            </el-table-column>

            <el-table-column prop="address" label="ip所在地" width="150" show-overflow-tooltip>
                
            </el-table-column>

            <el-table-column prop="browser" label="使用的浏览器" width="150" show-overflow-tooltip>
                
            </el-table-column>

            <el-table-column prop="os" label="使用的操作系统" width="150" show-overflow-tooltip>
                
            </el-table-column>

             
            
            <el-table-column prop="loginTime" label="用户登录时间" width="180" show-overflow-tooltip sortable>
                <template slot-scope="scope">
                    <span>{{scope.row.loginTime | dateformat('YYYY-MM-DD HH:mm:ss')}}</span>
                </template>
            </el-table-column>   

           
            <el-table-column label="操作" min-width="100">
                <template slot-scope="scope">
                    <el-button v-hasPerm="['sys:log:login:delete']" size="mini" type="danger" @click="handleDelete(scope.row)">删除</el-button>
                </template>
            </el-table-column>
        </el-table>
        <!-- 分页 -->
        
        <el-pagination
                background
                class="pager"
                layout="prev, pager, next"
                :total="config.total"
                :page-size="7"
                :current-page.sync="config.page"
                @current-change="changePage"
                >
        </el-pagination>
    </div>
</template>

<script>
export default {
    name:'LoginLogTable',
    props: {
        tableData: {
            type: Array,
            default: [],
        },
        config: Object
    },
    data() {
        return {}
    },
    methods: {
        handleDelete(row) {
            this.$emit('del', row)
        },
        changePage(page) {
           this.$emit('changePage', page)
        },
        handleSelectionChange(val) {
            this.multipleSelection = val;
        },
    }
}
</script>

<style lang="less" scoped>
.common-table {
    height: calc(100% - 62px);
    background-color: #fff;
    position: relative;
}
.pager {
        position: absolute;
        bottom: 10;
        right: 20px
    }
</style>