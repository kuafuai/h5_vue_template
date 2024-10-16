// import request from '@/utils/request';

import service from "@/utils/request";
const BASE_API = "/caseProgressAll";

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


get_case_info_list(){
  return service({
    url: BASE_API + "/get/caseInfo_list",
    method: "get"
  });
},

};
