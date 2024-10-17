import { createApp } from 'vue'
import App from './App.vue'
import router from './router'; // Import the router
import ElementPlus from 'element-plus';
import 'element-plus/dist/index.css';
//createApp(App).use(router).mount('#app')
const app = createApp(App); // Create Vue app

app.use(router); // Use Vue Router
app.use(ElementPlus); // Use Element Plus
app.mount('#app'); // Mount the app to the DOM
