<template>
  <!-- 

    解决elementui菜单栏收缩，但是多级菜单字体不消失的bug。解决方式为：首先引入vue-fragment，
    然后将最外层div改成fragment标签。
    参考文档：https://www.cnblogs.com/qiunanyan/p/14240272.html
   -->
    <fragment>
      <template v-for="(item, index) in data">
        <!-- 因为有子集和无子集渲染html标签不一样，所以要分为两种情况
             情况一：有子集的情况：(这种情况下的导航不可以点击跳转路由) 
            由于这里的导航只是一个目录，所以要设置:index="item.id+''"
            -->
        <el-submenu
          :key="index"
          :index="item.id+''"
          v-if="item.children.length > 0">

          <template slot="title">
            <i :class="item.icon"></i>
            <span slot="title">{{ item.menuName }}</span>
          </template>
          <!-- 递归子菜单 -->
          <NavMenuItem :data="item.children"></NavMenuItem>
        
        </el-submenu>


        
        <!-- 情况二：没子集的情况：(这种情况下的导航就可以点击跳转路由)
        由于这里的导航是会跳转到路由的，所以要设置v-else :index="item.path+''"
        elementui会根据:index中的path值去跳转
        -->
        <el-menu-item 
        :key="index"
        v-else 
        :index="item.path+''"
        @click="clickMenu(item)"
        >
          <i :class="item.icon"></i>
          <span slot="title">{{ item.menuName }}</span>

        </el-menu-item>
        
      </template>
    </fragment>
  </template>
  
  <script>
import { Fragment } from 'vue-fragment'
  export default {
    name: "NavMenuItem",
    components: { 
      Fragment 
    },
    props: {
      data: {
        type: Array,
        default: [],
      }
    },
    methods:{
      clickMenu(item) {
         //当点击会跳转路由的菜单时，会把这个菜单存储到MenuTag的vuex中，实现面包屑和菜单标签的数据添加
          this.$store.dispatch('addMenuTag', item)
          //更新当前侧边栏菜单高亮的index
          this.$store.dispatch('setCurrentNavMenuActiveIndex',item.path)
      }
    }
    // 注意： 在template标签上使用v-for，:key="index"不能写在template标签上，因为其标签不会被渲染，会引起循环错误
  };
  </script>