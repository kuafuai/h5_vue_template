import axios from 'axios'
import {ElLoading} from 'element-plus'
import {saveAs} from 'file-saver'

const baseURL = import.meta.env.VITE_APP_BASE_API;

export default {
    name(name) {
        let downloadLoadingInstance = ElLoading.service({
            text: "正在下载数据，请稍候",
            spinner: "el-icon-loading",
            background: "rgba(0, 0, 0, 0.7)",
        })

        let url = baseURL + "/common/download?fileName=" + encodeURIComponent(name)
        axios({
            method: 'get',
            url: url,
            responseType: 'blob',
            headers: {'Authorization': 'Bearer ' + localStorage.getItem("h5_token")},
            onDownloadProgress: (progressEvent) => {
                const percentCompleted = Math.round((progressEvent.loaded / progressEvent.total) * 100);
                downloadLoadingInstance.setText(`下载进度：${percentCompleted}%`)
            }
        }).then((res) => {
            const blob = new Blob([res.data])
            this.saveAs(blob, decodeURIComponent(res.headers['download-filename']))
            downloadLoadingInstance.close();
        }).catch((r) => {
            downloadLoadingInstance.close();
        })
    },
    download(url, params, filename) {
        let realUrl = baseURL + url;
        axios({
            method: 'post',
            url: realUrl,
            params: params,
            responseType: 'blob',
            headers: {
                'Authorization': 'Bearer ' + localStorage.getItem("h5_token"),
                'Content-Type': 'application/x-www-form-urlencoded'
            }
        }).then((res) => {
            const blob = new Blob([res.data])
            this.saveAs(blob, filename);
        });
    },
    downloadGet(url, params, filename) {
        let realUrl = baseURL + url;
        axios({
            method: 'get',
            url: realUrl,
            params: params,
            responseType: 'blob',
            headers: {
                'Authorization': 'Bearer ' + localStorage.getItem("h5_token"),
                'Content-Type': 'application/x-www-form-urlencoded'
            }
        }).then((res) => {
            const blob = new Blob([res.data])
            this.saveAs(blob, filename);
        });
    },
    saveAs(text, name, opts) {
        saveAs(text, name, opts);
    }
}