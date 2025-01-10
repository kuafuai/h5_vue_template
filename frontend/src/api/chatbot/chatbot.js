import service from "@/utils/request";

const BASE_API =  import.meta.env.VITE_APP_BASE_API;

const ctrl = new AbortController()
const conversion_id = ref()
let times = 0
const token = uni.getStorageSync("h5_token");
console.log("我看看 Token", token)

export default {

    downLoadWord(data) {
        return service({
            url: "/chatbot/download",
            method: "post",

            data: data,
        });
    },
};
