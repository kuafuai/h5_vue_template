// import request from '@/utils/request';

import service from "@/utils/request";
const BASE_API = "/category";

export default {
    list(query) {
        return service({
            url: BASE_API + "/list",
            method: "post",
            data: query
        });
    },
};
