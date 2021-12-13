import router from './routers'
import store from '@/store'
import Config from '@/settings'
import NProgress from 'nprogress' // progress bar
import 'nprogress/nprogress.css'// progress bar style
import { getToken } from '@/utils/auth' // getToken from cookie
import { buildMenus } from '@/api/system/menu'
import { pvIncr } from '@/api/dashboard/dashboard'
import { filterAsyncRouter } from '@/store/modules/permission'
import { Notification } from 'element-ui'

NProgress.configure({ showSpinner: false })// NProgress Configuration

const whiteList = ['/login', '/401', '/404', '/502']// no redirect whitelist

router.beforeEach((to, from, next) => {
  if (to.meta.title) {
    document.title = to.meta.title + ' - ' + Config.title
  }
  NProgress.start()
  if (getToken()) {
    const userCenter = '/user/center'
    const isMustResetPwd = store.getters.user.isMustResetPwd
    // 必须修改密码强制跳转
    if (isMustResetPwd) {
      Notification.warning({
        title: '请您先修改密码',
        duration: 5000
      })
    }
    if (isMustResetPwd && to.path !== userCenter) {
      next({ path: userCenter })
      NProgress.done()
    } else {
      // 已登录且要跳转的页面是登录页
      if (to.path === '/login') {
        next({ path: '/' })
        NProgress.done()
      } else {
        if (store.getters.roles.length === 0) { // 判断当前用户是否已拉取完user_info信息
          store.dispatch('GetInfo').then(res => { // 拉取user_info
            // 动态路由，拉取菜单
            loadMenus(next, to)
          }).catch((err) => {
            console.log(err)
            store.dispatch('LogOut').then(() => {
              location.reload() // 为了重新实例化vue-router对象 避免bug
            })
          })
          // 登录时未拉取 菜单，在此处拉取
        } else if (store.getters.loadMenus) {
          // 修改成false，防止死循环
          store.dispatch('updateLoadMenus').then(res => {})
          loadMenus(next, to)
        } else {
          next()
        }
      }
    }
  } else {
    /* has no token*/
    if (whiteList.indexOf(to.path) !== -1) { // 在免登录白名单，直接进入
      next()
    } else {
      next(`/login?redirect=${to.fullPath}`) // 否则全部重定向到登录页
      NProgress.done()
    }
  }
})

export const loadMenus = (next, to) => {
  buildMenus().then(res => {
    const asyncRouter = filterAsyncRouter(res)
    asyncRouter.push({ path: '*', redirect: '/404', hidden: true })
    store.dispatch('GenerateRoutes', asyncRouter).then(() => { // 存储路由
      router.addRoutes(asyncRouter) // 动态添加可访问路由表

      if (to.path === '/dashboard') {
        const children = asyncRouter[0].children
        if (children.length > 0) {
          const name = children[0].name
          next({ name, replace: true })
        }
      }
      next({ ...to, replace: true })
    })
  })
}

router.afterEach((to, from, nex) => {
  NProgress.done() // finish progress bar
  if (to.path !== '/' && to.path !== '/login') {
    pvIncr()
  }
})
