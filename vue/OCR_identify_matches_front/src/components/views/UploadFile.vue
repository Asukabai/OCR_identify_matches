<template>
  <div>
    <h1 class="title">文件上传录入识别</h1>
    <div class="container">
      <div class="upload-section">
        <div class="description">
          说明: 由于解析能力限制，现仅支持上传图片或PDF类型。一次性最多可上传100个不同类型的文件
        </div>
        <el-form ref="uploadForm" enctype="multipart/form-data" class="form-container">
          <el-upload
            ref="upload"
            action="/ss/service/uploadFileOperation"
            :auto-upload="false"
            :on-change="handleFileChange"
            :file-list="fileList"
            :on-remove="handleRemove"
            :accept="'.jpg,.jpeg,.png,.pdf'"
            multiple>
            <el-button slot="trigger" type="primary" :disabled="isUploading">选择（一个或多个）文件</el-button>
            <el-button type="primary" @click="submitUpload" :disabled="isUploading">上传录入文件</el-button>
            <el-button type="primary" @click="clearFileSelection" :disabled="isUploading">清空文件列表</el-button>
            <el-button type="primary" @click="goToPreview" :disabled="isUploading">返回预览界面</el-button>
          </el-upload>
          <div class="file-count">
            当前已选择文件数量: {{ selectedFileCount }} / 100
          </div>
        </el-form>
      </div>
      <div class="log-section">
        <h2>上传记录日志</h2>
        <div class="block">
          <div class="radio">
            排序方式：
            <el-radio-group v-model="reverse">
              <el-radio :label="true">倒序</el-radio>
              <el-radio :label="false">正序</el-radio>
            </el-radio-group>
          </div>
          <div style="height: 20px;"></div>
          <div class="timeline-container">
            <el-timeline :reverse="reverse">
              <el-timeline-item
                v-for="(activity, index) in activities"
                :key="index"
                :timestamp="activity.timestamp">
                {{ activity.content }}
              </el-timeline-item>
            </el-timeline>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>
<script>
import axios from "axios";
import { Message } from 'element-ui';
import SensorFileRequest from "../../utils/sensorFile";
export default {
    data() {
      return {
        fileList: [], // 存储文件的数组
        loadingMessage: null, // 用于存储加载消息实例
        reverse: false,
        activities: [], // 初始化为空数组
        isLoading: true, // 加载状态
        selectedFileCount: 0 ,
        isUploading: false // 新增属性，用于控制按钮的禁用状态
      };
    },
    methods: {
      handleFileChange(file, fileList) {
        this.fileList = fileList;
        this.selectedFileCount = fileList.length; // 更新文件数量
      },
      handleRemove(file, fileList) {
        this.fileList = fileList;
        this.selectedFileCount = fileList.length; // 更新文件数量
      },
      submitUpload() {
        if (this.fileList.length === 0) {
          Message.warning('请先选择一个或多个文件！');
          return;
        }
        // 检查文件数量是否超过最大限制
        const maxFiles = 100;
        if (this.fileList.length > maxFiles) {
          this.showMessage(`已选择${this.fileList.length}个文件，超过一次性上传最大数量（${maxFiles}个）,将自动清空列表，请重新选择文件 ！`, 'error',3000);
          setTimeout(() => {
            this.clearFileSelection(); // 清空文件列表
          }, 1500); // 延迟1.5秒执行
          return;
        }
        // 检查是否有重复文件
        const fileNames = this.fileList.map(file => file.name);
        const uniqueFileNames = new Set(fileNames);
        if (fileNames.length !== uniqueFileNames.size) {
          this.showMessage('文件列表中存在重复文件，将自动清空列表，请重新选择文件 ！', 'error',3000);
          setTimeout(() => {
            this.clearFileSelection(); // 清空文件列表
          }, 2000); // 延迟1.5秒执行
          return;
        }

        // 设置上传状态为 true
        this.isUploading = true;

        const formData = new FormData();
        this.fileList.forEach(file => {
          formData.append('multipartFiles', file.raw);
        });
        this.loadingMessage = this.showLoadingMessage('文件识别上传中...');
        axios.post('http://localhost:20122/ss/service/uploadFileOperation', formData)
          .then((response) => {
            const data = response.data;
            if (data.result === 1) {
              this.showMessage(response.data.msg, 'success', 10000); // 成功消息显示10秒
              this.fetchActivities(); // 刷新日志记录
            }
          })
          .catch((error) => {
            console.error('上传失败:', error);
            this.showMessage(error, 'error', 10000); // 失败消息显示10秒
          })
          .finally(() => {
            if (this.loadingMessage) {
              this.loadingMessage.close(); // 立即关闭加载消息
              // 设置上传状态为 false
              this.isUploading = false;
            }
            setTimeout(() => {
              this.clearFileSelection(); // 清空文件列表
            }, 5000); // 延迟3秒执行
          });

      },
      clearFileSelection() {
        this.fileList = [];
        this.selectedFileCount = 0; // 更新文件数量
        this.$refs.upload.clearFiles();
        this.showMessage('文件列表已清空！', 'success',2000);
      },
      showLoadingMessage(message) {
        return Message({
          message: message,
          type: 'info',
          duration: 0, // 持续显示
          showClose: true,
          customClass: 'loading-message'
        });
      },
      showMessage(message, type, duration) {
        Message({
          message: message,
          type: type,
          duration: duration, // 2秒后自动关闭
          showClose: true
        });
      },
      goToPreview() {
        this.$router.push({ path: '/ssPrice/web/viewFile' });
      },
      fetchActivities() {
        SensorFileRequest.uploadFileLog( response => {
            console.log('Received response data:', response); // 打印日志
            if (Array.isArray(response) && response.length > 0) {
              this.activities = response.map(item => ({
                content: item.content,
                timestamp: item.timestamp
              })).reverse();
            } else {
              this.activities = []; // 设置为空数组
              this.$message({
                message: '暂无数据，日志记录时间列表为空 ！',
                type: 'info',
                duration: 2000, // 2秒后自动关闭
                showClose: true // 显示关闭按钮
              });
            }
            this.isLoading = false; // 数据加载完成后将 isLoading 设置为 false
          },
          (error) => {
            console.error(error);
            this.$message({
              message: error,
              type: 'error'
            });
            this.isLoading = false;
          });
      }
    },
    mounted() {
      this.fetchActivities(); // 组件挂载时获取数据
    }
  }
