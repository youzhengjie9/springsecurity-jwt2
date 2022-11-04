<template>
    <div class="common-table">
        <el-table
            :data="tableData"
            border
            stripe
            style="width: 100%">

            <el-table-column prop="id" label="id" width="130" show-overflow-tooltip sortable>

            </el-table-column>

            <el-table-column prop="name" label="角色名称" width="135" show-overflow-tooltip>
                
            </el-table-column>

            <el-table-column prop="roleKey" label="角色关键字" width="150" show-overflow-tooltip>
                
            </el-table-column>

            <el-table-column
                prop="status"
                label="角色状态"
                width="85">
                <template slot-scope="scope">
                <el-tag
                    v-if="scope.row.status === true"
                    type="success"
                    disable-transitions
                    >正常
                </el-tag>
                <el-tag
                    v-if="scope.row.status === false"
                    type="danger"
                    disable-transitions
                    >停用
                </el-tag>
                </template>
            </el-table-column>

            <el-table-column prop="createTime" label="创建时间" width="110" show-overflow-tooltip sortable>
                <template slot-scope="scope">
                    <span>{{scope.row.createTime | dateformat('YYYY-MM-DD')}}</span>
                </template>
            </el-table-column>   
            
            <el-table-column prop="updateTime" label="最后一次修改时间" width="180" show-overflow-tooltip sortable>
                <template slot-scope="scope">
                    <span>{{scope.row.updateTime | dateformat('YYYY-MM-DD HH:mm:ss')}}</span>
                </template>
            </el-table-column>   

            <el-table-column prop="remark" label="备注" width="150" show-overflow-tooltip>
                
            </el-table-column>

           
            <el-table-column label="操作" min-width="180">
                <template slot-scope="scope">
                    <el-button v-hasPerm="['sys:role:list:update']" size="mini" type="primary" @click="handleEdit(scope.row)">编辑</el-button>
                    <el-button v-hasPerm="['sys:role:list:assign-menu']" type="warning" size="mini" @click="handleAssignMenu(scope.row)">分配菜单</el-button>
                    <el-button v-hasPerm="['sys:role:list:delete']" size="mini" type="danger" @click="handleDelete(scope.row)">删除</el-button>
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
    name: 'UserTable',
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
        handleEdit(row) {
            this.$emit('edit', row)
        },
        handleDelete(row) {
            this.$emit('del', row)
        },
        changePage(page) {
           this.$emit('changePage', page)
        },
        //当点击分配菜单选项时弹出dialog之前执行的方法
        handleAssignMenu(row){
            this.$emit('assignMenu', row)
        }
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
