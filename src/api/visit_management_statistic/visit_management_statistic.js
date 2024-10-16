// import request from '@/utils/request';

import service from "@/utils/request";
const BASE_API = "/visitManagementStatistics";

export default {
visit_management_statistics_1_count(data){
  return service({
    url: BASE_API + "/visit_management_statistics_1_count",
    method: "post",
    data:data
  });
},
visit_management_statistics_2_count(data){
  return service({
    url: BASE_API + "/visit_management_statistics_2_count",
    method: "post",
    data:data
  });
},
visit_management_statistics_3_count(data){
  return service({
    url: BASE_API + "/visit_management_statistics_3_count",
    method: "post",
    data:data
  });
},
visit_management_statistics_4_count(data){
  return service({
    url: BASE_API + "/visit_management_statistics_4_count",
    method: "post",
    data:data
  });
},
};
