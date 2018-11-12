/**
 * 低光照增强服务的使用示例
 */
var enhance = require("./ais_sdk/dark_enhance");
var token = require("./ais_sdk/gettoken");
var utils = require("./ais_sdk/utils");

var username = "*******";       // 配置用户名
var domainname = "*******";     // 配置用户名
var password = "*******";       // 密码
var regionName = "cn-north-1";  // 配置区域信息

var filepath = "./data/dark-enhance-demo.bmp";
var data = utils.changeFileToBase64(filepath);

/**
 * token 方式获取结果
 * @type {string}
 */
token.getToken(username, domainname, password, regionName, function (token) {

    enhance.dark_enhance(token, data, 0.9, function (result) {
        var resultObj = JSON.parse(result);
        utils.getFileByBase64Str("./data/dark-enhance-demo-token.bmp", resultObj.result);
    })
});

/**
 * aksk 方式获取结果
 * @type {string}
 */
var app_key = "*************";
var app_secret = "************";

enhance.dark_enhance_aksk(app_key, app_secret, data, 0.9, function (result) {
    var resultObj = JSON.parse(result);
    utils.getFileByBase64Str("./data/dark-enhance-demo-aksk.bmp", resultObj.result);
});

