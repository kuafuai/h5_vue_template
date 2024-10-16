function back(item) {
    const pages = getCurrentPages();
    const currentPage = pages[pages.length - 1]; // 获取当前页面实例
    const currentPath = currentPage.route; // 当前页面路径
    console.log(currentPath)
    var split = currentPath.split("/")
    uni.setStorageSync(split[1], item)
    uni.navigateBack()
}
export default back;