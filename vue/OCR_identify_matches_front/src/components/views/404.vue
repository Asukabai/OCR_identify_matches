<template>
  <div class="_404">
    <h2 class="m-0">抱歉，页面未找到，<span>{{countDown}}</span>s后自动跳转到
      <a href="javascript:;" @click="goHome">首页</a>
    </h2>
  </div>
</template>

<script>
export default {
  name: "NF404",
  data() {
    return {
      countDown: 5,
      timer: null
    }
  },
  methods: {
    goHome() {
      this.$router.push({path:'/ssPrice/web/login'})
      clearInterval(this.timer)
    }
  },
  created() {
    this.timer = setInterval(() => {
      this.countDown > 0 ? this.countDown-- : this.goHome()
    }, 1000)
  }
}
</script>

