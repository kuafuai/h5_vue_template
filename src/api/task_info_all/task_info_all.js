// import request from '@/utils/request';

import service from "@/utils/request";
const BASE_API = "/taskInfoAll";

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


get_task_status_enums_list(){
  return service({
    url: BASE_API + "/get/taskStatusEnums_list",
    method: "get"
  });
},


get_enterprise_information_list(){
  return service({
    url: BASE_API + "/get/enterpriseInformation_list",
    method: "get"
  });
},

};
