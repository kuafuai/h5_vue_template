// import request from '@/utils/request';
import service from "@/utils/request";

const BASE_API = "/surveyWriteA9a959e";

export default {

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

};
