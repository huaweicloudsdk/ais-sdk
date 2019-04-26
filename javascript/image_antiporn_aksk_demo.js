/**
 * 图像反黄检测服务ak,sk方式请求的使用示例
 */
var antiporn = require("./ais_sdk/image_antiporn");
var utils = require("./ais_sdk/utils");

// 初始化服务的区域信息，目前支持华北-北京一(cn-north-1)、亚太-香港(ap-southeast-1)等区域信息
utils.initRegion("cn-north-1");

var app_key = "*************";
var app_secret = "************";

var filepath = "./data/moderation-antiporn.jpg";
var data = utils.changeFileToBase64(filepath);

demo_data_url = 'https://ais-sample-data.obs.cn-north-1.myhuaweicloud.com/antiporn.jpg';

antiporn.image_antiporn_aksk(app_key, app_secret, data, "", function (result) {
    console.log(result);
});

antiporn.image_antiporn_aksk(app_key, app_secret, "", demo_data_url, function (result) {
    console.log(result);
});