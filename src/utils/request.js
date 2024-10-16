// #ifdef MP-WEIXIN
let baseurl = import.meta.env.VITE_APP_SERVICE_API;
// #endif

// #ifdef H5
let baseurl = import.meta.env.VITE_APP_BASE_API;
// #endif
// #ifdef H5
import axios from 'axios';

// 创建 axios 实例
const service = axios.create({
    baseURL: baseurl,
    timeout: 50000,
    headers: {'Content-Type': 'application/json;charset=utf-8'},
});

const useLogin = import.meta.env.VITE_USE_LOGIN === 'true'
// 请求拦截器
service.interceptors.request.use(
    (config) => {
        if (!config.headers) {
            throw new Error(`Expected 'config' and 'config.headers' not to be undefined`);
        }

        // 可选：定义 useLogin 函数或移除相关代码
        if (uni.getStorageSync("h5_token")) {
            config.headers.Authorization = 'Bearer ' + uni.getStorageSync("h5_token");
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
        const {code, message} = res;
        if (code === 0) {
            return res;
        } else {
            if (code === 401 || code === 403) {
                uni.removeStorageSync("h5_token");
                location.href =
                    import.meta.env.VITE_BASE;
            } else {
                uni.showToast({
                    title: message || '系统出错',
                    icon: "none"
                })
                // ElMessage({
                //     message: message || '系统出错',
                //     type: 'error',
                //     duration: 5 * 1000,
                // });
            }
            return Promise.reject(new Error(message || 'Error'));
        }
    },
    (error) => {
        console.log('请求异常：', error);
        if (error.response.status === 401) {
            // localStorage.removeItem("h5_token");
            uni.removeStorageSync("h5_token");
            location.href =
                import.meta.env.VITE_BASE;
        } else {
            uni.showToast({
                title: '网络异常，请稍后再试!',
                icon: "none"
            })
            // ElMessage({
            //     message: '网络异常，请稍后再试!',
            //     type: 'error',
            //     duration: 5 * 1000,
            // });
            return Promise.reject(new Error('Error'));
        }
    },
);
export default service
// #endif
// #ifdef MP-WEIXIN
let service = (res) => {
    console.log("加载中", res)
    uni.showLoading({
        title: '加载中',
    })

    let {url, data, method, token} = res
    token = uni.getStorageSync("h5_token")
    console.log("token", token)

    return new Promise((resolve, reject) => {
        console.log("1234567654321234567", url)
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
                resolve(res.data)
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
                } else if (res.data.code != 0) {
                    uni.showToast({
                        title: res.data.message || '系统出错',
                        icon: "none"
                    })
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
export default service
// #endif
