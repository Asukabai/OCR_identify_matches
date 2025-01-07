import Vue from 'vue';
import VueRouter from 'vue-router';

Vue.use(VueRouter);
let baseURL = ''
const routes = [
  { path: '/ssPrice/web/uploadFile', component: () => import('../components/views/UploadFile.vue') },
  { path: '/ssPrice/web/viewFile', component: () => import('../components/views/ViewFile.vue') },
  { path: '/ssPrice/web/login', name: 'Login', meta: { name: '登录' }, component: () => import('../components/views/Login.vue') },
  { path: '*', name: '404', meta: { name: '无法访问' }, component: () => import('../components/views/404.vue') },
];

const router = new VueRouter({
  mode: 'history',
  routes
});

export default router;
