<template>
    <div class="common-table">
        <el-table
            :data="tableData"
            border
            stripe
            style="width: 100%">

            <el-table-column prop="id" label="id" width="100" show-overflow-tooltip sortable>

            </el-table-column>

            <el-table-column prop="userName" label="用户名" width="100" show-overflow-tooltip>
                
            </el-table-column>

            <el-table-column prop="nickName" label="昵称" width="100" show-overflow-tooltip>
                
            </el-table-column>
            <el-table-column prop="avatar" label="头像" width="60">
      
                <!-- 使用slot将默认展示文本的机制替换成展示avatar -->
                <template slot-scope="scope">
                    <el-avatar :size="28" :src="scope.row.avatar"></el-avatar>
                </template>
            
            </el-table-column>

            <el-table-column
                prop="status"
                label="用户状态"
                width="78">
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

            <el-table-column prop="email" label="邮箱" width="100" show-overflow-tooltip>
                
            </el-table-column>

            <el-table-column prop="phone" label="手机号" width="100" show-overflow-tooltip>
                
            </el-table-column>

            <el-table-column
                prop="sex"
                label="性别"
                width="78">
                <template slot-scope="scope">
                
                <el-tag
                    v-if="scope.row.sex === '男'"
                    type="primary"
                    disable-transitions
                    >男
                </el-tag>
                
                <el-tag
                    v-if="scope.row.sex === '女'"
                    type="success"
                    disable-transitions
                    >女
                </el-tag>

                <el-tag
                    v-if="scope.row.sex === '未知'"
                    type="danger"
                    disable-transitions
                    >未知
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

           
            <el-table-column label="操作" min-width="180">
                <template slot-scope="scope">
                    <el-button v-hasPerm="['sys:user:list:update']" size="mini" type="primary" @click="handleEdit(scope.row)">编辑</el-button>
                    <el-button v-hasPerm="['sys:user:list:assign-role']" type="warning" size="mini" @click="handleAssignRole(scope.row)">分配角色</el-button>
                    <el-button v-hasPerm="['sys:user:list:delete']" size="mini" type="danger" @click="handleDelete(scope.row)">删除</el-button>
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
        //当点击分配角色选项时弹出dialog之前执行的方法
        handleAssignRole(row){
            this.$emit('assignRole', row)
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
        // bottom: 0;
        right: 20px
    }
</style>
