/**
 * 图像反黄检测服务ak,sk方式请求的使用示例
 */
var antiporn = require("./ais_sdk/image_antiporn");
var utils = require("./ais_sdk/utils");

var app_key = "*************";
var app_secret = "************";

var filepath = "./data/moderation-antiporn.jpg";
var data = utils.changeFileToBase64(filepath);

demo_data_url = 'https://ais-sample-data.obs.cn-north-1.myhwclouds.com/antiporn.jpg';

antiporn.image_antiporn_aksk(app_key, app_secret, data, "", function (result) {
    console.log(result);
});

antiporn.image_antiporn_aksk(app_key, app_secret, "", demo_data_url, function (result) {
    console.log(result);
});