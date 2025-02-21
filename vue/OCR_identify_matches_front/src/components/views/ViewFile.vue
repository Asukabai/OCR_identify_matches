<template>
  <div class="card-storage-operations" v-loading="loading" element-loading-text="页面正在加载中..." element-loading-spinner="el-icon-loading" element-loading-background="rgba(255, 255, 255, 0.7)">
    <el-header><h1 class="title">晟思文件录入识别系统</h1></el-header>
    <div style="height: 20px;"></div>
    <div class="button-container">
      <el-input v-model="searchQuery" placeholder="根据文件名或文件内容关键词" style="width: 300px;"></el-input>
      <el-button type="primary" @click="searchOperations">模糊查询</el-button>
      <el-button type="primary" @click="resetSearch">重置查询</el-button>
      <el-button type="primary" @click="goUerCardStorageOperate">文件上传</el-button>
    </div>
    <el-table :data="operations" stripe>
      <el-table-column label="序号" width="60">
        <template slot-scope="scope">
          {{ scope.$index + 1 + (currentPage - 1) * pageSize }}
        </template>
      </el-table-column>
<!--      <el-table-column prop="fileName" label="文件名称" width="360" class-name="custom-file-name">-->
<!--        <template slot-scope="scope">-->
<!--          <span>{{ scope.row.productInfo.fileName }}</span>-->
<!--        </template>-->
<!--      </el-table-column>-->
      <el-table-column prop="productName" label="产品名称" width="180">
        <template slot-scope="scope">
          <span>{{ scope.row.productInfo.productName }}</span>
        </template>
      </el-table-column>
      <el-table-column prop="model" label="材质" width="150">
        <template slot-scope="scope">
          <span>{{ scope.row.productInfo.model }}</span>
        </template>
      </el-table-column>
      <el-table-column prop="unitPrice" label="单价(默认:元)" width="140">
        <template slot-scope="scope">
          <span>{{ scope.row.productInfo.unitPrice }}</span>
        </template>
      </el-table-column>
      <el-table-column prop="manufacturer" label="制造商" width="400">
        <template slot-scope="scope">
          <span>{{ scope.row.productInfo.manufacturer }}</span>
        </template>
      </el-table-column>
      <el-table-column prop="contactPerson" label="联系人" width="150">
        <template slot-scope="scope">
          <span>{{ scope.row.productInfo.contactPerson }}</span>
        </template>
      </el-table-column>
      <el-table-column prop="phone" label="电话" width="280">
        <template slot-scope="scope">
          <span>{{ scope.row.productInfo.phone }}</span>
        </template>
      </el-table-column>
      <el-table-column prop="purchaseTime" label="签订时间" width="260">
        <template slot-scope="scope">
          <span>{{ scope.row.productInfo.purchaseTime }}</span>
        </template>
      </el-table-column>
<!--      <el-table-column label="文件内容">-->
<!--        <template slot-scope="scope">-->
<!--          <span>{{ scope.row.fileInfo.wordContent.substring(0, 50) + (scope.row.fileInfo.wordContent.length > 50 ? '...' : '') }}</span>-->
<!--          <el-button type="text" @click="showContent(scope.row.fileInfo.wordContent)">查看内容</el-button>-->
<!--        </template>-->
<!--      </el-table-column>-->
      <el-table-column label="文件预览" width="230">
        <template slot-scope="scope">
          <div v-if="scope.row.imageUrls && scope.row.imageUrls.length > 0">
            <el-image
              v-for="(imageUrl, index) in scope.row.imageUrls.slice(0, 3)"
              :key="index"
              :src="'data:image/png;base64,' + imageUrl"
              :preview-src-list="['data:image/png;base64,' + imageUrl]"
              style="width: 40px; height: 40px; margin-right: 5px;"
            />
            <el-button v-if="scope.row.imageUrls.length > 3" type="text" @click="showImages(scope.row.imageUrls)">查看更多</el-button>
          </div>
          <span v-else>无预览</span>
        </template>
      </el-table-column>
