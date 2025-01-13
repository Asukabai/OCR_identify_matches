
import * as ssDing from '../utils/Request';



const SensorFileRequest = {


    uploadFileLog(callSuccess, callFail) {
      return ssDing.PostData("uploadFileLog", "http://192.168.1.25:20122/ss/service/getUploadLog", null, respData => {
        callSuccess(respData);
      }, callFail);
    },

    // fetchOperations(param,callSuccess, callFail) {
    //   return ssDing.PostData("fetchOperations", "http://localhost:28009/ss/view/getAllFileInfoWithImages", param, respData => {
    //     callSuccess(respData);
    //   }, callFail);
    // },

    searchOperations(param,callSuccess, callFail) {
        return ssDing.PostData("searchOperations", "http://192.168.1.25:20122/ss/view/getQueryFileInfoWithImages", param, respData => {
          callSuccess(respData);
        }, callFail);
      }
};

  export default SensorFileRequest;

