let baseurl = import.meta.env.VITE_APP_BASE_API;

// #ifdef H5
import axios from 'axios';

// 创建 axios 实例
 const service = axios.create({
    baseURL: baseurl,
    timeout: 50000,
    headers: { 'Content-Type': 'application/json;charset=utf-8' },
});

const useLogin = import.meta.env.VITE_USE_LOGIN === 'true'
// 请求拦截器
service.interceptors.request.use(
    (config) => {
        if (!config.headers) {
            throw new Error(`Expected 'config' and 'config.headers' not to be undefined`);
        }

        // 可选：定义 useLogin 函数或移除相关代码
        if (localStorage.getItem('token')) {
            config.headers.Authorization = 'Bearer ' + localStorage.getItem('token');
        }

        return config;
    },
    (error) => {
        return Promise.reject(error);
    },
);

// 响应拦截器
service.interceptors.response.use(
    (response) => {
        const res = response.data;
        const { code, message } = res;
        if (code === 0) {
            return res;
        } else {
            if (code === 401 || code === 403) {
                localStorage.removeItem('token');
                location.href =
                    import.meta.env.VITE_BASE;
            } else {
                ElMessage({
                    message: message || '系统出错',
                    type: 'error',
                    duration: 5 * 1000,
                });
            }
            return Promise.reject(new Error(message || 'Error'));
        }
    },
    (error) => {
        console.log('请求异常：', error);
        if (error.response.status === 401) {
            localStorage.removeItem('token');
            location.href =
                import.meta.env.VITE_BASE;
        } else {
            ElMessage({
                message: '网络异常，请稍后再试!',
                type: 'error',
                duration: 5 * 1000,
            });
            return Promise.reject(new Error('Error'));
        }
    },
);
export default service
// #endif
// #ifdef MP-WEIXIN
let request = (url, data, method, token) => {
    uni.showLoading({
        title: '加载中',
    })

    return new Promise((resolve, reject) => {
        uni.request({
            url: baseurl + url,
            // url: url,
            data: data,
            method: method,
            timeout: 18000,
            header: {
                'content-type': 'application/json',
                "Authorization": 'Bearer ' + token
            },
            success(res) {
                resolve(res)
                if (res.data.code == 40401) {
                    uni.showToast({
                        title: "请先去绑定手机号吧～",
                        icon: "none"
                    })
                    setTimeout(() => {
                        uni.navigateTo({
                            url: "/pages/login/login"
                        })
                    }, 800)
                }
            },
            fail(err) {
                reject(err)
                console.log(err)
                if (err.errMsg == "request:fail timeout") {
                    uni.showToast({
                        title: "当前网络状态不佳请刷新重试",
                        icon: "none"
                    })
                }
            },

            complete() {
                uni.hideLoading()
            }
        })
    })
}
export default request
// #endif
