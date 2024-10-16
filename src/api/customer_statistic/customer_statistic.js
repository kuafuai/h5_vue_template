// import request from '@/utils/request';

import service from "@/utils/request";
const BASE_API = "/customerStatistics";

export default {
customer_base_statistics_1_count(data){
  return service({
    url: BASE_API + "/customer_base_statistics_1_count",
    method: "post",
    data:data
  });
},
customer_statistics_2_count(data){
  return service({
    url: BASE_API + "/customer_statistics_2_count",
    method: "post",
    data:data
  });
},
customer_statistics_3_count(data){
  return service({
    url: BASE_API + "/customer_statistics_3_count",
    method: "post",
    data:data
  });
},
customer_statistics_4_count(data){
  return service({
    url: BASE_API + "/customer_statistics_4_count",
    method: "post",
    data:data
  });
},
};
