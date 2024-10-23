import service from "@/utils/request";

const BASE_API = "/changeManager";

export default {
    page(query) {
        return service({
            url: BASE_API + "/page",
            method: "post",
            data: query
        });
    },
    myTake(query) {
        return service({
            url: BASE_API + "/myTake",
            method: "post",
            data: query
        });
    },
    page_info(query) {
        return service({
            url: BASE_API + "/page-info",
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
    },
    flowFormData(query) {
        return service({
            url: BASE_API + "/flowFormData",
            method: "get",
            params: query
        });
    },
    changeRecords(query){
        return service({
            url: BASE_API + "/changeRecords",
            method: "get",
            params: query
        });
    },
    changeRecordsAll(query){
        return service({
            url: BASE_API + "/changeRecordsAll",
            method: "get",
            params: query
        });
    },
    completeApprove(data) {
        return service({
            url: BASE_API + "/completeApprove",
            method: "post",
            data: data
        });
    },
    completeSubmit(data) {
        return service({
            url: BASE_API + "/completeSubmit",
            method: "post",
            data: data
        });
    },
    completeSubmitUpload(data) {
        return service({
            url: BASE_API + "/completeSubmitUpload",
            method: "post",
            data: data
        });
    },
    completeCheckFile(data) {
        return service({
            url: BASE_API + "/completeCheckFile",
            method: "post",
            data: data
        });
    },
    myTodo(){
        return service({
            url: BASE_API + "/myTodo",
            method: "get"
        });
    },
    myStatics(){
        return service({
            url: BASE_API + "/myStatics",
            method: "get"
        });
    },
    stopProcess(data){
        return service({
            url: BASE_API + "/stop",
            method: "post",
            data: data
        });
    },
    rejectProcess(data){
        return service({
            url: BASE_API + "/reject",
            method: "post",
            data: data
        });
    }
};