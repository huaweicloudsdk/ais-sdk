/**
 * 语音合成服务ak,sk请求方式的使用示例
 */
var sectence = require("./ais_sdk/tts");
var utils = require("./ais_sdk/utils");

var app_key = "*************";
var app_secret = "************";

sectence.tts_aksk(app_key, app_secret, "This is a test sample", "xiaoyan", 0, "16k", 0, 0, function (result) {
    var resultObj = JSON.parse(result);
    utils.getFileByBase64Str("./data/tts_aksk_sample.wav", resultObj.result.data);
});

