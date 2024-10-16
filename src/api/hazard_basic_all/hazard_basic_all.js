// import request from '@/utils/request';

import service from "@/utils/request";
const BASE_API = "/hazardBasicAll";

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


get_hazard_type_status_list(){
  return service({
    url: BASE_API + "/get/hazardTypeStatus_list",
    method: "get"
  });
},


get_category_menu_list(){
  return service({
    url: BASE_API + "/get/categoryMenu_list",
    method: "get"
  });
},


get_hazard_level_status_list(){
  return service({
    url: BASE_API + "/get/hazardLevelStatus_list",
    method: "get"
  });
},

};
