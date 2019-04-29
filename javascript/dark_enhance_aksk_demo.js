/**
 * 低光照增强服务ak，sk方式请求的使用示例
 */
var enhance = require("./ais_sdk/dark_enhance");
var utils = require("./ais_sdk/utils");

// 初始化服务的区域信息，目前支持华北-北京一(cn-north-1)、亚太-香港(ap-southeast-1)等区域信息
utils.initRegion("cn-north-1");

var app_key = "*************";
var app_secret = "************";

var filepath = "./data/dark-enhance-demo.bmp";
var data = utils.changeFileToBase64(filepath);

enhance.dark_enhance_aksk(app_key, app_secret, data, 0.9, function (result) {
    var resultObj = JSON.parse(result);
    utils.getFileByBase64Str("./data/dark-enhance-demo-aksk.bmp", resultObj.result);
});

