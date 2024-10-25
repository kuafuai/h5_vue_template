import service from "@/utils/request";

const BASE_API = "/userInfo";

export default {
    page(query) {
        return service({
            url: BASE_API + "/page",
            method: "post",
            data: query
        });
    },
    get(id){
        return service({
            url: BASE_API + "/get/" + id,
            method: "get"
        });
    },
    add(data) {
        return service({
            url: BASE_API + "/add",
            method: "post",
            data: data
        });
    },
    update(data) {
        return service({
            url: BASE_API + "/update",
            method: "put",
            data: data
        });
    },
    delete(id) {
        return service({
            url: BASE_API + "/delete/" + id,
            method: "delete"
        });
    },
    assignTask(data){
        return service({
            url: BASE_API + "/assign",
            method: "post",
            data: data
        });
    },
    select_list(){
        return service({
            url: BASE_API + "/get/select_list",
            method: "get"
        });
    }
};