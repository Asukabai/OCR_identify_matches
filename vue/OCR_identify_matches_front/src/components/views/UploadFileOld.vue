<!--<template>-->
<!--  <div class="uploadFile">-->
<!--    <section>-->
<!--      <div class="selectFileBox">-->
<!--        <el-upload-->
<!--          drag-->
<!--          multiple-->
<!--          :show-file-list="false"-->
<!--          :directory="true"-->
<!--          ref="uploadExcel"-->
<!--          action="/ss/service/uploadFileOperation"-->
<!--          :limit="limitNum"-->
<!--          name="file"-->
<!--          :auto-upload="false"-->
<!--          accept=".jpg,.jpeg,.png,.pdf"-->
<!--          :before-upload="beforeUploadFile"-->
<!--          :before-remove="removeFile"-->
<!--          :on-exceed="exceedFile"-->
<!--          :on-error="handleError"-->
<!--          :file-list="fileList"-->
<!--          :http-request="httpRequest"-->
<!--        >-->
<!--          <div class="img">-->
<!--            <img src="@/assets/upload.png" alt="">-->
<!--          </div>-->
<!--          <el-button size="small" type="primary">选择文件</el-button>-->
<!--          <div slot="tip" class="el-upload__tip">-->
<!--            支持点击或拖拽，一次性上传单个或多个文件，或整个文件夹文件-->
<!--          </div>-->
<!--        </el-upload>-->
<!--        <br/>-->
<!--&lt;!&ndash;        <el-button size="small" type="primary" @click="submitUpload" :disabled="!isCanClick">上传文件</el-button>&ndash;&gt;-->
<!--        <el-button size="small" type="primary" @click="submitUpload" >上传文件</el-button>-->
<!--        <el-button size="small" type="danger" @click="clearFileList">清空列表</el-button>-->
<!--        <el-button size="small" type="primary" @click="goReview">返回首页</el-button>-->
<!--      </div>-->
<!--    </section>-->
<!--  </div>-->
<!--</template>-->

<!--<script>-->
<!--import SensorFileRequest from "../../utils/sensorFile";-->

<!--export default {-->
<!--  data() {-->
<!--    return {-->
<!--      limitNum: 20,-->
<!--      fileList: [],-->
<!--      isCanClick: false,-->
<!--      filesToUpload: []-->
<!--    };-->
<!--  },-->
<!--  methods: {-->
<!--    goReview() {-->
<!--      this.$emit('setActive', 1);-->
<!--    },-->
<!--    removeFile(file, fileList) {-->
<!--      this.fileList = fileList;-->
<!--      this.filesToUpload = fileList;-->
<!--      this.isCanClick = fileList.length > 0;-->
<!--    },-->
<!--    beforeUploadFile(file) {-->
<!--      const extension = file.name.substring(file.name.lastIndexOf('.') + 1).toLowerCase();-->
<!--      const size = file.size / 1024 / 1024; // 将文件大小转换为MB-->
<!--      const allowedExtensions = ['jpg', 'jpeg', 'png', 'pdf'];-->
<!--      if (!allowedExtensions.includes(extension)) {-->
<!--        this.$message.error('请上传图片或PDF文件');-->
<!--        return false;-->
<!--      }-->
<!--      if (size > 20) {-->
<!--        this.$message.error('文件大小不得超过20M');-->
<!--        return false;-->
<!--      }-->
<!--      this.fileList.push(file); // 更新fileList-->
<!--      this.filesToUpload.push(file);-->
<!--      this.isCanClick = true; // 返回true允许文件添加到fileList-->
<!--      return true;-->
<!--    },-->
<!--    exceedFile(files, fileList) {-->
<!--      this.$message.warning(`一次最多只能选择 ${this.limitNum} 个文件，当前共选择了 ${files.length + fileList.length} 个`);-->
<!--    },-->
<!--    handleError(err) {-->
<!--      this.$message.error(err.msg);-->
<!--    },-->
<!--    submitUpload() {-->
<!--      this.$refs.uploadExcel.submit(); // 手动触发上传-->
<!--    },-->
<!--    httpRequest(item) {-->
<!--      const formData = new FormData();-->
<!--      this.filesToUpload.forEach(file => {-->
<!--        formData.append('multipartFiles', file);-->
<!--      });-->
<!--      this.importData(formData);-->
<!--      this.filesToUpload = []; // 清空已上传文件列表-->
<!--    },-->
<!--    importData(formData) {-->
<!--      SensorFileRequest.uploadFileOperation(formData).then((res) => {-->
<!--        const resData = JSON.parse(res.data);-->
<!--        if (resData.result === 1) {-->
<!--          this.$message.success('上传成功');-->
<!--        } else {-->
<!--          this.$message.error('上传失败');-->
<!--        }-->
<!--      }).catch((err) => {-->
<!--        this.$message.error('上传失败: ' + err.message);-->
<!--      });-->
<!--    },-->
<!--    clearFileList() {-->
<!--      this.fileList = [];-->
<!--      this.filesToUpload = [];-->
<!--      this.isCanClick = false;-->
<!--    }-->
<!--  }-->
<!--};-->
<!--</script>-->






