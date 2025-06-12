import { createApp } from 'vue'
import App from './App.vue'
import router from './router'

// 创建Vue应用实例
const app = createApp(App)

// 注册路由
app.use(router)

// 挂载到DOM
app.mount('#app')