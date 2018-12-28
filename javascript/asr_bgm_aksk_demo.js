/**
 * 背景音乐识别服务aksk 方式请求的使用示例
 */
var bgm = require("./ais_sdk/asr_bgm");

var app_key = "**************";
var app_secret = "************";

obsUrl = "https://obs-test-llg.obs.cn-north-1.myhuaweicloud.com/bgm_recognition";

bgm.asr_bgm_aksk(app_key, app_secret, obsUrl, function (result) {
    console.log(result);
});