<!--<style scoped>-->
<!--.uploadFile {-->
<!--  display: flex;-->
<!--  justify-content: center;-->
<!--  align-items: center;-->
<!--  height: 100vh; /* Full viewport height to center the content */-->
<!--  background-color: #f7f7f7; /* Optional: Set background color */-->
<!--}-->

<!--.selectFileBox {-->
<!--  width: 120%; /* Adjust width as needed */-->
<!--  max-width: 1000px; /* Optional: Set max width to avoid too large screens */-->
<!--  padding: 40px;-->
<!--  background: white;-->
<!--  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);-->
<!--  border-radius: 18px;-->
<!--}-->

<!--.el-upload__inner {-->
<!--  width: 60px; /* Set width to make the box smaller */-->
<!--  height: 60px; /* Set height to make the box square */-->
<!--  padding: 0; /* Remove padding */-->
<!--  border: 2px dashed #ccc;-->
<!--  border-radius: 8px;-->
<!--  background: #f9f9f9;-->
<!--  text-align: center;-->
<!--  display: flex;-->
<!--  justify-content: center;-->
<!--  align-items: center;-->
<!--}-->

<!--.el-upload__inner:hover {-->
<!--  border-color: #409EFF; /* Change border color on hover */-->
<!--}-->

<!--.el-upload__tip {-->
<!--  margin-top: 10px;-->
<!--  font-size: 14px;-->
<!--  color: #999;-->
<!--}-->

<!--.el-button {-->
<!--  margin-top: 10px;-->
<!--}-->

<!--.img img {-->
<!--  width: 105px; /* Adjust image size to fit within the square */-->
<!--  height: 80px; /* Ensure the image remains square */-->
<!--  margin-bottom: 20px;-->
<!--}-->
<!--</style>-->




<!--<template>-->
<!--  <div>-->
<!--    <h1>文件上传录入识别系统</h1>-->
<!--    <form ref="uploadForm" @submit="uploadFormSubmit" enctype="multipart/form-data">-->
<!--      <label for="fileInput">选择文件：</label>-->
<!--      <input type="file" id="fileInput" name="files[]" accept=".jpg,.jpeg,.png,.pdf" multiple @change="handleFileChange($event)">-->
<!--      <el-button type="primary" @click="uploadFormSubmit">上传文件</el-button>-->
<!--      <el-button type="primary" @click="clearFileSelection">清空列表</el-button>-->
<!--    </form>-->
<!--  </div>-->
<!--</template>-->

<!--<script>-->
<!--import axios from "axios";-->

<!--export default {-->
<!--  data() {-->
<!--    return {-->
<!--      files: [], // 存储文件的数组-->
<!--      fileUrls: null,-->
<!--    };-->
<!--  },-->
<!--  methods: {-->
<!--    handleFileChange(event) {-->
<!--      this.files = Array.from(event.target.files);-->
<!--    },-->
<!--    uploadFormSubmit(e) {-->
<!--      e.preventDefault();-->
<!--      const formData = new FormData();-->

<!--      if (this.files.length === 0) {-->
<!--        alert('请选择一个或多个文件！');-->
<!--        return;-->
<!--      }-->

<!--      this.files.forEach(file => {-->
<!--        formData.append('multipartFiles', file);-->
<!--      });-->

<!--      this.showLoadingMessage('文件上传中...');-->
<!--      axios.post('/manage/server/uploadExcels', formData)-->
<!--        .then((response) => {-->
<!--          const data = response.data;-->
<!--          if (data.result === 1 && data.data.length > 0) {-->
<!--            this.fileUrls = data.data;-->
<!--            this.showMessage('文件上传完成！', 'success');-->
<!--          }-->
<!--        })-->
<!--        .catch((error) => {-->
<!--          console.error('上传失败:', error);-->
<!--          this.showMessage('文件上传失败，请重试！', 'error');-->
<!--        });-->
<!--    },-->
<!--    clearFileSelection() {-->
<!--      this.files = [];-->
<!--      const input = this.$refs.uploadForm.querySelector('#fileInput');-->
<!--      if (input) {-->
<!--        input.value = ''; // 清空文件输入框-->
<!--      }-->
<!--    },-->
<!--    showLoadingMessage(message) {-->
<!--      const loadingElement = document.createElement('div');-->
<!--      loadingElement.classList.add('loading');-->
<!--      loadingElement.innerText = message;-->
<!--      this.$refs.uploadForm.appendChild(loadingElement);-->
<!--    },-->
<!--    showMessage(message, type) {-->
<!--      const messageElement = document.createElement('div');-->
<!--      messageElement.classList.add(type);-->
<!--      messageElement.innerText = message;-->
<!--      this.$refs.uploadForm.appendChild(messageElement);-->
<!--    }-->
<!--  }-->
<!--}-->
<!--</script>-->

<!--<style>-->
<!--.loading {-->
<!--  color: orange;-->
<!--  margin-top: 10px;-->
<!--}-->

<!--.success {-->
<!--  color: green;-->
<!--  margin-top: 10px;-->
<!--}-->

<!--.error {-->
<!--  color: red;-->
<!--  margin-top: 10px;-->
<!--}-->
<!--</style>-->


<!--        <span style="width: 50px;"></span> &lt;!&ndash; 添加一个宽度为10px的空span &ndash;&gt;-->
