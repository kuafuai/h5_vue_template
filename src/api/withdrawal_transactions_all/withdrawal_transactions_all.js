// import request from '@/utils/request';

import service from "@/utils/request";
const BASE_API = "/withdrawalTransactionsAll";

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


get_partner_list(){
  return service({
    url: BASE_API + "/get/partner_list",
    method: "get"
  });
},


get_sales_lead_item_list(){
  return service({
    url: BASE_API + "/get/salesLeadItem_list",
    method: "get"
  });
},


get_amount_status_list(){
  return service({
    url: BASE_API + "/get/amountStatus_list",
    method: "get"
  });
},


get_end_amount_status_list(){
  return service({
    url: BASE_API + "/get/endAmountStatus_list",
    method: "get"
  });
},

};
