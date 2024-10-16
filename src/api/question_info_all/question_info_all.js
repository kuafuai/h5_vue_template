// import request from '@/utils/request';

import service from "@/utils/request";
const BASE_API = "/questionInfoAll";

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


get_survey_info_list(){
  return service({
    url: BASE_API + "/get/surveyInfo_list",
    method: "get"
  });
},


get_question_type_info_list(){
  return service({
    url: BASE_API + "/get/questionTypeInfo_list",
    method: "get"
  });
},


get_required_flag_info_list(){
  return service({
    url: BASE_API + "/get/requiredFlagInfo_list",
    method: "get"
  });
},

};
