import service from "@/utils/request";
const token = uni.getStorageSync("h5_token");
console.log("我看看 Token", token)

export default {

    callBlock(data) {
        return service({
            url: "/chatbot/chat_block",
            method: "post",
            data: data,
        });
    },

    downLoadWord(data) {
        return service({
            url: "/chatbot/download",
            method: "post",

            data: data,
        });
    }
};
