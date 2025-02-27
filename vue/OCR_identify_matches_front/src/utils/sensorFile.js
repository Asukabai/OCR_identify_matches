import * as ssDing from '../utils/Request';

const SensorFileRequest = {
  uploadFileLog(callSuccess, callFail) {
    return ssDing.PostData("uploadFileLog", "http://localhost:20122/ss/service/getUploadLog", null, respData => {
      callSuccess(respData);
    }, callFail);
  },

  searchOperations(param, callSuccess, callFail) {
    return ssDing.PostData("searchOperations", "http://localhost:20122/ss/view/getQueryFileInfoWithImages", param, respData => {
      callSuccess(respData);
    }, callFail);
  }
};

export default SensorFileRequest;
