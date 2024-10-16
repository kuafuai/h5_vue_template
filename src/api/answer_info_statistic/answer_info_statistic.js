// import request from '@/utils/request';

import service from "@/utils/request";
const BASE_API = "/answerInfoStatistics";

export default {
answer_info_statistics_1_count(data){
  return service({
    url: BASE_API + "/answer_info_statistics_1_count",
    method: "post",
    data:data
  });
},
answer_info_statistics_2_(data){
  return service({
    url: BASE_API + "/answer_info_statistics_2_",
    method: "post",
    data:data
  });
},
};
