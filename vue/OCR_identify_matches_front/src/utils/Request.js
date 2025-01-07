import router from'../router'
import axios from 'axios'

let reqID = 1
function getReqID(){
    reqID = reqID + 1
    return reqID
}

export const key_DingTokenJWT = "sensor_DingTokenJWT"

export function PostData(method, postURL, data,  callSuccess, callFail) {
  let userToken = localStorage.getItem(key_DingTokenJWT) || ""; // 获取 token，如果没有则为空字符串

  let postPack = {
    reqID: getReqID(),
    method: method,
    sender: "",
    sendee: "",
    token: userToken,
    reqData: data
  };

  return axios.post(postURL, JSON.stringify(postPack), {
    headers: {
      "content-type": "application/json"
    }
  })
    .then(function (response) {
      if (response.data.result === 1) {
        if (callSuccess) {
          callSuccess(response.data.respData);
        }
        return response.data.respData; // 返回成功数据
      } else {
        if (callFail) {
          callFail(response.data.msg);
        }
        throw new Error(response.data.msg); // 抛出错误
      }
    })
    .catch(function (error) {
      console.log(error);
      if (callFail) {
        callFail(error);
      }
      throw error; // 抛出错误
    });
}

