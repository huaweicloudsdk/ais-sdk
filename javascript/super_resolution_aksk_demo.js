/**
 * 超分重建服务ak,sk方式请求的使用示例
 */
var supresol = require("./ais_sdk/super_resolution");
var utils = require("./ais_sdk/utils");

// 初始化服务的区域信息，目前支持北京1(cn-north-1)、香港(ap-southeast-1)等区域信息
utils.initRegion(region="cn-north-1");

var app_key = "*************";
var app_secret = "************";

var filepath = "./data/super-resolution-demo.png";
var data = utils.changeFileToBase64(filepath);

supresol.super_resolution_aksk(app_key, app_secret, data, 3, "ESPCN", function (result) {
    var resultObj = JSON.parse(result);
    utils.getFileByBase64Str("./data/super-resolution-demo-aksk.png", resultObj.result);
});

