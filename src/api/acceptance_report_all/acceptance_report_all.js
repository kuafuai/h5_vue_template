// import request from '@/utils/request';

import service from "@/utils/request";
const BASE_API = "/acceptanceReportAll";

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


get_hazard_info_list(){
  return service({
    url: BASE_API + "/get/hazardInfo_list",
    method: "get"
  });
},


get_rectification_progress_status_list(){
  return service({
    url: BASE_API + "/get/rectificationProgressStatus_list",
    method: "get"
  });
},

};
