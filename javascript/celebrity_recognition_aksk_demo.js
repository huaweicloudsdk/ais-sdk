/**
 * 名人识别检测服务的aksk 方式请求的使用示例
 */
var recognition = require("./ais_sdk/celebrity_recognition");
var utils = require("./ais_sdk/utils");

// 初始化服务的区域信息，目前支持华北-北京一(cn-north-1)、亚太-香港(ap-southeast-1)等区域信息
utils.initRegion("cn-north-1");

var app_key = "*************";
var app_secret = "************";

var filepath = "./data/celebrity-recognition.jpg";
var data = utils.changeFileToBase64(filepath);

// obs链接需要和region区域一致，不同的region的obs资源不共享
demo_data_url = 'https://ais-sample-data.obs.cn-north-1.myhuaweicloud.com/celebrity-recognition.jpg';

recognition.celebrity_recognition_aksk(app_key, app_secret, data, "", 0.48, function (result) {
    console.log(result);
});

recognition.celebrity_recognition_aksk(app_key, app_secret, "", demo_data_url, 0.48, function (result) {
    console.log(result);
});