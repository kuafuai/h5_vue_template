import service from "@/utils/request";

const BASE_API = "/flowable/definition";

export default {
    definitionStart(procDefId, query) {
        return service({
            url: BASE_API + "/start/" + procDefId,
            method: "post",
            data: query
        });
    },
    page(query) {
        return service({
            url: BASE_API + "/page",
            method: "post",
            data: query
        });
    },
    get(id) {
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
    }
};