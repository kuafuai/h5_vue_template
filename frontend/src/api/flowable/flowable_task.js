import service from "@/utils/request";

const BASE_API = "/flowable/task";

export default {
    page(query) {
        return service({
            url: BASE_API + "/page",
            method: "post",
            data: query
        });
    },
    myProcess(query) {
        return service({
            url: BASE_API + "/myProcess",
            method: "get",
            params: query
        });
    },
    todoList(query) {
        return service({
            url: BASE_API + "/todoList",
            method: "get",
            params: query
        });
    },
    flowFormData(query) {
        return service({
            url: BASE_API + "/flowFormData",
            method: "get",
            params: query
        });
    },
    getNextFlowNodeByStart(query) {
        return service({
            url: BASE_API + "/nextFlowNodeByStart",
            method: "post",
            data: query
        });
    },

    getProcessVariables(taskId) {
        return service({
            url: BASE_API + "/processVariables/" + taskId,
            method: "get"
        });
    },
    flowRecord(query){
        return service({
            url: BASE_API + "/flowRecord",
            method: "get",
            params: query
        });
    },
    stopProcess(data){
        return service({
            url: BASE_API + "/stopProcess",
            method: "post",
            data: data
        });
    },
    flowTaskForm(query){
        return service({
            url: BASE_API + "/flowTaskForm",
            method: "get",
            params: query
        });
    },
    complete(data){
        return service({
            url: BASE_API + "/complete",
            method: "post",
            data: data
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