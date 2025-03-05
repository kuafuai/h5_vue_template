export const dynamicRoutes = [
    {
        path: '/pages/home/index',
        meta: {
            title: '首页',
            allShow: true,
            shownot: false,
            adminShow: true,
            changeShow: true,
            icon:'icon-zhuye'
        }
    },
    {
        path: '/pages/change/index',
        meta: {
            title: '变更列表',
            shownot: false,
            adminShow: true,
            changeShow: true,
            icon:'icon-liebiao1'
        }
    },
    {
        path: '/pages/change/list_info',
        meta: {
            title: '变更明细',
            shownot: false,
            adminShow: true,
            changeShow: true,
            icon:'icon-liebiao1'
        }
    },
    {
        path: '/pages/change/myTake',
        meta: {
            title: '参与的变更',
            shownot: false,
            adminShow: true,
            changeShow: true,
            icon:'icon-biangeng'
        }
    },
    {
        path: '/pages/change/myFollow',
        meta: {
            title: '关注的变更',
            shownot: false,
            adminShow: true,
            changeShow: true,
            icon:'icon-biangeng'
        }
    },
    {
        path: '/pages/submission/index',
        meta: {
            title: '提交物管理',
            shownot: false,
            adminShow: true,
            changeShow: false,
            icon:'icon-zuanshi-L'
        }
    },
    {
        path: '/pages/approve/index',
        meta: {
            title: '审批人管理',
            shownot: false,
            adminShow: true,
            changeShow: false,
            icon:'icon-Viconzq-'
        }
    },
    {
        path: '/pages/form/index',
        meta: {
            title: '表单配置',
            shownot: false,
            adminShow: true,
            changeShow: false,
            icon:'icon-bianji'
        }
    },
    {
        path: '/pages/user/index',
        meta: {
            title: '人员管理',
            shownot: false,
            adminShow: true,
            changeShow: false,
            icon:'icon-pingtaiguanli-renyuanguanli'
        }
    },
    {
        path: '/pages/document/index',
        meta: {
            title: '表单下载',
            allShow: true,
            shownot: false,
            adminShow: true,
            changeShow: true,
            icon:'icon-biangeng'
        }
    },
    {
        path: '/pages/flowable/definition/index',
        meta: {
            title: '流程定义',
            shownot: true
        }
    },
    {
        path: '/pages/flowable/myProcess/index',
        meta: {
            title: '我的流程',
            shownot: true
        }
    },
    {
        path: '/pages/flowable/todo/index',
        meta: {
            title: '代办任务',
            shownot: true
        }
    }
]