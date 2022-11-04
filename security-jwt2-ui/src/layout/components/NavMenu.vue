<template>
  <el-menu
  :default-active="currentActiveMenuIndex"
  class="el-menu-vertical-demo"
  background-color="#545c64"
  text-color="#fff"
  active-text-color="#ffd04b"
  router 
  :collapse="isCollapse"
  >
    <!-- 后台系统左上角图标 -->
    <img class="menuTopImg" src="@/assets/logo.png" />

  <!-- 递归生成动态多级菜单 -->
  <NavMenuItem :data="menuData"></NavMenuItem>
    
  </el-menu>
</template>

<style scoped>

.el-menu-vertical-demo:not(.el-menu--collapse) {
  width: 200px;
  min-height: 400px;
}
/* .el-submenu__title:hover, .el-menu-item:focus, .el-menu-item:hover{
background-color: red;
} */
.el-menu {
  height: 100%;
  border: none;
}
.menuTopImg {
  width: 35px;
  height: 35px;
  display: block;
  margin: 0 auto;
  margin-top: 15px;
}
</style>

<script>
import NavMenuItem from '../../layout/components/NavMenuItem.vue'

export default {
  name: "NavMenu",
  components:{
    NavMenuItem
  },
  data() {
    return {
      //侧边栏数据
      menuData: []
    }
  },
  computed: {
    // 通过从vuex中拿到isCollapse共享数据，控制伸缩菜单栏
    isCollapse() {
        return this.$store.state.menuTag.isCollapse
    },
    currentActiveMenuIndex(){
      return this.$store.state.menu.currentNavMenuActiveIndex
    }
  },
  methods:{
    //获取当前用户的动态菜单数据
    getUserDynamicMenu(){
       //从vuex中获取动态菜单存到menuData并展示出来
       this.menuData=this.$store.state.user.dynamicMenu
    }
  },
  created(){
    this.getUserDynamicMenu();
  }

};
</script>