import { fileURLToPath, URL } from 'node:url'

import { defineConfig } from 'vite'
import vue from '@vitejs/plugin-vue'

// https://vitejs.dev/config/
export default defineConfig({
  plugins: [vue()],
  resolve: {
    alias: {
      '@': fileURLToPath(new URL('./src', import.meta.url))
    }
  },
  server:{
    host: '0.0.0.0',
    port: 5173, // 设置服务启动端口号
    open: true, // 设置服务启动时是否自动打开浏览器

    proxy:{

      "/api": {
        //target是代理的目标路径
        target: `http://localhost:8000`,
        changeOrigin: true, //必须要开启跨域

        rewrite: (path) => path.replace(/^\/api/, ''), // 路径重写
      },
    },

  }
})
