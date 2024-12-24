<template>
  <div class="netdisk-container">
    <!-- 工具栏 -->
    <div class="toolbar">
      <div class="left-tools">
        <el-button-group class="main-actions">
          <el-upload class="upload-btn" :action="uploadUrl" :headers="headers" :data="uploadData"
            :before-upload="beforeUpload" :on-success="handleUploadSuccess" :on-error="handleUploadError"
            :show-file-list="false" multiple>
            <el-button type="primary">
              <el-icon>
                <Upload />
              </el-icon>
              <span>上传文件</span>
            </el-button>
          </el-upload>
          <el-button type="primary" @click="showNewFolderDialog">
            <el-icon>
              <FolderAdd />
            </el-icon>
            <span>新建文件夹</span>
          </el-button>
        </el-button-group>
      </div>

      <div class="search-box">
        <el-input v-model="searchKeyword" placeholder="搜索文件..." clearable @keyup.enter="handleSearch"
          @clear="clearSearch">
          <template #prefix>
            <el-icon>
              <Search />
            </el-icon>
          </template>
        </el-input>
      </div>

      <div class="path-nav">
        <el-breadcrumb separator="/">
          <el-breadcrumb-item class="clickable" @click="navigateToRoot">
            <el-icon>
              <HomeFilled />
            </el-icon>
            <span>根目录</span>
          </el-breadcrumb-item>
          <el-breadcrumb-item v-for="(path, index) in currentPath" :key="index" class="clickable"
            @click="navigateToPath(index)">
            {{ path }}
          </el-breadcrumb-item>
        </el-breadcrumb>
      </div>
    </div>

    <!-- 文件列表 -->
    <div class="file-container">
      <el-table :data="fileList" style="width: 100%" v-loading="loading" @row-dblclick="handleRowDblClick">
        <el-table-column prop="name" label="名称" min-width="300">
          <template #default="scope">
            <div class="file-item" @click="handleFileClick(scope.row)">
              <el-icon v-if="scope.row.isDirectory" class="file-icon folder">
                <Folder />
              </el-icon>
              <el-icon v-else class="file-icon" :class="getFileIconClass(scope.row.name)">
                <Document />
              </el-icon>
              <span class="file-name">{{ scope.row.name }}</span>
            </div>
          </template>
        </el-table-column>
        <el-table-column prop="size" label="大小" width="120" align="right">
          <template #default="scope">
            {{ formatFileSize(scope.row.size) }}
          </template>
        </el-table-column>
        <el-table-column prop="lastModified" label="修改时间" width="180">
          <template #default="scope">
            {{ formatDate(scope.row.lastModified) }}
          </template>
        </el-table-column>
        <el-table-column label="操作" width="280" fixed="right">
          <template #default="scope">
            <el-button-group>
              <el-tooltip content="下载" placement="top" v-if="!scope.row.isDirectory">
                <el-button size="small" @click="handleDownload(scope.row)">
                  <el-icon>
                    <Download />
                  </el-icon>
                </el-button>
              </el-tooltip>
              <el-tooltip content="重命名" placement="top">
                <el-button size="small" @click="handleRename(scope.row)">
                  <el-icon>
                    <Edit />
                  </el-icon>
                </el-button>
              </el-tooltip>
              <el-tooltip content="移动" placement="top">
                <el-button size="small" @click="handleMoveFile(scope.row)">
                  <el-icon>
                    <Position />
                  </el-icon>
                </el-button>
              </el-tooltip>
              <el-tooltip content="删除" placement="top">
                <el-button size="small" type="danger" @click="handleDelete(scope.row)">
                  <el-icon>
                    <Delete />
                  </el-icon>
                </el-button>
              </el-tooltip>
            </el-button-group>
          </template>
        </el-table-column>
      </el-table>
    </div>

    <!-- 新建文件夹对话框 -->
    <el-dialog v-model="newFolderDialogVisible" title="新建文件夹" width="30%" destroy-on-close>
      <el-form :model="newFolderForm" :rules="folderRules" ref="folderFormRef">
        <el-form-item label="文件夹名称" prop="name">
          <el-input v-model="newFolderForm.name" placeholder="请输入文件夹名称" @keyup.enter="createFolder"></el-input>
        </el-form-item>
      </el-form>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="newFolderDialogVisible = false">取消</el-button>
          <el-button type="primary" @click="createFolder">确定</el-button>
        </span>
      </template>
    </el-dialog>

    <!-- 重命名对话框 -->
    <el-dialog v-model="renameDialogVisible" title="重命名" width="30%" destroy-on-close>
      <el-form :model="renameForm" :rules="renameRules" ref="renameFormRef">
        <el-form-item label="新名称" prop="newName">
          <el-input v-model="renameForm.newName" placeholder="请输入新名称" @keyup.enter="submitRename"></el-input>
        </el-form-item>
      </el-form>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="renameDialogVisible = false">取消</el-button>
          <el-button type="primary" @click="submitRename">确定</el-button>
        </span>
      </template>
    </el-dialog>

    <!-- 移动文件对话框 -->
    <el-dialog v-model="moveDialogVisible" :title="isCopy ? '复制文件' : '移动文件'" width="30%">
      <div class="move-dialog-content">
        <div class="operation-type">
          <el-radio-group v-model="isCopy" class="operation-radio">
            <div class="radio-item">
              <el-radio :label="false">移动到新位置</el-radio>
            </div>
            <div class="radio-item">
              <el-radio :label="true">复制到新位置</el-radio>
            </div>
          </el-radio-group>
        </div>
        <div class="target-location">
          <div class="section-title">目标位置：</div>
          <el-tree ref="folderTreeRef" :data="folderTree" node-key="id" :props="defaultProps" :load="loadNode" lazy
            @node-click="handleFolderSelect" class="location-tree" default-expand-all>
            <template #default="{ node }">
              <span class="custom-tree-node">
                <el-icon>
                  <Folder />
                </el-icon>
                <span>{{ node.label }}</span>
              </span>
            </template>
          </el-tree>
        </div>
      </div>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="moveDialogVisible = false">取消</el-button>
          <el-button type="primary" @click="submitMove" :disabled="selectedPath === undefined">
            {{ isCopy ? '复制到这里' : '移动到这里' }}
          </el-button>
        </span>
      </template>
    </el-dialog>

    <!-- 搜索结果对话框 -->
    <el-dialog v-model="searchDialogVisible" title="搜索结果" width="60%">
      <div class="search-results" v-loading="searching">
        <template v-if="searchResults.length">
          <el-table :data="searchResults" style="width: 100%">
            <el-table-column prop="name" label="文件名" min-width="200">
              <template #default="scope">
                <div class="file-item">
                  <el-icon v-if="scope.row.isDirectory" class="file-icon folder">
                    <Folder />
                  </el-icon>
                  <el-icon v-else class="file-icon" :class="getFileIconClass(scope.row.name)">
                    <Document />
                  </el-icon>
                  <span class="file-name">{{ scope.row.name }}</span>
                </div>
              </template>
            </el-table-column>
            <el-table-column prop="parentPath" label="所在位置" min-width="200">
              <template #default="scope">
                <el-tag size="small" type="info">
                  {{ scope.row.parentPath || '根目录' }}
                </el-tag>
              </template>
            </el-table-column>
            <el-table-column prop="size" label="大小" width="120" align="right">
              <template #default="scope">
                {{ formatFileSize(scope.row.size) }}
              </template>
            </el-table-column>
            <el-table-column label="操作" width="200" fixed="right">
              <template #default="scope">
                <el-button-group>
                  <el-tooltip content="打开所在位置" placement="top">
                    <el-button size="small" @click="navigateToFile(scope.row)">
                      <el-icon>
                        <Position />
                      </el-icon>
                    </el-button>
                  </el-tooltip>
                  <el-tooltip content="下载" placement="top" v-if="!scope.row.isDirectory">
                    <el-button size="small" @click="handleDownload(scope.row)">
                      <el-icon>
                        <Download />
                      </el-icon>
                    </el-button>
                  </el-tooltip>
                </el-button-group>
              </template>
            </el-table-column>
          </el-table>
        </template>
        <el-empty v-else description="没有找到匹配的文件" />
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { ElMessage, ElMessageBox } from 'element-plus'
import {
  Document, Folder, FolderAdd, Upload, Download, Edit, Delete, Position, HomeFilled, Search
} from '@element-plus/icons-vue'
import request from '@/js/request'

