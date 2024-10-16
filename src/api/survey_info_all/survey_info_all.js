// import request from '@/utils/request';

import service from "@/utils/request";
const BASE_API = "/surveyInfoAll";

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


get_survey_status_info_list(){
  return service({
    url: BASE_API + "/get/surveyStatusInfo_list",
    method: "get"
  });
},


get_company_list(){
  return service({
    url: BASE_API + "/get/company_list",
    method: "get"
  });
},

};
