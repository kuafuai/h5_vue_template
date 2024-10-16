// import request from '@/utils/request';

import service from "@/utils/request";
const BASE_API = "/withdrawalTransactionsStatistics";

export default {
withdrawal_transactions_statistics_3_sum(data){
  return service({
    url: BASE_API + "/withdrawal_transactions_statistics_3_sum",
    method: "post",
    data:data
  });
},
withdrawal_transactions_statistics_1_sum(data){
  return service({
    url: BASE_API + "/withdrawal_transactions_statistics_1_sum",
    method: "post",
    data:data
  });
},
withdrawal_transactions_statistics_2_sum(data){
  return service({
    url: BASE_API + "/withdrawal_transactions_statistics_2_sum",
    method: "post",
    data:data
  });
},
};
