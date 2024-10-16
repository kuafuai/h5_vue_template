// import request from '@/utils/request';

import service from "@/utils/request";
const BASE_API = "/followCommunicateInfoAll";

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


get_project_info_list(){
  return service({
    url: BASE_API + "/get/projectInfo_list",
    method: "get"
  });
},


get_buiness_person_list(){
  return service({
    url: BASE_API + "/get/buinessPerson_list",
    method: "get"
  });
},

};
