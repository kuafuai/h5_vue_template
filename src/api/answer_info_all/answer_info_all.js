// import request from '@/utils/request';

import service from "@/utils/request";
const BASE_API = "/answerInfoAll";

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


get_question_type_info_list(){
  return service({
    url: BASE_API + "/get/questionTypeInfo_list",
    method: "get"
  });
},


get_survey_info_list(){
  return service({
    url: BASE_API + "/get/surveyInfo_list",
    method: "get"
  });
},


get_question_info_list(){
  return service({
    url: BASE_API + "/get/questionInfo_list",
    method: "get"
  });
},


get_option_info_list(){
  return service({
    url: BASE_API + "/get/optionInfo_list",
    method: "get"
  });
},


get_user_list(){
  return service({
    url: BASE_API + "/get/user_list",
    method: "get"
  });
},

};
