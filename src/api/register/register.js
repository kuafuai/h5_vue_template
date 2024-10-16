// import request from '@/utils/request';
import service from "@/utils/request";


export default {
    register(data) {
        return service({
            url: "/login/register/passwd",
            method: "post",
            data: data
        });
    },
    get_not_null_field(param,register_type) {
        return service({
            url: "/login/get_not_null_field/" + param +"/"+ register_type,
            method: "get"
        });
    }
};
