// import request from '@/utils/request';

import service from "@/utils/request";
const BASE_API = "/hazardInfoAll";

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


get_task_info_list(){
  return service({
    url: BASE_API + "/get/taskInfo_list",
    method: "get"
  });
},


get_hazard_acceptance_status_list(){
  return service({
    url: BASE_API + "/get/hazardAcceptanceStatus_list",
    method: "get"
  });
},


get_enterprise_information_list(){
  return service({
    url: BASE_API + "/get/enterpriseInformation_list",
    method: "get"
  });
},


get_hazard_basic_list(){
  return service({
    url: BASE_API + "/get/hazardBasic_list",
    method: "get"
  });
},


get_correlation_basis_status_list(){
  return service({
    url: BASE_API + "/get/correlationBasisStatus_list",
    method: "get"
  });
},

};
