/**
 * 图像标签检测服务aksk请求方式的使用示例
 */
var tagging = require("./ais_sdk/image_tagging");
var utils = require("./ais_sdk/utils");

// 初始化服务的区域信息，目前支持华北-北京一(cn-north-1)、亚太-香港(ap-southeast-1)等区域信息
utils.initRegion("cn-north-1");

var app_key = "*************";
var app_secret = "************";

var filepath = "./data/image-tagging-demo.jpg";
var data = utils.changeFileToBase64(filepath);

// obs链接需要和region区域一致，不同的region的obs资源不共享
demo_data_url = "https://ais-sample-data.obs.myhuaweicloud.com/tagging-normal.jpg";

tagging.image_tagging_aksk(app_key, app_secret, data, "", 60, "en", 5, function (result) {
    console.log(result);
});

tagging.image_tagging_aksk(app_key, app_secret, "", demo_data_url, 60, "en", 5, function (result) {
    console.log(result);
});