<script>
import {defineComponent} from 'vue'
import "http://apps.bdimg.com/libs/jquery/1.9.1/jquery.min.js";
import "../../../../assets/static/js/drag.js";
import "../../../../assets/static/js/linux-file-base.js";
import "../../../../assets/static/js/linux-file-logic.js";
import "../../../../assets/static/js/linux-file-network.js";
import "../../../../assets/static/js//linux-file-elements.js";


$(function () {
  init("MARK")
  loadMarkFile();
});


export default defineComponent({
  name: "fileGuan"
})
</script>

<template>

  <div class="wrapper">
    <!-- 对话框 -->
    <div class="dialog-wrapper">
      <div class="dialog-edit">
        <header>
          标题
        </header>
        <textarea></textarea>
        <div class="opt-buttons">
          <button onclick="showEditDialog(false)">取消</button>
          <button onclick="setFileContent()">保存</button>
        </div>
      </div>

      <div class="dialog dialog-loading">
        <div class="loading">
          <div class="grp">
            <img src="../../../../assets/static/imgs/bars.svg" alt="">
          </div>
          <span class="progress"></span>
        </div>

      </div>
      <div class="dialog dialog-message alert-dialog">
        <header>提示</header>
        <div class="message">您想对文件进行怎么操作？</div>
        <div class="buttons">
          <button class="ok" onclick="showMessageDialog(false,'')">确定</button>
        </div>
      </div>
      <div class="dialog dialog-input alert-dialog">
        <header>提示</header>
        <div class="message">您想对文件进行怎么操作？</div>
        <div class="buttons">
          <button class="ok" onclick="showMessageDialog(false,'')">确定</button>
        </div>
      </div>
      <div class="dialog dialog-file-opt alert-dialog">
        <header>提示</header>
        <div class="message">您想对文件进行怎么操作？</div>
        <div class="buttons">
          <button class="more">
            <span>更多</span>
            <ul class="menu">
              <li class="mark-btn" onclick="markfile(this,false)">
                取消收藏
              </li>
              <li class="edit-btn" onclick="editFile()">编辑</li>
            </ul>
          </button>
          <button class="cencel" onclick="showFileOptDialog(false)">取消</button>
          <button onclick="startDownLoad()" class="download">下载</button>

          <button onclick="preDeleteFile()" class="delete">删除</button>
        </div>
      </div>
    </div>
    <div class="container">
      <header>
        <h3 class="title">Linux文件管理面板</h3>
      </header>
      <div class="file-manager">
        <aside>
          <nav>
            <ul>
              <li class=""><a href="/">我的电脑</a></li>
              <li class="select"><a href="/mark">收藏的文件夹</a></li>
            </ul>
          </nav>
        </aside>

        <div class="file-window">
          <div class="file-draw-panel">

            释放鼠标进行上传
          </div>
          <div class="file-window-header">
          </div>
          <div class="file-window-body">
            <ul>
            </ul>
          </div>
          <div class="file-window-footer"></div>
        </div>

      </div>

      <div class="file-transfer-list"></div>
    </div>
  </div>

</template>

<style>
@import "../../../../assets/static/css/base.css";
@import "../../../../assets/static/css/index.css";
@import "../../../../assets/static/css/file-style.css";
@import "../../../../assets/static/css/dialog.css";


</style>