<template>
    
<el-container style="height: 100%">
   <!-- 侧边栏区域 -->
  <el-aside width="auto">
    <!-- 侧边栏菜单布局实现 -->
    <nav-menu></nav-menu>
  </el-aside>
  <el-container>
    <el-header>
        <!-- 页头 -->
        <page-header></page-header>
    </el-header>

    <!-- 页头下面的菜单tag标签组件（记住这个不能在header里面放） -->
    <menu-tag></menu-tag>

    <el-main>
        <!-- Main区域路由页面 -->
        <router-view></router-view>
    </el-main>
    <el-footer>
        <!-- 页尾 -->
        <page-footer></page-footer>
    </el-footer>
  </el-container>
</el-container>

</template>

<script>
import NavMenu from '../layout/components/NavMenu.vue'
import PageFooter from '../layout/components/PageFooter.vue'
import PageHeader from '../layout/components/PageHeader.vue'
import MenuTag from '../layout/components/MenuTag.vue'

export default {
    name:'Layout',
    components:{
        NavMenu,
        PageFooter,
        PageHeader,
        MenuTag
    }
}
</script>

<style scoped>
.el-header{
    border-bottom: #DCDFE6 solid;
}
.el-main {
    padding-top: 0;
}
.el-footer{

    width: 90%;
    text-align: center;

}

</style>