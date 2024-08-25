const tabBarList =['/pages/customer/index', '/pages/visit_management/index', '/pages/business_opportunity/index']
function isCurrentPageTabBar(currentPage) {
    return tabBarList.includes(currentPage);
}


function navigate(path) {
    let basePath = path;
    let params = null;

    // 检查是否包含 '?'
    if (path.includes('?')) {
        const splitPath = path.split('?');
        basePath = splitPath[0]; // 获取 '?' 之前的路径
        const queryString = splitPath[1];

        // 将查询参数解析为对象
        params = queryString.split('&').reduce((acc, current) => {
            const [key, value] = current.split('=');
            acc[key] = value ? decodeURIComponent(value) : null; // 处理可能没有值的情况
            return acc;
        }, {});

        // 将对象转换为 JSON 字符串
        params = JSON.stringify(params);
    }

    // 判断是否为 TabBar 页面
    if (isCurrentPageTabBar(basePath)) {
        if (params) {
            // 在跳转前存储 JSON 格式的参数
            uni.setStorageSync(basePath , params);
        }
        uni.switchTab({ url: basePath });
    } else {
        uni.navigateTo({ url: path });
    }
}
export default  navigate;;
