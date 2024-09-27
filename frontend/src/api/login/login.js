import service from "@/utils/request";

export default {
    loginQyWxWeb(data) {
        return service({
            url: "/login/qyWxWeb",
            method: "post",
            data: data
        });
    },
    loginWxWeb(data) {
        return service({
            url: "/login/wxWeb",
            method: "post",
            data: data
        });
    },
    loginPasswd(data) {
        return service({
            url: "/login/passwd",
            method: "post",
            data: data
        });
    },
    getMessage() {
        return service({
            url: "/getUserInfo",
            method: "get",

        });
    },
    getLoginUser() {
        return service({
            url: "/getLoginUser",
            method: "get",
        });
    },
    getCodeByPhone(phone) {
        return service({
            url: "/login/sms/code?phone=" + phone,
            method: "get",

        });
    },
    loginBySms(data) {
        return service({
            url: "/login/sms",
            method: "post",
            data: data
        });
    }
};