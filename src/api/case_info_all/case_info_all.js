// import request from '@/utils/request';

import service from "@/utils/request";
const BASE_API = "/caseInfoAll";

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
  list(query) {
    return service({
      url: BASE_API + "/list",
      method: "post",
      data: query
    });
  },


get_user_info_list(){
  return service({
    url: BASE_API + "/get/userInfo_list",
    method: "get"
  });
},

};
