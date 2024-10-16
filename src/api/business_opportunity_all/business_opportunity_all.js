// import request from '@/utils/request';

import service from "@/utils/request";
const BASE_API = "/businessOpportunityAll";

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


get_opportunity_status_list(){
  return service({
    url: BASE_API + "/get/opportunityStatus_list",
    method: "get"
  });
},


get_salesman_list(){
  return service({
    url: BASE_API + "/get/salesman_list",
    method: "get"
  });
},


get_customer_list(){
  return service({
    url: BASE_API + "/get/customer_list",
    method: "get"
  });
},


get_visit_management_list(){
  return service({
    url: BASE_API + "/get/visitManagement_list",
    method: "get"
  });
},


get_customer_stage_list(){
  return service({
    url: BASE_API + "/get/customerStage_list",
    method: "get"
  });
},

};
