/**
 * 背景音乐识别服务aksk 方式请求的使用示例
 */
var bgm = require("./ais_sdk/asr_bgm");
var utils = require("./ais_sdk/utils");

// 初始化服务的区域信息，目前支持华北-北京一(cn-north-1)、亚太-香港(ap-southeast-1)等区域信息
utils.initRegion(region="cn-north-1");

var app_key = "**************";
var app_secret = "************";

// obs链接需要和region区域一致，不同的region的obs资源不共享
obsUrl = "https://obs-test-llg.obs.cn-north-1.myhuaweicloud.com/bgm_recognition";

bgm.asr_bgm_aksk(app_key, app_secret, obsUrl, function (result) {
    console.log(result);
});
