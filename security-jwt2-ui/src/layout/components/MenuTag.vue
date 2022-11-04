<template>
    <div class="tabs">
        <el-tag
            v-for="(menuTag, index) in MenuTags"
            :key="menuTag.path"
            :closable="menuTag.path !== '/dashboard'"
            :effect="$route.path === menuTag.path ? 'dark' : 'plain'"
            @click="clickMenuTag(menuTag)"
            @close="handleClose(menuTag, index)"
            size="small"
        >
            {{ menuTag.menuName }}
        </el-tag>
    </div>
</template>
<script>
import { mapState } from 'vuex'
export default {
    name: 'MenuTag',
    data() {
        return {

        }
    },
    computed: {
        ...mapState({
            MenuTags: state => state.menuTag.MenuTagList
        })
    },
    methods: {
        //点击菜单标签就可以根据组件路径进行路由跳转
        clickMenuTag(item) {
            this.$router.push({ path: item.path })
            //更新当前侧边栏菜单高亮的index
            this.$store.dispatch('setCurrentNavMenuActiveIndex',item.path)
        },
        //关闭菜单标签.tag就是删除的标签
        handleClose(tag, index) {
            const length = this.MenuTags.length - 1
            //调用actions删除tag标签
            this.$store.dispatch('closeMenu',tag)
            //如果删除的菜单标签路径不等于当前所在的路由路径，直接return，不进行任何路由跳转
            if (tag.path !== this.$route.path) {
                return;
            }
            
            if (index === length) {
                this.$router.push({
                    path: this.MenuTags[index - 1].path
                })
                //更新当前侧边栏菜单高亮的index
                this.$store.dispatch('setCurrentNavMenuActiveIndex',this.MenuTags[index - 1].path)
            } else {
                this.$router.push({
                    path: this.MenuTags[index].path
                })
                //更新当前侧边栏菜单高亮的index
                this.$store.dispatch('setCurrentNavMenuActiveIndex',this.MenuTags[index].path)
            }
        }
    }
}
</script>
<style lang="less" scoped>
.tabs {
    padding: 20px;
    .el-tag {
        margin-right: 15px;
        cursor: pointer;
    }
}
</style>