</script>


<style scoped>
.file-count {
  margin-top: 20px;
  font-size: 16px;
  color: #606266;
}

.title {
  margin-top: 30px;
  text-align: center;
  margin-bottom: 40px;
}
.container {
  display: grid;
  grid-template-columns: 3fr 2fr; /* 左边五分之四，右边五分之一 */
  gap: 20px;
  padding: 20px;
}
.upload-section {
  background-color: #f9f9f9;
  padding: 30px; /* 增加内边距 */
  border-radius: 12px; /* 增加圆角半径 */
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1); /* 增加阴影大小 */
  margin-bottom: 20px; /* 增加底部间距 */
}
.log-section {
  background-color: #f9f9f9;
  padding: 30px; /* 增加内边距 */
  border-radius: 12px; /* 增加圆角半径 */
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1); /* 增加阴影大小 */
  margin-bottom: 20px; /* 增加底部间距 */
}
.description {
  display: block;
  font-size: 15px;
  color: #888;
  margin-bottom: 40px;
}
.no-logs {
  text-align: center;
  color: #888;
}
.log-list {
  list-style-type: none;
  padding: 0;
}
.log-item {
  padding: 8px 0;
  border-bottom: 1px solid #ddd;
}
.log-item:last-child {
  border-bottom: none;
}
.welcome-container {
  flex-direction: column;
  min-height: 95vh;
  padding: 1rem;
}
.header {
  display: block;
  font-size: 2.5em;
  font-weight: bold;
  margin-bottom: 1rem;
  text-align: center;
  color: #212529;
  overflow-wrap: break-word;
  max-width: 340px;
}
.timeline-container {
  max-height: 600px; /* 设置最大高度 */
  overflow-y: auto; /* 添加垂直滚动条 */
  //border: 1px solid #c7c9cb; /* 可选：添加边框以便更好地识别区域 */
  padding: 15px 5px; /* 上下内边距为10px，左右内边距为5px */
  margin: 0 15px; /* 可选：设置左右外边距，以进一步减少宽度 */
}
.footer {
  margin-top: 20px;
  font-size: 15px;
  color: #666;
}
</style>
