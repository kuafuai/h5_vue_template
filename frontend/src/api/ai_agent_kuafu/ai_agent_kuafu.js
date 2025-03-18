// import request from '@/utils/request';
import service from "@/utils/request";

const BASE_API = "/aiAgent";

export default {
    isEnd(query) {
        return service({
            url: BASE_API+"/isEnd",
            method: "post",
            data: query
        });
    },
};
