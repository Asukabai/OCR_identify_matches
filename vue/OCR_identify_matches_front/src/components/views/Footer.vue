<template>
  <footer class="footer">
    <p>&copy; {{ currentYear }} 供应商产品管理系统 V1.0.1</p>
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