<!--      <el-table-column prop="createTime" label="上传时间" width="180">-->
<!--        <template slot-scope="scope">-->
<!--          {{ new Date(scope.row.fileInfo.createTime).toLocaleString() }}-->
<!--        </template>-->
<!--      </el-table-column>-->
    </el-table>
    <el-pagination
      @size-change="handleSizeChange"
      @current-change="handleCurrentChange"
      :current-page="currentPage"
      :page-sizes="[5, 10, 20, 50]"
      :page-size="pageSize"
      layout="total, sizes, prev, pager, next, jumper"
      :total="totalItems"
    ></el-pagination>
    <el-dialog title="资产详细信息" :visible.sync="detailsVisible">
      <el-descriptions :column="2" border>
        <el-descriptions-item label="文件名称">{{ form.productInfo.fileName }}</el-descriptions-item>
        <el-descriptions-item label="产品名称">{{ form.productInfo.productName }}</el-descriptions-item>
        <el-descriptions-item label="材质">{{ form.productInfo.model }}</el-descriptions-item>
        <el-descriptions-item label="单价">{{ form.productInfo.unitPrice }}</el-descriptions-item>
        <el-descriptions-item label="制造商">{{ form.productInfo.manufacturer }}</el-descriptions-item>
        <el-descriptions-item label="联系人">{{ form.productInfo.contactPerson }}</el-descriptions-item>
        <el-descriptions-item label="电话">{{ form.productInfo.phone }}</el-descriptions-item>
        <el-descriptions-item label="签订时间">{{ form.productInfo.purchaseTime }}</el-descriptions-item>
        <el-descriptions-item label="文件内容">{{ form.fileInfo.wordContent }}</el-descriptions-item>
        <el-descriptions-item label="文件预览">
          <el-image
            v-for="(imageUrl, index) in form.imageUrls"
            :key="index"
            :src="'data:image/png;base64,' + imageUrl"
            :preview-src-list="form.imageUrls.map(url => 'data:image/png;base64,' + url)"
            style="width: 100px; height: 100px; margin: 5px;"
          />
        </el-descriptions-item>
        <el-descriptions-item label="上传时间">{{ new Date(form.fileInfo.createTime).toLocaleString() }}</el-descriptions-item>
      </el-descriptions>
      <div slot="footer" class="dialog-footer">
        <el-button @click="detailsVisible = false">关 闭</el-button>
      </div>
    </el-dialog>
    <el-dialog title="文件内容详情" :visible.sync="contentDialogVisible" width="80%">
      <pre>{{ selectedContent }}</pre>
      <span slot="footer" class="dialog-footer">
        <el-button @click="contentDialogVisible = false">关闭</el-button>
      </span>
    </el-dialog>
    <el-dialog title="图片预览" :visible.sync="imageDialogVisible" width="80%">
      <el-image
        v-for="(imageUrl, index) in selectedImageUrls"
        :key="index"
        :src="'data:image/png;base64,' + imageUrl"
        :preview-src-list="selectedImageUrls.map(url => 'data:image/png;base64,' + url)"
        style="width: 100px; height: 100px; margin: 5px;"
      />
      <span slot="footer" class="dialog-footer">
        <el-button @click="imageDialogVisible = false">关闭</el-button>
      </span>
    </el-dialog>
    <Footer />
  </div>
</template>

<script>
import SensorFile from "../../utils/sensorFile"; // 引入 lodash 的 debounce 函数
import Footer from "../../components/views/Footer.vue"; // 引入 Footer 组件