export default {
  name: 'Netdisk',
  components: {
    Document, Folder, FolderAdd, Upload, Download, Edit, Delete, Position, HomeFilled, Search
  },
  data () {
    return {
      loading: false,
      fileList: [],
      currentPath: [],
      newFolderDialogVisible: false,
      renameDialogVisible: false,
      moveDialogVisible: false,
      newFolderForm: {
        name: ''
      },
      renameForm: {
        newName: '',
        file: null
      },
      folderRules: {
        name: [
          { required: true, message: '请输入文件夹名称', trigger: 'blur' },
          { pattern: /^[^\\/:*?"<>|]+$/, message: '名称不能包含特殊字符', trigger: 'blur' }
        ]
      },
      renameRules: {
        newName: [
          { required: true, message: '请输入新名称', trigger: 'blur' },
          { pattern: /^[^\\/:*?"<>|]+$/, message: '名称不能包含特殊字符', trigger: 'blur' }
        ]
      },
      uploadUrl: '/api/files/upload',
      headers: {
        'Authorization': 'Bearer ' + localStorage.getItem('token')
      },
      selectedFile: null,
      targetPath: '',
      folderTree: [],
      defaultProps: {
        label: 'label',
        isLeaf: 'isLeaf'
      },
      isCopy: false,
      searchKeyword: '',
      searchResults: [],
      searchDialogVisible: false,
      searching: false,
    }
  },
  computed: {
    uploadData () {
      return {
        path: this.currentPath.join('/')
      }
    }
  },
  methods: {
    // 加载文件列表
    async loadFileList () {
      this.loading = true
      try {
        const path = this.currentPath.join('/')
        const res = await request.get('/files/list', { params: { path } })
        if (res.code === 1) {
          this.fileList = res.data
        }
      } catch (error) {
        ElMessage.error('获取文件列表失败')
      } finally {
        this.loading = false
      }
    },

    // 导航相关
    navigateToRoot () {
      this.currentPath = []
      this.loadFileList()
    },

    navigateToPath (index) {
      this.currentPath = this.currentPath.slice(0, index + 1)
      this.loadFileList()
    },

    // 文件操作
    handleFileClick (file) {
      if (file.isDirectory) {
        this.currentPath.push(file.name)
        this.loadFileList()
      }
    },

    handleRowDblClick (row) {
      if (!row.isDirectory) {
        this.handlePreview(row)
      }
    },

    // 新建文件夹
    showNewFolderDialog () {
      this.newFolderDialogVisible = true
      this.newFolderForm.name = ''
    },

    async createFolder () {
      try {
        // 验证表单
        await this.$refs.folderFormRef.validate()

        const res = await request.post('/files/createFolder', {
          path: this.currentPath.join('/'),
          folderName: this.newFolderForm.name
        })

        if (res.code === 1) {
          ElMessage.success('创建成功')
          this.loadFileList()
          this.newFolderDialogVisible = false
          this.newFolderForm.name = '' // 清空表单
        }
      } catch (error) {
        if (error === false) {
          // 表单验证失败
          return
        }
        ElMessage.error('创建失败：' + (error.message || '未知错误'))
      }
    },

    // 下载文件
    handleDownload (file) {
      const path = [...this.currentPath, file.name].join('/')
      window.open(`/api/files/download?path=${path}`, '_blank')
    },

    // 重命名
    handleRename (file) {
      this.renameForm.file = file
      this.renameForm.newName = file.name
      this.renameDialogVisible = true
    },

    async submitRename () {
      try {
        await this.$refs.renameFormRef.validate()
        const res = await request.post('/files/rename', {
          path: this.currentPath.join('/'),
          oldName: this.renameForm.file.name,
          newName: this.renameForm.newName
        })
        if (res.code === 1) {
          ElMessage.success('重命名成功')
          this.loadFileList()
          this.renameDialogVisible = false
        }
      } catch (error) {
        if (error === false) {
          // 表单验证失败
          return
        }
        ElMessage.error('重命名失败：' + (error.message || '未知错误'))
      }
    },

    // 删除文件
    async handleDelete (file) {
      try {
        await ElMessageBox.confirm(
          `确定要删除 ${file.name} 吗？`,
          '警告',
          {
            confirmButtonText: '确定',
            cancelButtonText: '取消',
            type: 'warning',
          }
        )

        const path = [...this.currentPath, file.name].join('/')
        const res = await request.delete('/files/delete', {
          params: { path }
        })

        if (res.code === 1) {
          ElMessage.success('删除成功')
          this.loadFileList()
        }
      } catch (error) {
        if (error !== 'cancel') {
          ElMessage.error('删除失败')
        }
      }
    },

    // 工具函数
    getFileIconClass (fileName) {
      if (/\.(jpg|jpeg|png|gif)$/i.test(fileName)) return 'image'
      if (/\.(doc|docx)$/i.test(fileName)) return 'word'
      if (/\.(xls|xlsx)$/i.test(fileName)) return 'excel'
      if (/\.(pdf)$/i.test(fileName)) return 'pdf'
      return 'file'
    },

    formatFileSize (size) {
      if (size < 1024) return size + ' B'
      if (size < 1024 * 1024) return (size / 1024).toFixed(2) + ' KB'
      if (size < 1024 * 1024 * 1024) return (size / 1024 / 1024).toFixed(2) + ' MB'
      return (size / 1024 / 1024 / 1024).toFixed(2) + ' GB'
    },

    formatDate (timestamp) {
      return new Date(timestamp).toLocaleString()
    },

    // 上传相关
    beforeUpload (file) {
      // 可以在这里添加文件大小、类型等限制
      return true
    },

    handleUploadSuccess (response) {
      if (response.code === 1) {
        ElMessage.success('上传成功')
        this.loadFileList()
      } else {
        ElMessage.error(response.msg || '上传失败')
      }
    },

    handleUploadError () {
      ElMessage.error('上传失败')
    },

    // 移动文件相关方法
    handleMoveFile (file) {
      this.selectedFile = file
      this.selectedPath = ''
      this.isCopy = false // 默认为移动模式
      this.moveDialogVisible = true
    },

    // 加载文件夹树
    async loadFolderTree () {
      try {
        const res = await request.get('/files/list', { params: { path: '' } })
        if (res.code === 1) {
          this.folderTree = this.buildFolderTree(res.data)
        }
      } catch (error) {
        ElMessage.error('加载文件夹失败')
      }
    },

    // 构建文件夹树
    buildFolderTree (files) {
      return files
        .filter(file => file.isDirectory)
        .map(folder => ({
          id: folder.name,
          name: folder.name,
          children: []
        }))
    },

    // 选择目标文件夹
    handleFolderSelect (data) {
      this.selectedPath = data.id
    },

    // 提交移动
    async submitMove () {
      if (this.selectedPath === undefined || this.selectedPath === null) {
        ElMessage.warning('请选择目标位置')
        return
      }

      const currentFullPath = this.currentPath.join('/')
      if (this.selectedPath === currentFullPath) {
        ElMessage.warning('不能移动到相同目录')
        return
      }

      try {
        const res = await request.post('/files/move', {
          sourcePath: [...this.currentPath, this.selectedFile.name].join('/'),
          targetPath: this.selectedPath ? this.selectedPath + '/' + this.selectedFile.name : this.selectedFile.name,
          isCopy: this.isCopy
        })

        if (res.code === 1) {
          ElMessage.success(this.isCopy ? '复制成功' : '移动成功')
          this.loadFileList()
          this.moveDialogVisible = false
        }
      } catch (error) {
        ElMessage.error((this.isCopy ? '复制' : '移动') + '失败：' + (error.message || '未知错误'))
      }
    },

    // 修改加载文件夹树的方法
    async loadNode (node, resolve) {
      try {
        if (node.level === 0) {
          // 根节点
          resolve([{
            id: '',
            label: '根目录',
            isLeaf: false
          }])
          return
        }

        // 获取当前节点的完整路径
        const path = node.data.id
        const res = await request.get('/files/folders', {
          params: { path }
        })

        if (res.code === 1) {
          const folders = res.data.map(folder => ({
            id: folder.id,
            label: folder.label,
            isLeaf: !folder.hasChildren
          }))
          resolve(folders)
        } else {
          resolve([])
        }
      } catch (error) {
        console.error('加载文件夹失败:', error)
        ElMessage.error('加载文件夹失败')
        resolve([])
      }
    },

    // 搜索相关方法
    async handleSearch () {
      if (!this.searchKeyword.trim()) {
        ElMessage.warning('请输入搜索关键词')
        return
      }

      this.searching = true
      this.searchDialogVisible = true
      try {
        const res = await request.get('/files/search', {
          params: { keyword: this.searchKeyword.trim() }
        })
        if (res.code === 1) {
          this.searchResults = res.data
        }
      } catch (error) {
        ElMessage.error('搜索失败：' + error.message)
      } finally {
        this.searching = false
      }
    },

    clearSearch () {
      this.searchKeyword = ''
      this.searchResults = []
      this.searchDialogVisible = false
    },

    // 导航到文件所在位置
    navigateToFile (file) {
      this.searchDialogVisible = false
      if (file.parentPath) {
        this.currentPath = file.parentPath.split('/')
      } else {
        this.currentPath = []
      }
      this.loadFileList()
    }
  },
  mounted () {
    this.loadFileList()
  }
}
</script>

<style scoped>
.netdisk-container {
  padding: 20px;
  height: calc(100vh - 100px);
  display: flex;
  flex-direction: column;
}

.toolbar {
  margin-bottom: 20px;
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 0 10px;
}

.path-nav {
  flex-grow: 1;
  margin-left: 20px;
}

.file-container {
  flex-grow: 1;
  overflow: auto;
  background: #fff;
  border-radius: 4px;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
}

.file-item {
  display: flex;
  align-items: center;
  cursor: pointer;
  padding: 4px 0;
}

.file-icon {
  font-size: 20px;
  margin-right: 8px;
}

.file-icon.folder {
  color: #ffd04b;
}

.file-icon.image {
  color: #67c23a;
}

.file-icon.word {
  color: #409eff;
}

.file-icon.excel {
  color: #67c23a;
}

.file-icon.pdf {
  color: #f56c6c;
}

.file-name {
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.clickable {
  cursor: pointer;

  &:hover {
    color: #409EFF;
  }
}

.move-dialog-content {
  height: 400px;
  display: flex;
  flex-direction: column;
}

.folder-tree {
  flex-grow: 1;
  overflow: auto;
  border: 1px solid #dcdfe6;
  border-radius: 4px;
  padding: 10px;
}

.move-options {
  margin-top: 10px;
}

.folder-node {
  display: flex;
  align-items: center;
  gap: 8px;
}

.preview-container {
  min-height: 400px;
  display: flex;
  justify-content: center;
  align-items: center;
}

.preview-image {
  max-width: 100%;
  max-height: 70vh;
}

.preview-text {
  width: 100%;
  max-height: 70vh;
  overflow: auto;
  padding: 20px;
  background: #f5f7fa;
  border-radius: 4px;
}

.preview-unsupported {
  color: #909399;
  font-size: 16px;
}

.upload-btn {
  display: inline-block;
}

:deep(.el-upload) {
  display: block;
}

:deep(.el-button-group .el-upload--text) {
  margin: 0;
}

.el-tree {
  max-height: 400px;
  overflow-y: auto;
}

/* 添加新样式 */
.main-actions {
  display: flex;
  gap: 8px;
}

.main-actions .el-button {
  display: flex;
  align-items: center;
  gap: 4px;
  padding: 8px 16px;
}

.main-actions .el-icon {
  margin-right: 4px;
}

.toolbar {
  background-color: #fff;
  padding: 16px;
  border-radius: 8px;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.05);
  margin-bottom: 20px;
}

.path-nav {
  /* margin-top: 16px; */
  padding: 8px 0;
  /* border-top: 1px solid #eee; */
}

.operation-type {
  margin-bottom: 20px;
  padding: 16px;
  background-color: #f8f9fa;
  border-radius: 8px;
}

.operation-radio {
  display: flex;
  flex-direction: column;
  gap: 12px;
}

.radio-item {
  display: flex;
  align-items: center;
  height: 32px;
  /* 固定高度确保对齐 */
}

.radio-item .el-radio {
  margin-right: 0;
  width: 100%;
}

.radio-item .el-radio__label {
  font-size: 14px;
}

.target-location {
  flex: 1;
}

.section-title {
  font-weight: 500;
  margin-bottom: 12px;
  color: #606266;
}

.location-tree {
  border: 1px solid #e4e7ed;
  border-radius: 4px;
  padding: 12px;
  max-height: 300px;
  overflow-y: auto;
}

.custom-tree-node {
  display: flex;
  align-items: center;
  gap: 8px;
}

.move-dialog-content {
  display: flex;
  flex-direction: column;
  gap: 20px;
  padding: 0 0 16px 0;
}

/* 美化滚动条 */
::-webkit-scrollbar {
  width: 6px;
  height: 6px;
}

::-webkit-scrollbar-thumb {
  background: #c0c4cc;
  border-radius: 3px;
}

::-webkit-scrollbar-track {
  background: #f5f7fa;
}

.search-box {
  width: 300px;
  margin: 0 20px;
}

.search-results {
  min-height: 200px;
  max-height: 600px;
  overflow-y: auto;
}

.toolbar {
  display: grid;
  grid-template-columns: auto 300px 1fr;
  gap: 20px;
  align-items: center;
}
</style>