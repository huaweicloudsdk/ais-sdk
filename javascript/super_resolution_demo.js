/**
 * 超分重建服务的使用示例
 */
var supresol = require("./ais_sdk/super_resolution");
var token = require("./ais_sdk/gettoken");
var utils = require("./ais_sdk/utils");

var username = "*******";       // 配置用户名
var domainname = "*******";     // 配置用户名
var password = "*******";       // 密码
var regionName = "cn-north-1";  // 配置区域信息

var filepath = "./data/super-resolution-demo.png";
var data = utils.changeFileToBase64(filepath);

/**
 * token 方式获取结果
 * @type {string}
 */
token.getToken(username, domainname, password, regionName, function (token) {

    supresol.super_resolution(token, data, 3, "ESPCN", function (result) {
        var resultObj = JSON.parse(result);
        utils.getFileByBase64Str("./data/super-resolution-demo-token.png", resultObj.result);
    })
});

/**
 * aksk 方式获取结果
 * @type {string}
 */
var app_key = "*************";
var app_secret = "************";

supresol.super_resolution_aksk(app_key, app_secret, data, 3, "ESPCN", function (result) {
    var resultObj = JSON.parse(result);
    utils.getFileByBase64Str("./data/super-resolution-demo-aksk.png", resultObj.result);
});