export default {
  components: {
    Footer
  },
  data() {
    return {
      searchQuery: '', // 查询输入框的值
      operations: [], // 存储所有操作数据
      currentPageData: [], // 当前页面的数据
      dialogVisible: false,
      detailsVisible: false, // 查看详情对话框的显示状态
      form: {
        productInfo: {
          id: null,
          fileName: '',
          productName: '',
          model: '',
          unitPrice: '',
          manufacturer: '',
          contactPerson: '',
          phone: '',
          purchaseTime: ''
        },
        fileInfo: {
          id: null,
          fileName: '',
          wordContent: '',
          tableContent: '',
          imageId: null,
          createTime: '',
          updateTime: '',
          fileImages: null
        },
        imageUrls: []
      },
      isEdit: false,
      currentId: null,
      currentPage: 1, // 当前页数
      pageSize: 5, // 每页显示的数据量
      totalItems: 0, // 总数据量
      contentDialogVisible: false, // 文件内容弹窗显示状态
      selectedContent: '', // 选中的文件内容
      imageDialogVisible: false, // 图片弹窗显示状态
      selectedImageUrls: [], // 选中的图片URLs
      loading: true, //加载状态，默认值为 true。
      isFuzzySearch: false, // 新增标志位，标识是否为模糊查询
    };
  },
  created() {
    this.searchOperations('all');
  },
  methods: {
    searchOperations(query = 'all') {
      if (query === 'all') {
        this.isFuzzySearch = false;
        let param = { 'query': 'all', 'pageNum': this.currentPage, 'pageSize': this.pageSize };
        SensorFile.searchOperations(param, (response) => {
          this.$message({
            // message: '查询成功',
            message: response.msg,
            type: 'success'
          });
          this.operations = response.respData.records || [];
          this.totalItems = response.respData.total;
          this.updateCurrentPageData();
          this.loading = false; // 请求成功后设置 loading 为 false
        }, (error) => {
          console.error(error);
          this.$message({
            message: error,
            type: 'error'
          });
          this.loading = false; // 请求失败后也设置 loading 为 false
        });
      } else if (this.searchQuery.trim() === '') {
        this.$message({
          message: '请输入查询内容！',
          type: 'error'
        });
        this.fetchOperations(this.currentPage, this.pageSize);
        this.loading = false; // 输入为空时也设置 loading 为 false
      } else {
        this.isFuzzySearch = true;
        const trimmedQuery = this.searchQuery.trim();
        let param = { 'query': trimmedQuery, 'pageNum': this.currentPage, 'pageSize': this.pageSize };
        console.log(trimmedQuery);
        SensorFile.searchOperations(param, (response) => {
          this.$message({
            // message: '查询成功',
            message: response.msg,
            type: 'success'
          });
          this.operations = response.respData.records || [];
          this.totalItems = response.respData.total;
          this.updateCurrentPageData();
          this.loading = false; // 请求成功后设置 loading 为 false
        }, (error) => {
          console.error(error);
          this.$message({
            message: error,
            type: 'error'
          });
          this.loading = false; // 请求失败后也设置 loading 为 false
        });
      }
    },
    updateCurrentPageData() {
      const start = (this.currentPage - 1) * this.pageSize;
      const end = start + this.pageSize;
      this.currentPageData = this.operations.slice(start, end);
    },
    handleSizeChange(newSize) {
      this.pageSize = newSize;
      this.currentPage = 1; // 重置到第一页
      if (this.isFuzzySearch) {
        this.searchOperations(this.searchQuery); // 如果是模糊查询，则继续模糊查询
      } else {
        this.searchOperations('all'); // 否则查询所有
      }
    },
    handleCurrentChange(newPage) {
      this.currentPage = newPage;
      if (this.isFuzzySearch) {
        this.searchOperations(this.searchQuery); // 如果是模糊查询，则继续模糊查询
      } else {
        this.searchOperations('all'); // 否则查询所有
      }
    },
    resetSearch() {
      this.searchQuery = '';
      this.searchOperations('all');
      this.$message({
        message: '重置成功！',
        type: 'success'
      });
      window.location.reload(); // 添加这行代码以重新加载页面
    },
    goUerCardStorageOperate() {
      this.$router.push({ path: '/ssPrice/web/uploadFile' });
    },
    showContent(content) {
      this.selectedContent = content;
      this.contentDialogVisible = true;
    },
    showImages(imageUrls) {
      this.selectedImageUrls = imageUrls;
      this.imageDialogVisible = true;
    },
    showDetails(row) {
      this.form = {
        productInfo: row.productInfo,
        fileInfo: row.fileInfo,
        imageUrls: row.imageUrls
      };
      this.detailsVisible = true;
    }
  }
};
</script>

<style scoped>
.card-storage-operations {
  padding: 20px;
}
.title {
  margin: 0;
  font-size: 30px;
  line-height: 1.3;
}
.button-container {
  text-align: left; /* 确保按钮左对齐 */
  margin-bottom: 20px; /* 添加按钮与表格的间距 */
}
.add-button {
  margin: 0; /* 确保按钮没有额外的外边距 */
}
.dialog-footer {
  text-align: right;
}
.el-header {
  background-color: #f5f7fa;
  padding: 10px;
  border-bottom: 1px solid #dcdfe6;
}
input, select, textarea {
  -moz-appearance: none;
  appearance: none;
  border: 1px solid #ccc; /* 自定义边框 */
  border-radius: 4px; /* 圆角 */
  padding: 10px; /* 内边距 */
  font-size: 16px; /* 字体大小 */
  /* 添加其他自定义样式 */
}
/* 针对其他现代浏览器 */
input, select, textarea {
  appearance: none; /* 标准语法 */
}
</style>
