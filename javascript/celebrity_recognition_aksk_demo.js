/**
 * 名人识别检测服务的aksk 方式请求的使用示例
 */
var recognition = require("./ais_sdk/celebrity_recognition");
var utils = require("./ais_sdk/utils");

var app_key = "*************";
var app_secret = "************";

var filepath = "./data/celebrity-recognition.jpg";
var data = utils.changeFileToBase64(filepath);

demo_data_url = 'https://ais-sample-data.obs.cn-north-1.myhuaweicloud.com/celebrity-recognition.jpg';

recognition.celebrity_recognition_aksk(app_key, app_secret, data, "", 0.48, function (result) {
    console.log(result);
});

recognition.celebrity_recognition_aksk(app_key, app_secret, "", demo_data_url, 0.48, function (result) {
    console.log(result);
});