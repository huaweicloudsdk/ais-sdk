/**
 * 语音合成服务token请求方式的使用示例
 */
var sectence = require("./ais_sdk/tts");
var token = require("./ais_sdk/gettoken");
var utils = require("./ais_sdk/utils");

var username = "*******";        // 配置用户名
var domain_name = "*******";     // 配置用户名
var password = "*******";        // 密码

token.getToken(username, domain_name, password, function (token) {

    sectence.tts(token, "This is a test sample", "xiaoyan", 0, "16k", 0, 0, function (result) {
        var resultObj = JSON.parse(result);
        utils.getFileByBase64Str("./data/tts_token_sample.wav", resultObj.result.data);
    })
});
