/**
 * 语音合成服务的使用示例
 */
var sectence = require("./ais_sdk/tts");
var token = require("./ais_sdk/gettoken");
var utils = require("./ais_sdk/utils");

var username = "*******";       // 配置用户名
var domainname = "*******";     // 配置用户名
var password = "*******";       // 密码
var regionName = "cn-north-1";  // 配置区域信息

/**
 * token 方式获取结果
 * @type {string}
 */
token.getToken(username, domainname, password, regionName, function (token) {

    sectence.tts(token, "This is a test sample", "xiaoyan", 0, "16k", 0, 0, function (result) {
        var resultObj = JSON.parse(result);
        utils.getFileByBase64Str("./data/tts_token_sample.wav", resultObj.result.data);
    })
});

/**
 * aksk 方式获取结果
 * @type {string}
 */
var app_key = "*************";
var app_secret = "************";

sectence.tts_aksk(app_key, app_secret, "This is a test sample", "xiaoyan", 0, "16k", 0, 0, function (result) {
    var resultObj = JSON.parse(result);
    utils.getFileByBase64Str("./data/tts_aksk_sample.wav", resultObj.result.data);
});

