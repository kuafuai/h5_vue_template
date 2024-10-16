// import request from '@/utils/request';

import service from "@/utils/request";
const BASE_API = "/customerAll";

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


get_industry_list(){
  return service({
    url: BASE_API + "/get/industry_list",
    method: "get"
  });
},


get_personnel_scale_list(){
  return service({
    url: BASE_API + "/get/personnelScale_list",
    method: "get"
  });
},


get_customer_hierarchy_list(){
  return service({
    url: BASE_API + "/get/customerHierarchy_list",
    method: "get"
  });
},

};
