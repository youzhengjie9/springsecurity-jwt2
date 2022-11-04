<template>
  <div id="app">
    <router-view/>
  </div>
</template>



<script>
  export default {
    name: "App"
  }
</script>



<style>


/* 自定义nprogress进度条颜色样式*/
#nprogress .bar {
   background: blue !important;
}

html, body {
  margin: 0;
  padding: 0;
}
#app {
  height: 100vh;
}
</style>
