/**
 * 图像内容检测服务ak,sk请求方式的使用示例
 */
var content = require("./ais_sdk/image_moderation");
var utils = require("./ais_sdk/utils");

// 初始化服务的区域信息，目前支持华北-北京一(cn-north-1)、亚太-香港(ap-southeast-1)等区域信息
utils.initRegion("cn-north-1");

var app_key = "*************";
var app_secret = "************";

var filepath = "./data/moderation-terrorism.jpg";
var data = utils.changeFileToBase64(filepath);

// obs链接需要和region区域一致，不同的region的obs资源不共享
demo_data_url = "https://ais-sample-data.obs.cn-north-1.myhuaweicloud.com/terrorism.jpg";

content.image_content_aksk(app_key, app_secret, data, "", ["politics"], "", function (result) {
    console.log(result);
});

content.image_content_aksk(app_key, app_secret, "", demo_data_url, ["politics"], "", function (result) {
    console.log(result);
});