<template>
  <footer class="footer">
    <p>&copy; {{ currentYear }} 晟思文件录入识别系统 V2.0.1</p>
  </footer>
</template>

<script>
export default {
  computed: {
    currentYear() {
      return new Date().getFullYear();
    }
  }
};
</script>

<style scoped>
.footer {
  margin-top: 20px;
  font-size: 15px;
  color: #666;
}
</style>
