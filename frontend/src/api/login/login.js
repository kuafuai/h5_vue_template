
import service from "@/utils/request";

export default {
    loginWxWeb(data){
        return service({
            url: "/login/wxWeb",
            method: "post",
            data: data
        });
    },
    loginPasswd(data){
        return service({
            url: "/login/passwd",
            method: "post",
            data: data
        });
    },
    getMessage(){
        return service({
            url: "/getUserInfo",
            method: "get",

        });
    },
    getLoginUser(){
        return service({
            url: "/getLoginUser",
            method: "get",
        });
    }
};