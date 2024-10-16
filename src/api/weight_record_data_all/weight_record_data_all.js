// import request from '@/utils/request';

import service from "@/utils/request";
const BASE_API = "/weightRecordDataAll";

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


get_user_data_list(){
  return service({
    url: BASE_API + "/get/userData_list",
    method: "get"
  });
},

};
