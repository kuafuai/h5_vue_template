<script setup>

console.log("stepup")
</script>

<style>
/*每个页面公共css */
 ::v-deep .uni-popup__wrapper{
    width: 80%;
    
}
body {
margin: 0;  /* 如果页面出现垂直滚动条，则加入此行CSS以消除之 */
}
pages{
  height: 100vh;
}
uni-left-window{
  margin-right: 1rem;
  background: white;
  padding-right: 2rem;
}
.uni-forms{
  width: 90%;
  max-width: 410px;
}
.main-header{
  display: none;
}
::v-deep .el-container{
    width: 99%;
    height: 95%;
}
</style>
