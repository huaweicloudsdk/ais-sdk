/**
 * 图像清晰度检测服务aksk 方式请求的使用示例
 */
var clarity = require("./ais_sdk/clarity_detect");
var utils = require("./ais_sdk/utils");

var app_key = "*************";
var app_secret = "************";

var filepath = "./data/moderation-clarity.jpg";
var data = utils.changeFileToBase64(filepath);

demo_data_url = "https://ais-sample-data.obs.cn-north-1.myhwclouds.com/vat-invoice.jpg";

clarity.clarity_detect_aksk(app_key, app_secret, data, "", 0.8, function (result) {
    console.log(result);
});

clarity.clarity_detect_aksk(app_key, app_secret, "", demo_data_url, 0.8, function (result) {
    console.log(result);
});