/**
 * 翻拍识别服务ak,sk请求方式的使用示例
 */
var recapture = require("./ais_sdk/recapture_detect");
var utils = require("./ais_sdk/utils");

var app_key = "*************";
var app_secret = "************";

var filepath = "./data/recapture-detect-demo.jpg";
var data = utils.changeFileToBase64(filepath);

demo_data_url = "https://ais-sample-data.obs.myhuaweicloud.com/recapture-detect.jpg";

recapture.recapture_detect_aksk(app_key, app_secret, data, "", 0.99, ['recapture'], function (result) {
    console.log(result);
});

recapture.recapture_detect_aksk(app_key, app_secret, "", demo_data_url, 0.99, ['recapture'], function (result) {
    console.log(result);
});