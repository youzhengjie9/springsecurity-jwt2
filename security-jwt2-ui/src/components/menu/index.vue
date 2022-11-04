<template>
  <div>
    <el-table
      :data="tableData"
      style="width: 100%; margin-bottom: 20px"
      row-key="id"
      border
      stripe
      default-expand-all
      :tree-props="{ children: 'children', hasChildren: 'hasChildren' }"
    >
      <el-table-column prop="menuName" label="菜单名称" width="180" show-overflow-tooltip>
      </el-table-column>

      <el-table-column prop="icon" label="图标" width="53">
      
        <!-- 使用slot将默认展示文本的机制替换成展示icon -->
        <template slot-scope="scope">
          <i :class="scope.row.icon"></i>
        </template>
        
      </el-table-column>

      <el-table-column
        prop="type"
        label="菜单类型"
        width="78"
        filter-placement="bottom-end"
      >
        <template slot-scope="scope">
          <!-- 目录类型 -->
          <el-tag
            v-if="scope.row.type === 0"
            type="primary"
            disable-transitions
            >目录
          </el-tag>
          <!-- 菜单类型 -->
          <el-tag
            v-if="scope.row.type === 1"
            type="success"
            disable-transitions
            >菜单
          </el-tag>
          <!-- 按钮类型 -->
          <el-tag
            v-if="scope.row.type === 2"
            type="danger"
            disable-transitions
            >按钮
          </el-tag>

        </template>
      </el-table-column>

      <el-table-column prop="path" label="路由地址" width="105" show-overflow-tooltip>
      </el-table-column>

      <el-table-column prop="component" label="组件路径" width="220" show-overflow-tooltip>
      </el-table-column>

      <el-table-column
        prop="status"
        label="菜单状态"
        width="78"
        filter-placement="bottom-end"
      >
        <template slot-scope="scope">
          <!-- 菜单状态 -->
          <el-tag
            v-if="scope.row.status === 0"
            type="success"
            disable-transitions
            >正常
          </el-tag>
          <!-- 菜单状态 -->
          <el-tag
            v-if="scope.row.status === 1"
            type="danger"
            disable-transitions
            >停用
          </el-tag>
        </template>
    </el-table-column>

      <el-table-column
        prop="visible"
        label="菜单是否显示"
        width="100"
        filter-placement="bottom-end"
      >
        <template slot-scope="scope">
          <!-- 目录类型 -->
          <el-tag
            v-if="scope.row.visible === 0"
            type="success"
            disable-transitions
            >显示
          </el-tag>
          <!-- 菜单类型 -->
          <el-tag
            v-if="scope.row.visible === 1"
            type="danger"
            disable-transitions
            >隐藏
          </el-tag>
        </template>
    </el-table-column>

      <el-table-column prop="perms" label="权限标识" width="150" show-overflow-tooltip>
    </el-table-column>

      <el-table-column prop="sort" label="排序" width="73" sortable show-overflow-tooltip>
        
      </el-table-column>

      

      <el-table-column label="操作" min-width="180">
        <template slot-scope="scope">
          <el-button v-hasPerm="['sys:menu:list:update']" size="mini" type="primary" @click="handleEdit(scope.row)">编辑</el-button>
          <el-button v-hasPerm="['sys:menu:list:delete']" size="mini" type="danger" @click="handleDelete(scope.row)"
            >删除</el-button
          >
        </template>
      </el-table-column>
    </el-table>
  </div>
</template>

<script>
export default {
  name: "MenuTable",
  props: {
    tableData: {
      type: Array,
      default: [],
    },
  },
  data() {
    return {};
  },
  methods: {
    handleEdit(row) {
          this.$emit('edit', row)
    },
    handleDelete(row) {
          this.$emit('del', row)
    }
  },
};
</script>