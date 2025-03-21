let STATIC_URL = import.meta.env.VITE_STATIC_URL
if (STATIC_URL === undefined || STATIC_URL === '' || STATIC_URL == null) {
    STATIC_URL = 'https://kuafuai.obs.cn-east-3.myhuaweicloud.com/h5_vue_template/'
}
const get_resource_url = (path) => {
    if (STATIC_URL)
        return STATIC_URL + path
}
export default get_resource_url