const tabBarList =['home:首页', 'profile:个人中心']
const homePageName='login'
function isCurrentPageTabBar(currentPage) {
    return tabBarList.includes(currentPage);
}


function navigate(path,is_redirect) {
    let basePath = path;
    let params = null;

    if (path.startsWith('/'+homePageName)){
        path="/pages"+path;
    }else if(!path.startsWith("/pages") && !path.startsWith("/pageA")){
        path="/pagesA"+path;
    }

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
         if (is_redirect) {
            uni.redirectTo({url: path});
        } else {
            uni.navigateTo({url: path});
        }    }
}
export default  navigate;