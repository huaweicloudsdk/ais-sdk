/**
 * 图像标签检测服务aksk请求方式的使用示例
 */
var tagging = require("./ais_sdk/image_tagging");
var utils = require("./ais_sdk/utils");

var app_key = "*************";
var app_secret = "************";

var filepath = "./data/image-tagging-demo.jpg";
var data = utils.changeFileToBase64(filepath);

demo_data_url = "https://ais-sample-data.obs.myhuaweicloud.com/tagging-normal.jpg";

tagging.image_tagging_aksk(app_key, app_secret, data, "", 60, "en", 5, function (result) {
    console.log(result);
});

tagging.image_tagging_aksk(app_key, app_secret, "", demo_data_url, 60, "en", 5, function (result) {
    console.log(result);
});