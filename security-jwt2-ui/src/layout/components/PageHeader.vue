<template>
  <header>
    <div class="l-content">
      <el-button
        @click="handleMenu"
        plain
        icon="el-icon-menu"
        size="mini">
    </el-button>

 <!-- 面包屑。注意面包屑的点击事件要加@click.native -->
 <el-breadcrumb separator=">">

        <el-breadcrumb-item
          v-for="item in MenuTags"
          :key="item.path"
          @click.native="clickBreadCrumb(item)">
      
        
          <span v-if="$route.path === item.path">
            
              {{ item.menuName }}
            
          </span>
          <span v-if="$route.path !== item.path">
            <a>
              {{ item.menuName }}
            </a>
          </span>
        
        </el-breadcrumb-item>

  </el-breadcrumb>

</div>



<div class="r-content">
      <el-dropdown trigger="click" size="mini">
        <span>
          <div class="block">
            <el-avatar :size="40" :src="avatar"></el-avatar>
          </div>
        </span>
        <el-dropdown-menu slot="dropdown">
          <el-dropdown-item @click.native="logout">退出</el-dropdown-item>
        </el-dropdown-menu>
      </el-dropdown>
 </div>
  </header>
</template>

<script>
import { mapState } from 'vuex'
import{
  logout
}from '../../api/logout'

export default {
  name: "PageHeader",
  data() {
    return {
    };
  },
  methods: {
    // 伸缩侧边菜单栏
    handleMenu() {
      this.$store.dispatch('collapseMenu')
    },
    //点击面包屑调用的方法
    clickBreadCrumb(item){
            this.$router.push({ path: item.path })
            //更新当前侧边栏菜单高亮的index
            this.$store.dispatch('setCurrentNavMenuActiveIndex',item.path)
    },
    //退出登录
    logout() {

      //拿到token
      let accessToken=localStorage.getItem('accessToken');
      let refreshToken=localStorage.getItem('refreshToken');

      logout(accessToken,refreshToken).then(res=>{
        
        //调用vuex的方法，进行退出成功后的处理（比如清空localstorage和vuex数据）
        this.$store.dispatch('logoutSuccess');
        this.$message({
                showClose: true,
                message: "用户退出成功",
                type: "success",
                duration: 1000,
        });
        //跳转到登录页。
        //this.$router.replace和push区别是这个replace跳转后不允许使用浏览器回退功能
        //如果是push跳转页面则可以回退，对于这个退出功能来说建议使用replace
        this.$router.replace({ path: '/login' })

      }).catch(err=>{
        this.$message({
                showClose: true,
                message: "用户退出失败",
                type: "error",
                duration: 1000,
        });
      })
      
    }
  },
  computed: {
        ...mapState({
          MenuTags: state => state.menuTag.MenuTagList
        })
        ,
        avatar(){
          return this.$store.state.user.avatar;
        }
  }
};
</script>

<style lang="less" scoped>

/* 面包屑不被选中时的颜色 */
.el-breadcrumb ::v-deep .el-breadcrumb__inner {
        color: #d9bb95 !important;
        font-weight:400 !important;
}
/* 面包屑被选中时的颜色 */


header {
  display: flex;
  height: 100%;
  justify-content: space-between;
  align-items: center;
}
.l-content {
  display: flex;
  align-items: center;
  .el-button {
    margin-right: 20px;
  }
}
/deep/ .el-breadcrumb__item {
  .el-breadcrumb__inner {
    font-weight: normal;
    &.is-link {
      color: #666;
    }
    a {
      color: #666;
    }
  }
  &:last-child {
    .el-breadcrumb__inner {
      color: #fff;
    }
  }
}


</style>