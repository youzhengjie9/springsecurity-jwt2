<template>
    <el-form ref="form" label-width="100px" :model="form" :inline="inline">
        <!-- <el-form-item v-for="item in formLabel" :key="item.label" :label="item.label"> -->
            <el-form-item v-for="item in formLabel" :key="item.label">
            <!-- 如果指定的类型是input -->
            <el-input
                v-if="item.type === 'input'"
                :placeholder="'请输入' + item.label"
                v-model="form[item.model]"
            ></el-input>
            
        </el-form-item>
        <el-form-item><slot></slot></el-form-item>
    </el-form>
</template>
<script>


export default {
    name: 'SearchForm',
    props: {
        formLabel: Array,
        form: Object,
        inline: Boolean
    },
    data () {
        return {}
    }
}
</script>