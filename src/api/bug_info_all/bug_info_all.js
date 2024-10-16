// import request from '@/utils/request';

import service from "@/utils/request";
const BASE_API = "/bugInfoAll";

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


get_bug_handle_status_list(){
  return service({
    url: BASE_API + "/get/bugHandleStatus_list",
    method: "get"
  });
},


get_bug_priority_status_list(){
  return service({
    url: BASE_API + "/get/bugPriorityStatus_list",
    method: "get"
  });
},


get_project_info_list(){
  return service({
    url: BASE_API + "/get/projectInfo_list",
    method: "get"
  });
},

};
